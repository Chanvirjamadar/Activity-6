#!/bin/bash
echo "================================================================"
echo "          COMPILING STUDENT MANAGEMENT SYSTEM (JAVA SWING)      "
echo "================================================================"

mkdir -p bin

echo "Compiling Java source files..."
javac -encoding UTF-8 -d bin src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/Main.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed. Please check your JDK installation."
    exit 1
fi

echo "[SUCCESS] Compilation completed successfully!"
echo "Launching Student Management System GUI..."
java -cp bin Main

