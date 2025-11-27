@echo off
@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@REM Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@REM Add default JVM options here. You can also use JAVA_OPTS and MAVEN_OPTS to pass JVM options to this script.
set MAVEN_OPTS=-Xms512m -Xmx1024m

@REM Find the project base dir, i.e., the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.

if exist "%APP_HOME%\.mvn" goto existBaseDir
cd ..
set APP_HOME=%CD%
goto baseDirSet

:existBaseDir
cd /D "%APP_HOME%"

:baseDirSet

set WRAPPER_JAR="%APP_HOME%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM If %MAVEN_JAVA_EXE% is defined, use it
if not "%MAVEN_JAVA_EXE%"=="" goto gotJavaExe

set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto gotJavaExe

echo.
echo ERROR: JAVA_HOME not set and 'java' command not found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.
goto error

:gotJavaExe

@REM Use Maven wrapper if exists
if exist %WRAPPER_JAR% (
    set MVN_CMD="%JAVA_EXE%" %MAVEN_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%APP_HOME%" %WRAPPER_LAUNCHER% %MAVEN_CONFIG%
) else (
    echo.
    echo ERROR: Maven wrapper not found.
    echo.
    echo Download the Maven wrapper from https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar
    echo and place it in the .mvn\wrapper directory.
    echo.
    goto error
)

@REM Execute Maven
%MVN_CMD% %*

:end
@REM End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:error
exit /b 1

:mainEnd
exit /b 0