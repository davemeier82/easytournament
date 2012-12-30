heat dir ../help -cg helpFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr helpdir -o help.wxs
heat dir ../lib -cg libFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr libdir -o lib.wxs
heat dir ../res -cg resFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr resdir -o res.wxs
heat dir ../sports -cg sportsFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr sportsdir -o sports.wxs
candle -ext WixUIExtension -ext WixUtilExtension wixinstaller.wxs 
candle -dSourceDir=../help help.wxs
candle -dSourceDir=../res res.wxs
candle -dSourceDir=../lib lib.wxs
candle -dSourceDir=../sports sports.wxs
light -ext WixUIExtension -ext WixUtilExtension -cultures:en-us -loc lang/Loc_en-us.wxl -out EasyTournament.msi wixinstaller.wixobj help.wixobj res.wixobj lib.wixobj sports.wixobj
@pause