#!/bin/bash

STARTED="FIRST_RUN"
if [ ! -e $STARTED ]; then
	touch $STARTED
	echo "Initialising Container..."
	mkdir -p /var/www/logs
	/etc/init.d/apache2 start
	a2enmod rewrite
	a2enmod proxy
	a2enmod proxy_http
	a2ensite anything.com
fi

echo "127.0.0.1		anything.com" >> /etc/hosts

/etc/init.d/apache2 start
/etc/init.d/apache2 restart
apache2 -DFOREGROUND -v
/bin/bash
