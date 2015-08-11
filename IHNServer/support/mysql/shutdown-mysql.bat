@echo off

title Shutdown Mysql5.0.20a

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
pushd %DIRNAME%

REM Stopping Mysql Server
@echo Stopping Mysql Server

bin\mysqladmin -P 7788 --user=root shutdown
