@echo off
for /r c:/ %%i in (boot.ini) do if exist %%i echo %%i
