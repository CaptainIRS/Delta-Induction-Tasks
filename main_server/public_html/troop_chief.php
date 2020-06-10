<?php 
   $user = $_GET['user'];
   ?>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
      <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/2.3.0/socket.io.js"></script>
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
      <link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
      <script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/showdown@1.9.1/dist/showdown.min.js"></script>
      <title>Troop Chief Portal</title>
   </head>
   <style> 
         html,body {
           height: 100%;
         } 
         .list-group{
           height: 300px;
           overflow:scroll;
           -webkit-overflow-scrolling: touch;
         }
         .CodeMirror{
	   min-height: 50px;
	   max-height: 100px;
           width: 86%;
           padding-right: 0px !important;
         }
         .CodeMirror-scroll {
	   min-height: 50px;
	   max-height: 100px;
           width: 100%;
         }
         .list-group-item > .message {
           display: inline-block !important;
           vertical-align: top;
           margin-bottom: 0px !important;
           max-width: 80% !important;
           padding-left: 5px !important;
         }
         .message > p {
           margin-bottom: 0px !important;
         }
         .list-group-item > .badge {
           vertical-align: middle;
           min-width: 15% !important;
         }
         .list-group-item > .timestamp {
           font-size: 11px;
           color: gray;
           vertical-align: bottom;
           float: right;
         }
   </style>
   <body>
      <nav class="navbar navbar-expand-md navbar-dark bg-dark">
         <div class="container-fluid">
            <a href="#" class="navbar-brand mr-3">Troop Chief Portal</a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
               <div class="navbar-nav ml-auto">
                  <li class="nav-item">
                     <button class="btn btn-success navbar-btn mr-3" data-toggle="modal" data-target="#chatModal">
                       Chat 
                       <i class="material-icons align-middle ml-3">chat</i> 
                     </button>
                  </li>
                  <li class="nav-item">
                     <button class="btn btn-danger navbar-btn" data-toggle="modal" data-target="#complaintModal">File Complaint</button>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link" href="#">Welcome, <?php echo $user; ?></a>
                  </li>
               </div>
            </div>
         </div>
      </nav>
      <div class="modal" id="complaintModal">
         <div class="modal-dialog">
            <div class="modal-content">
               <form action="" id="complaintForm">
                  <div class="modal-header">
                     <h4 class="modal-title">Complaint Form</h4>
                     <button type="button" class="close" data-dismiss="modal">&times;</button>
                  </div>
                  <div class="modal-body">
                     <div class="form-group">
                        <label for="complaint">Enter Complaint:</label>
                        <textarea class="form-control" rows="5" id="complaint"></textarea>
                     </div>
                     <div id="successAlert" class="alert alert-success alert-dismissible fade collapse" role="alert">
                        <strong>Success!</strong> Your complaint has been submitted!
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                     </div>
                     <div id="errorAlert" class="alert alert-danger alert-dismissible fade collapse" role="alert">
                        <strong>Error!</strong> Your complaint couldn't be submitted
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                     </div>
                  </div>
                  <div class="modal-footer">
                     <button type="submit" class="btn btn-primary">Submit</button>
                     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                  </div>
               </form>
            </div>
         </div>
      </div>
      <div class="modal w-auto" id="chatModal">
         <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                   <h4 class="modal-title">Chat</h4>
                   <button type="button" class="btn btn-outline-dark ml-3" onclick="history()"><i class="material-icons align-middle">history</i></button>
                   <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                  <ul class="nav nav-tabs">
                    <li class="nav-item">
                      <a id="groupChatTab" href="#" onclick="groupChat()" class="nav-link active">Group Chat</a>
                    </li>
                    <li class="nav-item dropdown">
                      <a id="privateChatTab" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Private Chat</a>
                      <div id="usersMenu" class="dropdown-menu">
                        <div class="dropdown-divider"></div>
                        <a id="openUserModal" class="dropdown-item" data-toggle="modal" data-target="#addUserModal"><i class="material-icons align-middle mr-2">add</i>New Private Chat</a>
                      </div>
                    </li>
                  </ul>
                  <div class="card" style="width: auto">
                    <div class="card-header w-100 p-1">
                      <div id="onlineUsers"></div>
                    </div>
                    <ul id="messageList" class="list-group list-group-flush">

                    </ul>
                    <div class="card-footer w-100 p-0">
                      <div class="input-group m-2">

                        <textarea class="form-control" placeholder="Type message here..." id="messageMd"></textarea>
                        
                        <div class="input-group-append">
                          <button id="sendButton" class="btn btn-outline-success" type="button"><i class="material-icons align-middle">send</i></button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
            </div>
         </div>
      </div>
      <div id="addUserModal" class="modal fade bd-example-modal-sm" tabindex="-1" style="z-index:5000" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-sm modal-dialog-centered p-2">
           <div class="modal-content">
              <div class="modal-header">
                 <h4 class="modal-title">Add User</h4>
                 <button type="button" class="close" data-dismiss="modal">&times;</button>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="at">@</span>
                </div>
                <input id="username" type="text" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="at">
                <div class="input-group-append">
                  <button id="addUser" class="btn btn-outline-secondary" type="button">Chat</button>
                </div>
              </div>
           </div>
        </div>
      </div>
      <div class="container-fluid d-flex flex-column" style="height: calc(100% - 56px);">
         <div class="row flex-fill" style="min-height:0">
            <div class="col mh-100 p-3">
               <div class="card  mh-100">
                  <div class="card-header">
                     Past & Future Attendance Records
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <div id="cards">
                           <?php
                              $fpath = "/home/" . $user . "/attendance_record";
                              $f = fopen($fpath, "r");
                              $record = array();
                              while (($data = fgetcsv($f)) !== false) {
                                if(!array_key_exists($data[0], $record)) $record[$data[0]] = array();
                                $temp_data = $record[$data[0]];
                                if(sizeof($temp_data)===0) $tempdata = array();
                                array_push($temp_data, htmlspecialchars($data[1]));
                                $record[$data[0]] = $temp_data;
                              }
                              foreach($record as $date_key => $present_array) {
                                $date = strtotime($date_key);
                                $date_str = date('d M Y', $date);
                                if(date('d M Y', $date) == date('d M Y', time())) { $t_date = $date; $t_present = $present_array; }
                                echo "<div class='card'> \n";
                                echo "  <div class='card-header' id='heading{$date_key}'>\n";
                                echo "    <h5 class='mb-0'>\n";
                                echo "      <button class='btn btn-link collapsed' data-toggle='collapse' data-target='#collapse{$date_key}' aria-expanded='false' aria-controls='collapse{$date_key}'>\n";
                                echo "        {$date_str}\n";
                                echo "      </button>\n";
                                echo "    </h5>\n";
                                echo "  </div>\n";
                                echo "  <div id='collapse{$date_key}' class='collapse' aria-labelledby='heading{$date_key}' data-parent='#cards'>\n";
                                echo "    <div class='card-body'>\n";
                                foreach($present_array as $id => $value) {
                                  echo "      <span class='badge badge-success'> {$value} </span>\n";
                                }
                                echo "    </div>\n";
                                echo "  </div>\n";
                                echo "</div>\n";
                              }
                              fclose($f);
                              ?>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
            <div class="col mh-100 p-3">
               <div class="card  h-100">
                  <div class="card-header">
                     Today's Record
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <h2 class="card-title"><?php echo date('d M Y', $t_date); ?></h2>
                        <h5 class="card-text">
                        <?php 
                           foreach($t_present as $id => $value) {
                             echo "<span class='badge badge-success'> {$value} </span>\n";
                           }
                        ?>
                        </h5>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <script type="text/javascript" charset="utf-8">
        function getRandomHue() {
          return Math.floor(Math.random() * 360);
        }
        var messageMd = new SimpleMDE({ element: document.getElementById("messageMd"), toolbar: false, status: false});
        var socket, hues = {}, dest, me = '<?php echo $user; ?>';

        $(document).ready(function () {

          var converter = new showdown.Converter({simplifiedAutoLink: 'true', strikethrough: 'true', tables: 'true'});

          socket = io.connect('/chat');

          socket.on('connect', function() {
            console.log("Connected");
            dest = 'group';
            socket.emit("connected", "<?php echo $user; ?>");
            if(document.hasFocus()) socket.emit("focus", "focussed");
            else socket.emit("focus", "blurred");
          });

          socket.on('message', function(msg) {
            console.log(msg);
            username = (msg.username == me) ? 'You' : msg.username;
            $('#messageList').append('<li class="list-group-item p-1">' + '<span class="badge mr-2" style="background-color: hsl(' + hues[msg.username] + ', 100%, 80%)">' + username + '</span>' + '<div class="message">' + converter.makeHtml(msg.message) + '</div>' + '<div class="timestamp mt-1"><i>' + (new Date(msg.timestamp)).toLocaleString("en-IN") + '</i></div>' + '</li>');
            $('#messageList').stop().animate ({
              scrollTop: $('#messageList')[0].scrollHeight
            });
          });

          socket.on('user joined', function(msg) {
            console.log(msg.username);
            username = (msg.username == me) ? 'You' : msg.username;
            $('#messageList').append('<li class="list-group-item p-1 text-center">' + '<b>' + username + '</b>' + ' joined the chat' +  '</li>');
            $('#messageList').stop().animate ({
              scrollTop: $('#messageList')[0].scrollHeight
            });
            hues[msg.username] = msg.hue;
            if(!$('#pill' + msg.username).length) {
              console.log("True");
              $('#onlineUsers').append('<span id="pill' + msg.username + '" class="badge badge-success m-1">' + username + '&nbsp;<i class="material-icons align-middle" style="font-size:13px">computer</i>' + '</span>');
            } else {
              console.log("False");
              $('#pill' + msg.username).addClass('badge-success');
              $('#pill' + msg.username).removeClass('badge-danger');
            }
          });

          socket.on('current users', function(data) {
            console.log(data);
            data.forEach((usr) => {
              username = (usr.username == me) ? 'You' : usr.username;
              hues[usr.username] = usr.hue;
              if(!$('#pill' + usr.username).length) {
                console.log("True");
                $('#onlineUsers').append('<span id="pill' + usr.username + '" class="badge badge-success m-1">' + username + '&nbsp;<i class="material-icons align-middle" style="font-size:13px">computer</i>' + '</span>');
              } else {
                console.log("False");
              }
              $('#pill' + usr.username).removeClass('badge-success');
              $('#pill' + usr.username).removeClass('badge-warning');
              $('#pill' + usr.username).removeClass('badge-danger');
              var addedflavor = (usr.state == 'online') ? 'success' : (usr.state == 'idle') ? 'warning' : 'danger';
              $('#pill' + usr.username).addClass('badge-' + addedflavor);
            });
          });

          socket.on('user left', function(username) {
            console.log(username);
            username = (username == me) ? 'You' : username;
            $('#messageList').append('<li class="list-group-item p-1 text-center">' + '<b>' + username + '</b>' + ' left the chat' +  '</li>');
            $('#messageList').stop().animate ({
              scrollTop: $('#messageList')[0].scrollHeight
            });
            $('#pill' + username).removeClass('badge-success');
            $('#pill' + username).removeClass('badge-warning');
            $('#pill' + username).addClass('badge-danger');
          });

          socket.on('state update', function(msg) {
            console.log(msg);
            username = (msg.username == me) ? 'You' : msg.username;
            if(!$('#pill' + msg.username).length) {
              $('#onlineUsers').append('<span id="pill' + msg.username + '" class="badge badge-success m-1">' + username + '&nbsp;<i class="material-icons align-middle" style="font-size:13px">computer</i>' + '</span>');
            } else {
              var addedflavor = (msg.state == 'online') ? 'success' : 'warning', removedflavor = (msg.state == 'idle') ? 'success' : 'warning';
              $('#pill' + msg.username).addClass('badge-' + addedflavor);
              $('#pill' + msg.username).removeClass('badge-' + removedflavor);
              $('#pill' + msg.username).removeClass('badge-danger');
            }
          });

          socket.on('room history', function(messages) {
            messages.forEach((msg) => {
              username = (msg.username == me) ? 'You' : msg.username;
              $('#messageList').append('<li class="list-group-item p-1">' + '<span class="badge mr-2" style="background-color: lightgray">' + username + '</span>' + '<div class="message">' + converter.makeHtml(msg.message) + '</div>' + '<div class="timestamp"><i>' + (new Date(parseInt(msg.timestamp))).toLocaleString("en-IN") + '</i></div>' + '</li>');
              $('#messageList').stop().animate ({
                scrollTop: $('#messageList')[0].scrollHeight
              });
            });
          });

          socket.on('private chats', function(users) {
            users.forEach((usr) => {
              $('#usersMenu').html('<a class="dropdown-item" href="#" onclick="chat(\'' + usr + '\')">' + usr + '</a>' + $('#usersMenu').html());
            });
          });

          socket.on('history', function(data) {
            const a = document.createElement("a");
            a.href = URL.createObjectURL(new Blob([JSON.stringify(data, null, 2)], {
              type: "text/plain"
            }));
            a.setAttribute("download", "history.txt");
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
          });

          window.addEventListener('focus', function() {socket.emit("focus", "focussed")});
	  window.addEventListener('blur', function() {socket.emit("focus", "blurred")});

          $('#sendButton').click(function() {
            socket.emit('message', {message: messageMd.value(), timestamp: new Date().getTime(), dest: dest});
            messageMd.codemirror.getDoc().setValue('');
          });

          $('#addUser').click(function() {
             $('#addUserModal').modal('hide');
             usr = $('#username').val();
             $('#usersMenu').html('<a class="dropdown-item" onclick="chat(\'' + usr + '\')">' + usr + '</a>' + $('#usersMenu').html());
             chat(usr);
          });

	});
        function chat(usr) {
           socket.emit('leave');
           $('#onlineUsers').empty();
           $('#messageList').empty();
           dest = usr;
           socket.emit('join', {to: usr});
           $('#privateChatTab').text(usr);
           $('#privateChatTab').addClass('active');
           $('#groupChatTab').removeClass('active');
        }

        function groupChat() {
           socket.emit('leave');
           $('#onlineUsers').empty();
           $('#messageList').empty();
           dest = 'group';
           socket.emit('join', {to: 'group'});
           $('#privateChatTab').text('Private Chat');
           $('#privateChatTab').removeClass('active');
           $('#groupChatTab').addClass('active');
        }

        function history() {
          socket.emit('history');
        }
      </script>
      <script>
         $("#complaintForm").submit(function(event) {
         
           event.preventDefault();
         
           var complaint = $('#complaint').val();         
           $.ajax({
           type: "POST",
           url: "../complaint.php",
           data: { 
             user: "<?php echo $user; ?>",
             complaint: complaint,
           },
           success: function(data) {
             $('#successAlert').removeClass('collapse');
             $('#successAlert').addClass('show');
           },
           fail: function() {
             $('#errorAlert').removeClass('collapse');
             $('#errorAlert').addClass('show');
           }
           });
         });
         $('.alert').on('close.bs.alert', function (event) {
           event.preventDefault();
           $(this).addClass('collapse');
           $(this).removeClass('show');
         });
      </script>
   </body>
</html>


