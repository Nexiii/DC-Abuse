@echo off
color 5
title DC/Abuse Set Token

mkdir cfg
type nul>cfg/token.txt

cls

echo Made by Nexiii / v0.1
set /p token=Enter your Bot token: 
echo %token%>>cfg/token.txt
echo Token has been set. Bot will start after pressing any key.
echo Now if you want to start the Bot just open "start.bat" it will start normally. :)
PAUSE
call "start.bat"