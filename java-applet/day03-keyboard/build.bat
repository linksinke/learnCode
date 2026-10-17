@echo off
setlocal
cd /d "%~dp0"

set OUT=res

javac -encoding UTF-8 -d "%OUT%" ^
  KeyboardApplet.java ^
  KeyboardPanel.java

if errorlevel 1 (
  echo Compile failed
  exit /b 1
)

echo Compile OK, starting...
appletviewer res/index.html
endlocal
