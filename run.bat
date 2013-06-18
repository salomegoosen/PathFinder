set toolsDir=C:\Users\Salome_2\My Work\Source Code\AStarPathFinder\tools
set JAVA_HOME=%toolsDir%\jdk1.5.0_15\jre
set ANT_HOME=%toolsDir%\apache-ant-1.9.1
set PATH=%JAVA_HOME%;%ANT_HOME%\bin;%JAVA_HOME%\bin

java -cp dist\PathFinder.jar;dist\log4j-1.2.13.jar pathfinding.MyUsage %1
