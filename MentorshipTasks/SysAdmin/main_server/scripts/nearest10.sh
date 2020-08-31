#!/bin/bash

function print_details() {
	while read -r line; do
		echo $2 ${line:1:10} >> /home/ChiefCommander/nearest10
	done < <(cat /home/$2/post_allotted | grep $3 | grep $4)
}

function find_distance() {
	post="$(cat /home/$2/post_allotted | grep ${1:0:10})"
	lat=${post:25:8}
	lon=${post:38:8}
	lat_trans=$(bc <<< "scale=5; $lat - 28.7041")
	lon_trans=$(bc <<< "scale=5; $lon - 77.1025")
	dist=$(bc <<<"scale=5; sqrt($lat_trans*$lat_trans + $lon_trans*$lon_trans)" | awk '{printf "%09.5f\n", $0}')
	echo $dist $2 $lat $lon >> /home/ChiefCommander/tmp	
}

echo "Finding nearest 10..."

export -f find_distance
export -f print_details

cur_date=$(date +'%Y-%m-%d')
grep "$cur_date, Army" /home/ChiefCommander/attendance_record | xargs -n 2 bash -c 'find_distance "$@"' _

cat /home/ChiefCommander/tmp | sort | head -10 | xargs -n 4 bash -c 'print_details "$@"' _

rm /home/ChiefCommander/tmp

echo "Found nearest 10!"
