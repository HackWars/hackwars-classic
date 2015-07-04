#! /bin/bash

# 
#  db-repopulate.sh
#  HackWars
#  
#  Created by Dana Rea on 2009-01-17.
#  Copyright 2009 Dana Rea. All rights reserved.
# 
USER=root
HOST=localhost

mysql -u $USER -p -h $HOST hackwars < hackwars_truncatedata.sql

mysql -u $USER -p -h $HOST hackwars < hackwars_data.sql