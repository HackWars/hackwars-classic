@echo off

cd %cd%

mkdir dbtmp
cd dbtmp

"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysqldump" -u root --databases --add-drop-database alex_chat > chat.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysqldump" -u root --databases --add-drop-database hackerforum > hackerforum.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysqldump" -u root --databases --add-drop-database hackwars > hackwars.sql
"%PROGRAMFILES%\MySQL\MySQL Server 5.1\bin\mysqldump" -u root --databases --add-drop-database hackwars_drupal > hackwars_drupal.sql

cd ..

echo Created files in /dbtmp
pause