#! /bin/bash

mysqldump -u root --no-data --add_drop_database --databases hackwars > hackwars_schema.sql
mysqldump -u root --no-data --add_drop_database --databases hackerforum > hackerforum_schema.sql
