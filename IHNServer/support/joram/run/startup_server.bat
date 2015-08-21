@echo off

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
pushd %DIRNAME%

cd joram\run
set JORAM_HOME=..

set CONFIG_DIR=%JORAM_HOME%\config
set JORAM_LIBS=%JORAM_HOME%\libs
set RUN_DIR=%JORAM_HOME%\run

REM  Building the Classpath
set CLASSPATH=%JORAM_LIBS%\joram-mom.jar
set CLASSPATH=%CLASSPATH%;%JORAM_LIBS%\joram-shared.jar
set CLASSPATH=%CLASSPATH%;%JORAM_LIBS%\JCup.jar
set CLASSPATH=%CLASSPATH%;%JORAM_LIBS%\jakarta-regexp-1.2.jar
set CLASSPATH=%CLASSPATH%;%JORAM_LIBS%\ow_monolog.jar
set CLASSPATH=%CLASSPATH%;%JORAM_LIBS%\jmxri.jar
set CLASSPATH=%CLASSPATH%;%RUN_DIR%

mkdir %RUN_DIR%
copy %CONFIG_DIR%\a3config.dtd %RUN_DIR%\a3config.dtd
copy %CONFIG_DIR%\a3debug.cfg %RUN_DIR%\a3debug.cfg
copy %CONFIG_DIR%\centralized_a3servers.xml %RUN_DIR%\a3servers.xml
copy %CONFIG_DIR%\jndi.properties %RUN_DIR%\jndi.properties

if not exist %JORAM_HOME%\..\jre goto setJava
echo use jre in ems
set JAVA=%JORAM_HOME%\..\jre\bin\java.exe
goto start

:setJava
echo use jre in javahome= %JAVA_HOME%
set JAVA=%JAVA_HOME%\bin\java.exe

:start
echo == Launching a non persistent server#0 ==
start /D "%RUN_DIR%" /B %JAVA% -classpath %CLASSPATH% fr.dyade.aaa.agent.AgentServer 0 ./s0

