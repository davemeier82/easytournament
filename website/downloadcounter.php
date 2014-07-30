<?php
if (isset($_GET[download])){
	$con = mysql_connect("easytou.mysql.db.internal", "easytou_guest","12ka01be1982");
	if (!$con)
	  {
	  die('Could not connect: ' . mysql_error());
	  }
	mysql_select_db("easytou_application", $con);
	mysql_query("UPDATE download SET counter = counter + 1");
	mysql_close($con);
	if($_GET[download] == 1) {
		header( 'Location: http://www.easy-tournament.com/downloads/ETInstaller-0.8.4.0.jar' ) ;
	} else if($_GET[download] == 2) {
		header( 'Location: http://www.easy-tournament.com/downloads/EasyTournament-0.8.4.0.msi' ) ;
	} else {
		header( 'Location: http://www.easy-tournament.com/downloads/EasyTournament-0.8.4.0.zip' ) ;
	}
	
} else {
 echo "ERROR";
}
?>