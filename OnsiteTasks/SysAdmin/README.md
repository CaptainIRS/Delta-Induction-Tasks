# Task Descriptions

## Task 1
You have to create 3 separate text files (any format) in which data is represented in the form of a table.
The first file will have Name, Email, and Roll Number as columns.
Second file will have Roll Number, Phone Number as columns
Third file will have Email, Phone Number and their Expected Graduation Year as columns.

Your task is to merge all the three files into one, avoiding duplicates i.e. the merged file will consist of the columns: Name, Email, Roll Number, Phone Number and Expected Graduation Year. 

Also ensure that all the entries in the three files match with the final one. Finally, you have to write a small search script that returns rows in which the fields that you have specified start with the value you give as input. For example, say that you have a column called “roll” which indicates Roll Number. If “roll: 1101” is given as input to your script, all details of rows in which the Roll Number starts with 1101 should be printed.

Note: There is no need to create boxes for tables, fields can be separated by spaces and rows by new lines.

## Task 2
Your task is to create a simple login page and change its domain to “anything.com” for your local machine using apache rewrite rules. Visiting anything.com should redirect you to the login page. Also, using tcpdump, write a command to capture the password and login ID of the user after every use.

Bonus: Include Remember Me and Forgot Password features in your login page.

## Task 3
AES is a very popular symmetric encryption algorithm widely used in secure file transfer protocols like FTPS and HTTPS.

Your task is to create an AES encryptor and decryptor. You can use any language you want to write the scripts, but can’t use any prebuilt libraries. You need to create your own functions using the core algorithm.

## Task 4
Write a brute force script to find the password of a user named "whiteHack". Their password pattern begins with "wIhEXtAhKcXY" where X,Y are single digit integers. Also parallelize the process (use multi threading)
