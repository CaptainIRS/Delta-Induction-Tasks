<?php 

  $host     = "users_db";
  $user     = "l33t_user";
  $password = "l33t_h4xx0r";
  $database = "l33t_db";

  mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
  
  $mysqli = new mysqli($host, $user, $password, $database);
  if ($mysqli->connect_error) {
    die("MySQL Connection Error " . $mysqli->connect_error);
  }

  function check_user($username, $password) {
	global $mysqli;
  	$stmt = mysqli_query($mysqli, "SELECT * FROM users WHERE username = '$username' AND password = '$password' LIMIT 1");
	$rows = mysqli_fetch_assoc($stmt);
	if(!$rows || mysqli_num_rows($stmt) !== 1) {
		return FALSE;
	}
    return TRUE;	
  }
	
  function is_user_unique($username) {
    global $mysqli;
  	$stmt = mysqli_query($mysqli, "SELECT username FROM users WHERE username = '$username' LIMIT 1");
	if(mysqli_num_rows($stmt) < 1) {
		return TRUE;
	}
    return FALSE;	
  }

  function register_user($username, $password, $hint) {
    global $mysqli;
    $stmt = $mysqli->prepare("INSERT INTO users (username, password, hint) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $username, $password, $hint);
    $stmt->execute();
  }

  function get_hint($username) {
	global $mysqli;
	$stmt = mysqli_query($mysqli, "SELECT hint FROM users WHERE username = '$username'");
	$rows = mysqli_fetch_assoc($stmt);
	if(!$rows || mysqli_num_rows($stmt) !== 1) {
		return 'Username doesn\'t exist!';	
	}
	return $rows['hint'];
  }

?>
