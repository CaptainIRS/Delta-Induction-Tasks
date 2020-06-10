# Delta SysAdmin Task 2
## Install Instructions
* Clone the repository
* cd into the repository
* Make sure that no other service is running at port 80
* Run `chmod +x config.sh login.sh`
* Run `sudo ./config.sh`
* Run `docker-compose build`
* Run `docker-compose up`

* Visit soldier.io in the host browser
* To SSH as any user, run `./login <user_name>`

*To view the Chief Commander portal, your Browser User Agent must contain "ChiefCommander"*

## Screenshots
|Tasks 1 and 2|||
|:----------------------------------------:|:-----------------------------------------:|:-----------------------------------------: |
| ![Imgur](https://i.imgur.com/z6J9iEc.png) | ![Imgur](https://i.imgur.com/AnnCJWj.png) | ![Imgur](https://i.imgur.com/R7IbVjd.png) |
| ![Imgur](https://i.imgur.com/31jfouq.png) | ![Imgur](https://i.imgur.com/ZrY8GAe.png) | ![Imgur](https://i.imgur.com/AxcguNQ.png) |


|Task 3||
|:----------------------------------------:|:-----------------------------------------:|
| ![Imgur](https://i.imgur.com/rdYUS1B.png) | ![Imgur](https://i.imgur.com/YobvLTz.png) |
| ![Imgur](https://i.imgur.com/K69rqHI.png) | ![Imgur](https://i.imgur.com/ma4ybll.png) |

# Task Descriptions:

# Task 1 - pan1c @ the c0mmandL1n3
## Problem Statement
Welcome, Sysad, you have been selected for working on the systems for the Indian defence forces. As a system administrator, you need to complete the basic tasks allotted to you. You will be having a mentor throughout your journey. Get on board, cadet.

Write a bash script for each of the following tasks and use aliases (the alias mentioned with each sub-task) to run each script.

## Main Task
* Create accounts and home directories for the following users in the given formats (alias - userGenerate):
  * ChiefCommander
  * ArmyGeneral, NavyMarshal and AirForceChief
  * 50 soldiers under each category named in the format: Army1 to Army50, Navy1 to Navy50, and Airforce1 to AirForce50.
* Assign them permissions as follows (alias - permit):

  * The chief commander can access and modify any directory.
  * The troop chiefs can access and modify any directory of their own troops and their own directory as well.
  * The soldiers can only access and modify their own directories.
  * Troop chiefs should not be able to access or modify any other troop chief's, any other troop soldier’s, or the chief commander's directory.
* You have been given a log file with time-stamps, username and the duty positions allotted to each soldier (position.log). Write a script for the following sub tasks (alias - autoSchedule):

  * Create a text file named post_allotted in each soldier's home directory.
  * Each file should have a table with 2 columns: Date and Post.
  * Every day at 12 am there should be a scheduled update in each soldier’s table with their respective post for the next day.
* Every day each soldier has to report at the post allotted to them. You are given an attendance log of whether the soldier was present. Write a script to automate the given sub tasks (alias - attendance):

  * Create a text file in every troop chief’s home directory named attendance_record.
  * This file should have a table with soldiers who were present at the posts allotted to them.
  * This file should automatically update every morning at 6 am. You have been given another log file with the attendance record. (attendance.log)
* Develop a script for each troop chief to fetch the attendance records of a day in a week (alias – record):

  * For example, if the troop chief types "record 4" the script will show the attendance records of the 4th day of the previous week, that is Thursday. Set up the script to handle the variable accordingly.
## Hacker Mode
The Chief Commander, who is in Delhi (28.7041° N, 77.1025° E), has to submit a report to the Ministry of defence every day. Write a script to automate the following tasks.

* (alias - finalattendance):

  * Create a file named attendance_report in the Chief Commander’s home directory with a table having each troop's attendance records along with the corresponding dates.
* (alias – nearest):

  * Create a file named nearest10 in the Chief Commander’s home directory.
  * The file should have a table containing the usernames of 10 army soldiers who had their allotted posts nearest to Delhi from the total list of those who were present, as well as the dates when they were posted there. Develop a suitable algorithm for it.

# Task 2 - The Right Redirection

Congratulations on setting up the system for the Indian Defence Forces! Our job does not end there, though.

## Main Task
### Sub task 1
The Chief has asked us to host two static websites:

* Set up Apache in the server. Create two web pages as follows and host them under the same URL soldier.io in your local server.
  * Page 1 which contains a soldier's profile at the path /username/index.html
  * Page 2 which displays the attendance records of the troops.
* The chief has his own unique browser with a unique USER_AGENT string. We have to make sure that when the Chief tries to access soldier.io from his browser, he will be shown page 2.
* If anyone else tries to access the URL, they should be shown page 1 with the details of the user. This should be done by serving each user's page from a different directory.

### Sub task 2
The army is facing a unique issue. There has been a change in the systems and hence we need to shift everything to the newer systems. As a result of this, the scripts that were written before to set up the user system are no longer supported. However, rewriting the scripts is a waste of time and effort and there is a high chance the systems can change again in the future . To solve the issue the Chief wants them to be dockerized to remove this barrier that arises due to using a different OS.

* Create a Dockerfile to handle all the scripts
* Using Ubuntu as the base image, copy all the scripts to the appropriate locations and run them accordingly
* Execute the alias.sh script inside the container

The result of this should be that of the first task, and the only difference would be that everything, including the aliases and crontasks, are inside the container.

### Hacker Mode
Everything has been working smoothly, but the Chief came to know about bad hygiene among soldiers at some locations. He got many complaints but was unable to handle all of them at once. He wants you to create a simple web page where soldiers can file complaints.

* Create a database having a table with the following columns: id, username, complaint and time when complaint was filed.
  * Create a .sql file which will be used to create the required table in the database.
* Soldiers should be able to register their complaints regarding the problems they face.
  * Create a simple web page which may send the complaints to the database along with the username of the person who filed them.
  * It should work on the simple principle of HTTP requests.
* The application is to be used by the Chief, so it needs to be shifted to the Chief's system without any difficulties.
  * Write a dockerfile for the application, which will be handling complaints, and run a container for the application.
  * Map the port 80 of the application container to port 3333 outside the container.
  * Also, write a script for the Chief to reverse proxy the URL soldier.io to this port so that whenever any soldier visits this URL, he should get redirected to the complaints page.
  * You need to modify the apache.conf file in order to set up the reverse proxy.
  * You may write automated scripts for the tasks if possible.

#### Notes:
You may use any language to create the static site. The preferred options are:
* PHP
* Python with Flask

### Resources:
1. https://www.digitalocean.com/community/tutorials/how-to-rewrite-urls-with-mod_rewrite-for-apache-on-ubuntu-16-04 
2. https://docs.docker.com/get-started/ 
3. https://docs.docker.com/compose/ 
4. https://www.tutorialspoint.com/docker/docker_compose.htm
5. https://www.youtube.com/playlist?list=PLhW3qG5bs-L99pQsZ74f-LC-tOEsBp2rK 
6. https://www.youtube.com/playlist?list=PL_jn5jikluSKecZf55QbKmnN195HL-o78

### Submissions
* Upload the task on GitHub and submit it to your mentors.
* Normal mode is compulsory, hacker mode is highly recommended.

# Task 3 - The Stringed Communication

The defence ministry is really happy with your work so far. The Chief Commander wants to offer you a permanent position among the highly talented system administrator team. Unfortunately, due to the sudden spread of COVID-19, the troops are facing many difficulties. Chief trusts you and believes that you can help come up with a solution.

## Main Task
The Ministry of defence wants to develop a chatting application to maintain proper communication between all the defence officials and soldiers.

The chat system should consist of the following features.

* A server and client model, where each soldier and official acts as a client.
* The system should follow a group based communication protocol, where a soldier from one troop can only see messages from soldiers from the same troop.
* A troop leader can send messages to everyone in his own troop, as well as see messages from other troop leaders.
* The chief commander can see all messages but he should only be able to communicate directly with the troop leaders.
* The whole model will be based on groups, so you will be required to create groups to maintain the communication protocols.
* Dockerize the CLI.

## Hacker Mode
* Each client should be able to keep a record of his chat history over the past one week, and he would be able to download it in the form of a text file whenever he wants.
* If any of the client is offline, the server should store messages along with the username of the recipient and send the messages to the client as soon as he is online, rather than giving an error message.
* If a client receives a message, he should receive it with the name of the sender along with it. If the client sends a message, his own name should be printed along with the message on his own screen as given below.
Army1: Hello guys, how are you doing? Army2: I am fine, what about you? Army3 (me): I am also fine.
* To implement this feature, you should learn how to manage system arguments and buffer storage.
* Finally, dockerize the whole application along with the client side database. You should learn Docker Compose for this.

### Note
You need to know the basics of python. Learn about topics like sockets and Multithreading for the task.

### Submission
Dealine - 10th june 2020 Upload the final scripts on github and submit the github link.

### Sources
1. https://docs.python.org/3/library/socket.html
2. https://realpython.com/python-sockets/
3. https://www.tutorialspoint.com/python/python_multithreading.htm
