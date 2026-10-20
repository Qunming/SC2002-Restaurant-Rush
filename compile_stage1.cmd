
@echo off
setlocal

rem Run from the repository root, even if this script was opened elsewhere.
pushd "%~dp0" || exit /b 1

if not exist build mkdir build

rem Main.java imports every class needed for the Stage 1 application.
javac -encoding UTF-8 -sourcepath src -d build src\app\Main.java
if errorlevel 1 (
    echo Compilation failed.
    popd
    exit /b 1
)

echo Compilation successful.
echo To play: java -cp build app.Main
popd
endlocal
