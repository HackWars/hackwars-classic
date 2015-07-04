@echo off

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes\chatServer"

javac -classpath "%cd%" Assignments/*.java
javac -classpath "%cd%" chat/messages/*.java
javac -classpath "%cd%" chat/server/*.java
javac -classpath "%cd%" chat/util/*.java
javac -classpath "%cd%" Server/*.java
javac -classpath "%cd%" util/*.java

pause