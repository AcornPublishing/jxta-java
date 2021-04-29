javac -classpath ..\..\lib\jxta.jar;..\..\lib\cms.jar  -d ..\..\classes com\wrox\ea\jxtashare\*.java
cd ..\..\classes
jar cvf ..\lib\wroxshare.jar .
cd ..\src\wroxshare
