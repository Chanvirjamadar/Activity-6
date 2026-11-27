@echo off
title Student Management System
echo ================================================================
echo           COMPILING STUDENT MANAGEMENT SYSTEM (JAVA SWING)
echo ================================================================

if not exist bin mkdir bin

echo Compiling Java source files...
javac -encoding UTF-8 -d bin src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Please ensure JDK 8 or higher is installed and in your PATH.
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCCESS] Compilation completed successfully!
echo.
echo Launching Student Management System GUI...
java -cp bin Main
pause

