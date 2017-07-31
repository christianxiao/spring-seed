#!/bin/bash
rootpasswd=root
dbName=spring_seed
mysql -uroot -p${rootpasswd} -e "create database if not exists ${dbName}  DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;"
mysql -uroot -p${rootpasswd} -e "CREATE USER 'ddl'@'%' IDENTIFIED BY 'ddl';"
mysql -uroot -p${rootpasswd} -e "GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER ON ${dbName}.* TO 'ddl'@'%';"
mysql -uroot -p${rootpasswd} -e "CREATE USER 'dml'@'%' IDENTIFIED BY 'dml';"
mysql -uroot -p${rootpasswd} -e "GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.* TO 'dml'@'%';"

mysql -uroot -p${rootpasswd} -e "FLUSH PRIVILEGES;"
