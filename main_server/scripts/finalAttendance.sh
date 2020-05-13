#!/bin/bash

for dept in Army Navy AirForce
do
	echo "Setting final attendance..." 
	head=$(ls /home/ | grep $dept[^0-9])
	cat /home/$head/attendance_record >> /home/ChiefCommander/attendance_record
	cat /home/ChiefCommander/attendance_record | sort >> /home/ChiefCommander/tmp
	rm /home/ChiefCommander/attendance_record
	mv /home/ChiefCommander/tmp /home/ChiefCommander/attendance_record
	echo "Final attendance set!"
done
