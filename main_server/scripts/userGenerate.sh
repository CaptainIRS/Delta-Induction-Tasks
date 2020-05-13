#!/bin/bash

echo "Generating users..."

useradd -m ChiefCommander

useradd -m ArmyGeneral
useradd -m NavyMarshal
useradd -m AirForceChief

for i in {1..50} 
do
	useradd -m Army$i
	useradd -m Navy$i
	useradd -m AirForce$i
done

echo "Users generated!"
