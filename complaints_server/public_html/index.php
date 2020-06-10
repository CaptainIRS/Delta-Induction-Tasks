<?php 

  $host     = "complaints_db";
  $user     = "db_user";
  $password = "cH13F_c0mM4nD3r";
  $database = "complaints_db";

  $mysqli = new mysqli($host, $user, $password, $database);
  if ($mysqli->connect_error) {
    die("MySQL Connection Error " . $mysqli->connect_error);
  }

  if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $user = $_POST["user"];  
    $complaint = $_POST["complaint"];
    $stmt = $mysqli->prepare("INSERT INTO complaints (user, complaint) VALUES (?, ?)");
    $stmt->bind_param("ss", $user, $complaint);
    $stmt->execute();
  } else {
    $sth = mysqli_query($mysqli, "SELECT user, complaint FROM complaints");
    $rows = array();
    while($r = mysqli_fetch_assoc($sth)) {
        $rows[] = $r;
    }
    echo json_encode($rows);
  }
?>
