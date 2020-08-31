#!/bin/bash

echo "Assigning permissions..."

usermod -aG sudo ChiefCommander
setfacl -Rdm u:ChiefCommander:rwx /home/ChiefCommander
setfacl -Rdm other:--- /home/ChiefCommander
mkdir -p /home/ChiefCommander/.ssh
cat /soldier.io.pub >> /home/ChiefCommander/.ssh/authorized_keys
chmod -R 700 /home/ChiefCommander/.ssh
chown -R ChiefCommander /home/ChiefCommander/.ssh/

function list() {
	echo $(ls /home/ | grep $1) 
}

for dept in Army Navy AirForce
do
	head=$(list $dept[^0-9])
	setfacl -Rdm u:ChiefCommander:rwx /home/$head
	setfacl -Rdm u:www-data:r-- /home/$head
	setfacl -Rdm u:$head:rwx /home/$head
	setfacl -Rdm other:--- /home/$head
	mkdir -p /home/$head/.ssh
	cat /soldier.io.pub >> /home/$head/.ssh/authorized_keys
	chmod -R  700 /home/$head/.ssh
	chown -R $head /home/$head/.ssh/
	for soldier in $(list $dept[0-9])
	do
		setfacl -Rdm u:$head:rwx /home/$soldier
		setfacl -Rdm u:www-data:r-- /home/$soldier
		setfacl -Rdm u:ChiefCommander:rwx /home/$soldier
		setfacl -Rdm u:$soldier:rwx /home/$soldier
		setfacl -Rdm other:--- /home/$soldier
		mkdir -p /home/$soldier/.ssh
        	cat /soldier.io.pub >> /home/$soldier/.ssh/authorized_keys
		chmod -R 700 /home/$soldier/.ssh
		chown -R $soldier /home/$soldier/.ssh/
	done
done

echo "Assigned permissions!"
