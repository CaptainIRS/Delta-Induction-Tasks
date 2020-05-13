# Delta SysAdmin Task 2
## Install Instructions
* Clone the repository
* cd into the repository
* Make sure that no other service is running at port 80
* Run `chmod +x config.sh`
* Run `sudo ./config.sh`
* Run `docker-compose build`
* Run `docker-compose up -d`
* Configuration may take upto 5 minutes or more
* Visit soldier.io in the host browser

## Screenshots
||||
|:----------------------------------------:|:-----------------------------------------:|:-----------------------------------------: |
| ![Imgur](https://i.imgur.com/z6J9iEc.png) | ![Imgur](https://i.imgur.com/AnnCJWj.png) | ![Imgur](https://i.imgur.com/R7IbVjd.png) |
| ![Imgur](https://i.imgur.com/31jfouq.png) | ![Imgur](https://i.imgur.com/ZrY8GAe.png) | ![Imgur](https://i.imgur.com/AxcguNQ.png) |

# Task Description

The Right Redirection

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
* https://www.digitalocean.com/community/tutorials/how-to-rewrite-urls-with-mod_rewrite-for-apache-on-ubuntu-16-04 
* https://docs.docker.com/get-started/ 
* https://docs.docker.com/compose/ 
* https://www.tutorialspoint.com/docker/docker_compose.htm

* https://www.youtube.com/playlist?list=PLhW3qG5bs-L99pQsZ74f-LC-tOEsBp2rK 
* https://www.youtube.com/playlist?list=PL_jn5jikluSKecZf55QbKmnN195HL-o78

### Submissions
* Upload the task on GitHub and submit it to your mentors.
* Normal mode is compulsory, hacker mode is highly recommended.
