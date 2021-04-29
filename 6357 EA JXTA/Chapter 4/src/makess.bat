javac -classpath ..\lib\jxta.jar;..\lib\jxtashell.jar -d ..\classes com\wrox\ea\jxtaservice\*.java
javac -classpath ..\classes;..\lib\jxta.jar;..\lib\jxtashell.jar  -d ..\classes net\jxta\impl\shell\bin\sstest\*.java
cd ..\classes
jar cvf ..\lib\jxtaservice.jar .
cd ..\src
