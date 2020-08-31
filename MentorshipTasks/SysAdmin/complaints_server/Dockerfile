FROM php:7.3-apache

RUN apt-get update -y && apt-get upgrade -y
RUN docker-php-ext-install pdo pdo_mysql mysqli

EXPOSE 80
