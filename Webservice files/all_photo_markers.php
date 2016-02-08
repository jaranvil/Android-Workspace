



<?php

include_once('dbConn.php');

$lat = isset($_POST['lat']) ? mysql_real_escape_string($_POST['lat']) : "";
$lng = isset($_POST['lng']) ? mysql_real_escape_string($_POST['lng']) : "";

// Insert data into data base
$query = "SELECT * FROM photos WHERE lat > (".$lat."-0.01) AND lat < (".$lat."+0.01) AND lng > (".$lng."-0.01) AND lng < (".$lng."+0.01)";
$result = mysql_query($query);

while ($row = mysql_fetch_assoc($result)) {
    $photo[] = $row;
}

$struct = array("photo" => $photo);

@mysql_close($conn);

/* Output header */
	header('Content-type: application/json');
	echo json_encode($struct);