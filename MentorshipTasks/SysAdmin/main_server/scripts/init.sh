#!/bin/bash

STARTED="FIRST_RUN"
if [ ! -e $STARTED ]; then
	touch $STARTED
	echo "Initialising Container..."

	#Configure executable files

	chmod +x attendance.sh
	chmod +x autoSchedule.sh
	chmod +x finalAttendance.sh
	chmod +x nearest10.sh
	chmod +x permit.sh
	chmod +x record.sh
	chmod +x srcedit.sh
	chmod +x userGenerate.sh
	./srcedit.sh
	./userGenerate.sh
	./permit.sh
	./autoSchedule.sh position.log
	./attendance.sh attendance.log
        ./finalAttendance.sh
        ./nearest10.sh

	#Configure server

	mkdir -p /var/www/logs
	/etc/init.d/apache2 start
	a2enmod rewrite
	a2enmod proxy
	a2enmod proxy_http
	a2enmod proxy_wstunnel
	a2ensite soldier.io

	#Configure cron-jobs

	touch /etc/cron.d/position
	touch /etc/cron.d/attendance
	echo "0 0 * * * /scripts/autoSchedule.sh /position.log" >> /etc/cron.d/position
	echo "0 6 * * * /scripts/attendance.sh /attendance.log" >> /etc/cron.d/attendance
	
fi

echo "127.0.0.1		soldier.io" >> /etc/hosts
/usr/sbin/sshd
service ssh restart
/etc/init.d/apache2 start
/etc/init.d/apache2 restart
apache2 -DFOREGROUND -v
/bin/bash
