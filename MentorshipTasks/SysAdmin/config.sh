#!/bin/bash

echo "127.0.0.1 soldier.io" >> /etc/hosts
ssh-keygen -t rsa -N '' -f main_server/soldier.io <<< y
mv main_server/soldier.io ~/.ssh/soldier.io
cp main_server/soldier.io.pub ~/.ssh/soldier.io.pub
