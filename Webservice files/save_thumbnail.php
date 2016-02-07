



<?php

include_once('dbConn.php');

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


$data = isset($_POST['string']) ? mysql_real_escape_string($_POST['string']) : "";
$lat = isset($_POST['lat']) ? mysql_real_escape_string($_POST['lat']) : "";
$lng = isset($_POST['lng']) ? mysql_real_escape_string($_POST['lng']) : "";

$filename = rand() . rand() . rand();

$binary=base64_decode($data);
header('Content-Type: bitmap; charset=utf-8');
// Images will be saved under 'www/imgupload/uplodedimages' folder
$file = fopen('images/'.$filename.'.PNG', 'wb');
// Create File
fwrite($file, $binary);
fclose($file);


$query = "INSERT INTO photos (lat, lng, url) VALUES (".$lat.", ".$lng.", '".$filename."')";
$result = mysql_query($query);



@mysql_close($conn);

