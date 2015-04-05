#!/bin/bash
mkdir .ant
mkdir .ant/lib
wget -O .ant/lib/junit-4.12.jar "http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar"
wget -O lib/hamcrest-core-1.3.jar "http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
