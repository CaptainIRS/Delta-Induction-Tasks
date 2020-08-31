import eventlet
from datetime import datetime
from random import randint
from flask import Flask, request
from flask_socketio import SocketIO, send, emit, join_room, leave_room, rooms
import mysql.connector as MySQL

app = Flask(__name__)
app.config['SECRET_KEY'] = 'cH13F_c0mM4nD3r'
socketio = SocketIO(app, cors_allowed_origins="*")

onlineUsers = {}

messageDb = MySQL.connect(
  host='chat_db',
  user='db_user',
  password='cH13F_c0mM4nD3r',
  database='chat_db'
) 

dbCursor = messageDb.cursor()

messages = []

dbCursor.execute('SELECT * FROM chat_messages')
res = dbCursor.fetchall()
fields = ('username', 'message', 'timestamp', 'room')
for tup in res:
    messages.append(dict(zip(fields, tup)))

print(messages)

def dept(username):
    if 'Army' in username:
        return 'Army'
    elif 'Navy' in username:
        return 'Navy'
    elif 'AirForce' in username:
        return 'AirForce'
    elif username == 'ChiefCommander':
        return 'ChiefCommander'
    else:
        return None

def getMessagesFromRoom(room):
    roomMsg = []
    for message in messages:
        if message['room'] == room:
            roomMsg.append(message);
    return roomMsg

def getMessageHistory(user):
    history = []
    for message in messages:
        if message['username'] == user or message['username'] == 'ChiefCommander':
            if message['room'] == dept(message['username']) or message['username'] == 'ChiefCommander':
                history.append({
                    'from'     : message['username'],
                    'to'       : 'group', 
                    'message'  : message['message'],
                    'timestamp': message['timestamp'], 
                    'private'  : False 
                })
            else:
                history.append({
                    'from'     : message['username'],
                    'to'       : message['room'].replace(user, ''), 
                    'message'  : message['message'],
                    'timestamp': message['timestamp'], 
                    'private'  : True 
                })
    return history

def prevDetails(username):
    for sid, user in onlineUsers.items():
        if user['username'] == username:
            hue, room, state = user['hue'], user['room'], user['state']
            print('User with username ' + username + ' already present. Deleting...')
            leave_room(sid)
            del onlineUsers[sid]
            return hue, room, state
    else: return None, None, 'offline'

def getPrivateChats(username):
    users = set()
    for message in messages:
        if username in message['room']:
            users.add(message['room'].replace(username, ''))
    return users

@socketio.on('connected', namespace='/chat')
@socketio.on('reconnect', namespace='/chat')
def handleConnected(username):
    hue, room, state = prevDetails(username);
    hue = randint(0, 360) if  hue is None else hue
    department = dept(username)
    room = department if room is None else room
    onlineUsers[str(request.sid)] = {
        'username': username,
        'state'   : 'online',
        'platform': 'browser',
        'hue'     : hue,
        'dept'    : department,
        'room'    : room
    }
    join_room(room)
    if onlineUsers[str(request.sid)]['username'] == 'ChiefCommander':
        join_room('Army')
        join_room('Navy')
        join_room('AirForce')
    if state == 'offline':
        emit('room history', getMessagesFromRoom(room))
    emit('user joined', {
        'username': username, 
        'hue'     : hue
    }, room = room)
    emit('private chats', list(getPrivateChats(username)))
    emit('current users', [usr for usr in list(onlineUsers.values()) or [] if usr['room'] == room])
    print(onlineUsers)

@socketio.on('join', namespace='/chat')
def joinRoom(data):
    room = ''
    if data['to'] == 'group':
        room = onlineUsers[str(request.sid)]['dept']
        if onlineUsers[str(request.sid)]['username'] == 'ChiefCommander':
            join_room('Army')
            join_room('Navy')
            join_room('AirForce')
    elif onlineUsers[str(request.sid)]['username'] < data['to']:
        room = room + onlineUsers[str(request.sid)]['username'] + data['to']
    else:
        room = room + data['to'] + onlineUsers[str(request.sid)]['username']
    join_room(room)
    print(onlineUsers[str(request.sid)])
    emit('room history', getMessagesFromRoom(room))
    emit('user joined', {
        'username': onlineUsers[str(request.sid)]['username'], 
        'hue'     : onlineUsers[str(request.sid)]['hue']
    }, room = room)
    emit('current users', [usr for usr in list(onlineUsers.values()) or [] if usr['room'] == room])

@socketio.on('leave', namespace = '/chat')
def leaveRoom():
    for room in rooms(request.sid): 
        if room != str(request.sid):
            leave_room(room)
            emit('user left', onlineUsers[str(request.sid)]['username'], room = room)

@socketio.on('disconnect', namespace='/chat')
def handleDisconnected():
    for room in rooms(request.sid): 
        if room != str(request.sid):
            emit('user left', onlineUsers[str(request.sid)]['username'], room = room)
            leave_room(room)
            onlineUsers[str(request.sid)]['state'] = 'Offline'
    print(onlineUsers)

@socketio.on('history', namespace='/chat')
def sendHistory():
    emit('history', getMessageHistory(onlineUsers[str(request.sid)]['username']))

@socketio.on('message', namespace='/chat')
def handleMessage(data):
    print('Message: ' + data['message'])
    print(data)
    message = {}
    for room in rooms(request.sid): 
        if room != str(request.sid):
            print(rooms(request.sid))
            message = {
                'username' : onlineUsers[str(request.sid)]['username'], 
                'message'  : data['message'], 
                'timestamp': data['timestamp'],
                'room'     : room
            }
            messages.append(message)
            emit('message', { 
                'username' : onlineUsers[str(request.sid)]['username'],
                'message'  : data['message'], 
                'timestamp': int(data['timestamp']),
                'dest'     : data['dest']
            }, room = room)
    statement = 'INSERT INTO chat_messages VALUES (%s, %s, %s, %s)'
    values = (message['username'], message['message'], str(message['timestamp']), message['room']);
    dbCursor.execute(statement, values)
    messageDb.commit()

@socketio.on('focus', namespace='/chat')
def handleFocus(focus):
    if str(request.sid) in onlineUsers:
        print('Focus of ' + onlineUsers[str(request.sid)]['username'] + ' : ' + focus)
        for room in rooms(request.sid): 
            if room != str(request.sid):
                if focus == 'focussed':
                    emit('state update', {
                        'username': onlineUsers[str(request.sid)]['username'],
                        'state'   : 'online'
                    }, room = room)
                    onlineUsers[str(request.sid)]['state'] = 'online'
                elif focus == 'blurred':
                    emit('state update', { 
                        'username': onlineUsers[str(request.sid)]['username'],
                        'state'   : 'idle'
                    }, room = room)
                    onlineUsers[str(request.sid)]['state'] = 'idle'

if __name__ == "__main__":
    eventlet.wsgi.server(eventlet.listen(('', 5000)), app)
