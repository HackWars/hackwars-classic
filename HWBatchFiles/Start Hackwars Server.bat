@echo off

title Server

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes"

java -classpath "%cd%" Server/HackerServer 1