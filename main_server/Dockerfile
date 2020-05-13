FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive 

RUN apt-get update -y
RUN apt-get install acl sudo apache2 php libapache2-mod-php bc cron -y

ADD scripts /scripts/
ADD soldier.io.conf /etc/apache2/sites-available/soldier.io.conf

WORKDIR /scripts/

EXPOSE 80

RUN chmod +x init.sh
