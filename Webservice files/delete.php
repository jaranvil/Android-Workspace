
<?php

include_once('dbConn.php');

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


$file = isset($_GET['file']) ? mysql_real_escape_string($_GET['file']) : "";

$query = "DELETE FROM photos WHERE url = '".$file."'";
$result = mysql_query($query);

@mysql_close($conn);