



<?php

include_once('dbConn.php');

// if($_SERVER['REQUEST_METHOD'] == "POST"){
// 	// Get data
// 	$name = isset($_POST['name']) ? mysql_real_escape_string($_POST['name']) : "";
// 	$email = isset($_POST['email']) ? mysql_real_escape_string($_POST['email']) : "";
// 	$password = isset($_POST['pwd']) ? mysql_real_escape_string($_POST['pwd']) : "";
// 	$status = isset($_POST['status']) ? mysql_real_escape_string($_POST['status']) : "";

// TODO get lat/lng from GET

// Insert data into data base
$query = "SELECT * FROM photos;";
$result = mysql_query($query);

while ($row = mysql_fetch_assoc($result)) {
    $photo[] = $row;
}

$struct = array("photo" => $photo);

@mysql_close($conn);

/* Output header */
	header('Content-type: application/json');
	echo json_encode($struct);