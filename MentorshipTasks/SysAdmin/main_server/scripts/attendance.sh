#!/bin/bash

function mark_attendance() {
	dept=$(echo "$2" | grep -o ".*[^0-9]")
	head=$(ls /home/ | grep $dept[^0-9])
	if [[ $3 = "YES" ]]
	then
		echo ${1:1:10}, $2 >> /home/$head/attendance_record
	fi
}

echo "Marking Attendance..."

export -f mark_attendance
xargs -n 3 -a $1 bash -c 'mark_attendance "$@"' _

echo "Attendance marked!"
