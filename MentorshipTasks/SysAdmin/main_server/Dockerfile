FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive 

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install acl sudo apache2 php libapache2-mod-php bc cron openssh-server python3 python3-pip --fix-missing -y

ADD scripts /scripts/
ADD soldier.io.conf /etc/apache2/sites-available/soldier.io.conf
ADD sshd_config /etc/ssh/sshd_config
ADD soldier.io.pub /soldier.io.pub

RUN mkdir /var/run/sshd
RUN chmod 0755 /var/run/sshd

WORKDIR /scripts/

RUN pip3 install python-socketio

EXPOSE 80
EXPOSE 22

RUN chmod +x init.sh
