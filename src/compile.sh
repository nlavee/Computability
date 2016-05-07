#!/bin/bash

# Compile java file
javac -cp .:../lib/log4j-1.2.9.jar main/java/project/Implementation.java

# Run java file
java -cp .:../lib/log4j-1.2.9.jar main.java.project.Implementation
