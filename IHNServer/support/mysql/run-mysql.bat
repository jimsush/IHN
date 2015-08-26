
title Mysql5.0.20a for OpView V2.0 Console

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
pushd %DIRNAME%

REM start mysql using configure file
set MYSQL_HOME=.
set MYSQL_CONF_FILE=ihn.ini

REM if you want to start mysql from command and without config file,
REM use following command, BTW, please delete the inno db log file at first.
rem %MYSQL_HOME%\bin\mysqld --verbose --default-character-set=utf8 --default-storage-engine=INNODB

%MYSQL_HOME%\bin\mysqld --defaults-file="%MYSQL_CONF_FILE%" --verbose --default-storage-engine=INNODB
