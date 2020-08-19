#!/bin/bash

col=$(echo $1 | cut -d: -f1 | awk '{$1=$1};1')
filter=$(echo $1 | cut -d: -f2 | awk '{$1=$1};1')

i=$(
	awk -v col=$col '
		BEGIN{ 
			FS=","
		}
		NR==1{
			for(i=1;i<=NF;i++){
				if ($i ~ col) {print i}
			}
		}
	' merged.txt
)

awk -v i=$i -v filter=$filter '
	BEGIN{
		FS=","
	}{
		if($i ~ filter) {print $0}
	}
' merged.txt
