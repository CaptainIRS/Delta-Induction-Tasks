#!/bin/bash
echo 'alias userGenerate="/scripts/userGenerate.sh"' >> /etc/skel/.bashrc
echo 'alias permit="/scripts/permit.sh"' >> /etc/bash.bashrc
echo 'alias attendance="/scripts/attendance.sh"' >> /etc/skel/.bashrc
echo 'alias autoSchedule="/scripts/autoSchedule.sh"' >> /etc/skel/.bashrc
echo 'alias finalAttendance="/scripts/finalAttendance.sh"' >> /etc/skel/.bashrc
echo 'alias nearest="/scripts/nearest10.sh"' >> /etc/skel/.bashrc
echo 'alias record="/scripts/record.sh"' >> /etc/skel/.bashrc

