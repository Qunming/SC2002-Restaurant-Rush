@echo off
setlocal
if not exist build mkdir build
dir /s /b src\*.java > sources.txt
javac -d build @sources.txt
if errorlevel 1 (
   echo Compilation failed.
   exit /b 1
)
echo Compilation successful. The unfinished skeleton is not yet playable.
echo To run after implementation: java -cp build app.Main
endlocal
