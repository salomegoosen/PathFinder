USAGE NOTES:

1) Build Pre-requisites:
ANT_HOME must be set

To run full regression test, use:
ant run-full-regression-test


2) Runtime Pre-requisites:
JAVA_HOME must be set


3) CONFIGURATION:
A map configuration file must be defined.  
See example property file, terrain.properties, in dist/config folder.


4) Run on commandline by using command:
	.\run.bat <mapName> on windows
or	.\run.sh <mapName> on unix

eg .\run.bat large_map.txt