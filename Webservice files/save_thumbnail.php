



<?php

include_once('dbConn.php');

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


$fullImage = isset($_POST['fullImage']) ? mysql_real_escape_string($_POST['fullImage']) : "";
$thumbnail = isset($_POST['thumbnail']) ? mysql_real_escape_string($_POST['thumbnail']) : "";
$lat = isset($_POST['lat']) ? mysql_real_escape_string($_POST['lat']) : "";
$lng = isset($_POST['lng']) ? mysql_real_escape_string($_POST['lng']) : "";
$title = isset($_POST['title']) ? mysql_real_escape_string($_POST['title']) : "";
$description = isset($_POST['description']) ? mysql_real_escape_string($_POST['description']) : "";

$filename = rand() . rand() . rand();

// save full image
$binary=base64_decode($fullImage);
header('Content-Type: bitmap; charset=utf-8');
// Images will be saved under 'www/imgupload/uplodedimages' folder
$file = fopen('images/'.$filename.'.PNG', 'wb');
// Create File
fwrite($file, $binary);
fclose($file);

// save thumbnail
$binary=base64_decode($thumbnail);
header('Content-Type: bitmap; charset=utf-8');
// Images will be saved under 'www/imgupload/uplodedimages' folder
$file = fopen('images/'.$filename.'_thumbnail.PNG', 'wb');
// Create File
fwrite($file, $binary);
fclose($file);

$query = "INSERT INTO photos (lat, lng, url, title, description) VALUES (".$lat.", ".$lng.", '".$filename."', '".$title."', '".$description."')";
$result = mysql_query($query);

@mysql_close($conn);

