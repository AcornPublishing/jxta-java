set SHELLROOT=..
javac -classpath %SHELLROOT%\lib\jxta.jar;%SHELLROOT%\lib\jxtashell.jar -d %SHELLROOT%\classes net\jxta\impl\shell\bin\mkdoc\*.java
cd ..\classes
jar cvf ..\custcmds\doccmd.jar net\jxta\impl\shell\bin\mkdoc\*.class
cd ..\src

