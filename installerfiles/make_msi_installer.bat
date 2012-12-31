del EasyTournament-@VERSION@.msi
heat dir ../help/de -cg helpFilesDe -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr helpdirDe -o helpDe.wxs
heat dir ../help/en -cg helpFilesEn -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr helpdirEn -o helpEn.wxs
heat dir ../lib -cg libFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr libdir -o lib.wxs
heat dir ../res -cg resFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr resdir -o res.wxs
heat dir ../sports -cg sportsFiles -var var.SourceDir -scom -sreg -sfrag -srd -gg -dr sportsdir -o sports.wxs
candle -ext WixUIExtension -ext WixUtilExtension  -dLANG=1033 wixinstaller_rel.wxs 
candle -ext WixUIExtension -ext WixUtilExtension LicenseAgreementDialogOverwritten.wxs 
candle -dSourceDir=../help/de helpDe.wxs
candle -dSourceDir=../help/en helpEn.wxs
candle -dSourceDir=../res res.wxs
candle -dSourceDir=../lib lib.wxs
candle -dSourceDir=../sports sports.wxs
light -ext WixUIExtension -ext WixUtilExtension -cultures:en-us -loc lang/Loc_en-us.wxl -out EasyTournament-@VERSION@.msi wixinstaller_rel.wixobj LicenseAgreementDialogOverwritten.wixobj helpDe.wixobj helpEn.wixobj res.wixobj lib.wixobj sports.wixobj
candle -ext WixUIExtension -ext WixUtilExtension -dLANG=1031 wixinstaller_rel.wxs 
light -ext WixUIExtension -ext WixUtilExtension -cultures:de-de -loc lang/Loc_de-de.wxl -out EasyTournament-de.msi wixinstaller_rel.wixobj LicenseAgreementDialogOverwritten.wixobj helpDe.wixobj helpEn.wixobj res.wixobj lib.wixobj sports.wixobj
msitran -g EasyTournament-@VERSION@.msi EasyTournament-de.msi de.mst
wisubstg.vbs EasyTournament-@VERSION@.msi de.mst 1031
WiLangId.vbs EasyTournament-@VERSION@.msi Package 1033,1031
del wixinstaller_rel.wixobj
del helpDe.wixobj
del helpEn.wixobj
del res.wixobj
del lib.wixobj
del sports.wixobj
del LicenseAgreementDialogOverwritten.wixobj
del helpDe.wxs
del helpEn.wxs
del lib.wxs
del res.wxs
del sports.wxs
del de.mst
del EasyTournament-de.msi
del EasyTournament-@VERSION@.wixpdb
del EasyTournament-de.wixpdb