@echo off
color 5
title DC/Abuse Set Token

mkdir cfg
type nul>cfg/token.txt
type nul>cfg/id.txt

cls

echo Made by Nexiii / v0.1.2_A
set /p token=Enter your Bot token: 
echo %token%>>cfg/token.txt
set /p id=Enter your Bot ID: 
echo %id%>>cfg/id.txt
echo Token and ID has been set. Bot will start after pressing any key.
echo Now if you close the Bot you can just reopen "start.bat" it will start normally from now on. :)
PAUSE
call "start.bat"