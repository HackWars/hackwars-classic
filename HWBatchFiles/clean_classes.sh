#! /bin/bash

#Use this to clean out old class files from the classes directory
#The classes shouldn't end up in the classes directory (ok very poor
#naming) if you use the ant build script (build.xml). But if you do 
#run javac by mistake, this will clean up.
#Note: do not run this on the net/org/edu etc dirs, these are static
#and never compiled

find Applet/ Assignments/ Browser/ GUI/ Game/ HackerLogin/ HackerSearch/ \
Hackscript/ Hacktendo/ Server/ View/ chat simulation/   \
-name "*.class" | xargs rm -f