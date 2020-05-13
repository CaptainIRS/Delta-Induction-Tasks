#!/bin/bash

sub=$((7 - $1 + $(date +%w)))
grep $(date -d "$sub day ago" +'%Y-%m-%d') $HOME/attendance_record
