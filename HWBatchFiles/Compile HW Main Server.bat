@echo off

rem please note, this file is still growing...

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes"

javac -classpath "%cd%" Assignments/*.java
javac -classpath "%cd%" Game/*.java
javac -classpath "%cd%" Server/*.java

pause