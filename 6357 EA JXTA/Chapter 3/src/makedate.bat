set SHELLROOT=..
javac -classpath %SHELLROOT%\lib\jxta.jar;%SHELLROOT%\lib\jxtashell.jar -d %SHELLROOT%\classes net\jxta\impl\shell\bin\date\*.java
cd ..\classes
jar cvf ..\custcmds\datecmd.jar net\jxta\impl\shell\bin\date\date.class
cd ..\src

