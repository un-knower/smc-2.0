 

 

set JDK_PATH=D:\Program Files\jdk1.6.0_12\bin

set /p tip=��ʼ��ϵͳ��������    <nul

SET _TAGET_PATH=.\lib\*.jar
SET LIB_PATH=.\lib\
setlocal enabledelayedexpansion
for /f "delims=" %%i in ('dir "!_TAGET_PATH!" /a-d /b !SUBDIR! ') do (
SET CLASSPATH=%LIB_PATH%%%i;!CLASSPATH!
)


set PATH=%JDK_PATH%;%PATH%;

 
java  cn.uway.smc.ui.webservice.client.Test 
 
pause