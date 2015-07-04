@echo off

cd %cd%

unzip db.zip -d %windir%\..\hwdbs

rem "%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\hackwars_schema.sql
rem "%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root hackwars < %windir%\..\hwdbs\hackwars_data.sql
rem "%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\hackerforum_schema.sql
rem "%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root hackerforum < %windir%\..\hwdbs\hackerforum_data.sql

"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\hackwars.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\hackerforum.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\chat.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysql" -u root < %windir%\..\hwdbs\hackwars_drupal.sql

del /F /Q %windir%\..\hwdbs\*.*
rmdir %windir%\..\hwdbs