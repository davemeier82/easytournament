<?php
header('Content-Type: application/octet-stream');
header('Content-Disposition: attachment; filename="euro2008.ett"');
echo file_get_contents('downloads/euro2008.ett');
?>