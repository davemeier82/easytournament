<?php
require("_resources/_phpmailer/class.phpmailer.php");
if (isset($_POST[email]) && isset($_POST[version]) && isset($_POST[lang]) && isset($_POST[os_name]) && isset($_POST[os_arch]) && isset($_POST[os_vers]) && isset($_POST[java_vendor]) && isset($_POST[java_version]) && isset($_POST[error]) ){
		$header = 'ET Server';
		$mail = new PHPMailer();
		$mail->IsSMTP();               // per SMTP verschicken
		$mail->Host     = "localhost"; // SMTP-Server
		$mail->From     = "support@easy-tournament.com";
		$mail->FromName = "support@easy-tournament.com";
		$mail->Subject = "[ERROR]";
		$mail->Body = "email: ".$_POST[email]."\nVersion: ".$_POST[version]."\nLanguage: ".$_POST[lang]."\nOS: ".$_POST[os_name]." ".$_POST[os_arch]." ".$_POST[os_vers]
		."\nJava: ".$_POST[java_vendor]." ".$_POST[java_version]."\nError:\n\n".$_POST[error];
		$mail->AddAddress("support@easy-tournament.com");
		if($mail->Send())
		{
			echo "OK";
		} else {
			echo "SEND ERROR";
		}
}
else if (isset($_GET[version]) && isset($_GET[lang]) && isset($_GET[os_name]) && isset($_GET[os_arch]) && isset($_GET[os_vers]) && isset($_GET[java_vendor]) && isset($_GET[java_version]) && isset($_GET[error]) ){
		$header = 'ET Server';
		$mail = new PHPMailer();
		$mail->IsSMTP();               // per SMTP verschicken
		$mail->Host     = "localhost"; // SMTP-Server
		$mail->From     = "support@easy-tournament.com";
		$mail->FromName = "support@easy-tournament.com";
		$mail->Subject = "[ERROR]";
		$mail->Body = $mail->Body."Version: ".$_GET[version]."\nLanguage: ".$_GET[lang]."\nOS: ".$_GET[os_name]." ".$_GET[os_arch]." ".$_GET[os_vers]
		."\nJava: ".$_GET[java_vendor]." ".$_GET[java_version]."\nError:\n\n".$_GET[error];
		$mail->AddAddress("support@easy-tournament.com");
		if($mail->Send())
		{
			echo "OK";
		} else {
			echo "SEND ERROR";
		}
} else {
 echo "ERROR";
}
?>