#!/bin/sh
mvn install:install-file -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar -Dfile=jta-1.0.1B/jta-1.0.1B.jar -DgeneratePom=true
