FROM php:7.4-apache

ENV DEBIAN_FRONTEND=noninteractive 

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install sudo -y

RUN docker-php-ext-install pdo pdo_mysql mysqli

ADD scripts /scripts/
ADD anything.com.conf /etc/apache2/sites-available/anything.com.conf

WORKDIR /scripts/

EXPOSE 80

RUN chmod +x init.sh

ENTRYPOINT /scripts/init.sh
