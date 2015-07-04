#! /bin/bash

SQL_PATH=/usr/local/mysql-5.1.36-osx10.4-i686/bin/mysql

$SQL_PATH -u root --max_allowed_packet=128M < hackwars_schema.sql
$SQL_PATH -u root --max_allowed_packet=128M hackwars < hackwars_data.sql
$SQL_PATH -u root --max_allowed_packet=128M < hackerforum_schema.sql
$SQL_PATH -u root --max_allowed_packet=128M hackerforum < hackerforum_data.sql
$SQL_PATH -u root < chat.sql

