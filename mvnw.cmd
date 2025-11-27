@echo off
echo Installing Maven...
powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile 'maven.zip'"
powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath '.' -Force"
.\apache-maven-3.9.6\bin\mvn.cmd %*
