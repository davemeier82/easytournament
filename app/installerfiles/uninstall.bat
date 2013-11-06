set currdir="%CD%\Uninstaller\uninstaller.jar"
IF EXIST uninstallXP.bat (
uninstallXP.bat
) ELSE (
elevate javaw -jar %currdir%
)