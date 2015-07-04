@echo off

cd "%cd%\..\apache-tomcat-6.0.14\webapps\ROOT\WEB-INF\classes"

javac -classpath "%cd%" Applet/*.java
javac -classpath "%cd%" GUI/*.java
javac -classpath "%cd%" View/*.java
javac -classpath "%cd%" Assignments/*.java
javac -classpath "%cd%" Browser/*.java
javac -classpath "%cd%" Hacktendo/*.java
javac -classpath "%cd%" Hacktendo/Functions/*.java
javac -classpath "%cd%" Game/*.java