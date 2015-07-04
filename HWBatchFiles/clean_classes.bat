@echo off
rem Use this to clean out old class files from the classes directory
rem The classes shouldn't end up in the classes directory (ok very poor
rem naming) if you use the ant build script (build.xml). But if you do 
rem run javac by mistake, this will clean up.
rem Note: do not run this on the net/org/edu etc dirs, these are static
rem and never compiled

cd ../apache-tomcat-6.0.14/webapps/ROOT/WEB-INF/classes
del /F /S /Q Applet\*.class
del /F /S /Q Assignements\*.class
del /F /S /Q Browser\*.class
del /F /S /Q GUI\*.class
del /F /S /Q Game\*.class
del /F /S /Q HackerLogin\*.class
del /F /S /Q HackerSearch\*.class
del /F /S /Q Hackscript\*.class
del /F /S /Q Hacktendo\*.class
del /F /S /Q Server\*.class
del /F /S /Q View\*.class
del /F /S /Q chat\*.class
rem del /F /S /Q \*.class

echo.
echo done :)
echo.
pause

