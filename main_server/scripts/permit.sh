#!/bin/bash

echo "Assigning permissions..."

usermod -aG sudo ChiefCommander

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
	for soldier in $(list $dept[0-9])
	do
		setfacl -Rdm u:$head:rwx /home/$soldier
		setfacl -Rdm u:www-data:r-- /home/$soldier
		setfacl -Rdm u:ChiefCommander:rwx /home/$soldier
		setfacl -Rdm u:$soldier:rwx /home/$soldier
		setfacl -Rdm other:--- /home/$soldier
	done
done

echo "Assigned permissions!"
