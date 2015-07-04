@echo off

title Client

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes"

echo Enter your username.
set /p Username=

echo Enter your password.
set /p Password=

echo Enter your ingame IP.
set /p IP=

cls

java -classpath "%cd%" View/View localhost %Username% %Password% %IP%
rem java -classpath "%cd%" View/View hackstockhw.co.cc %Username% %Password% %IP%
rem java -classpath "%cd%" View/View hackwars.net %Username% %Password% %IP%