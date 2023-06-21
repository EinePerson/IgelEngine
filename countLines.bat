@echo off
setlocal enabledelayedexpansion
set /a count=0

for /r "src\main\java" %%f in (*) do (
    set /a lines=0
    for /f "tokens=*" %%l in (%%f) do (
        set /a lines+=1
    )
    set /a count+=lines
)

echo Total number of lines: %count%
pause