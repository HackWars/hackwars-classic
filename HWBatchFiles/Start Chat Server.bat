@echo off

title Chat Server

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes\chatServer"

java -classpath "%cd%" Server/ChatServer

pause