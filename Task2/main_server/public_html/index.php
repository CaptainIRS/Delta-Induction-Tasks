<?php

include 'db_util.php';

$loggedin = 'false';
$user = '';

if($_SERVER['REQUEST_METHOD'] === 'POST') {
    if(isset($_POST['logout'])) {
        echo '<script>alert("Logged out!")</script>';
	} else if(isset($_POST['forgot_password'])) {
		if(isset($_POST['username'])) {
			echo '<script>alert("Password hint: '. get_hint($_POST['username']). '")</script>';
		} else {
			echo '<script>alert("Please enter your username!")</script>';
		}
	} else if (isset($_POST['username'])
		&& isset($_POST['password'])
		&& isset($_POST['hint'])
	) {
		if(is_user_unique($_POST['username'])) {
			register_user($_POST['username'], $_POST['password'], $_POST['hint']);
            echo '<script>alert("User registered! Login now!")</script>';
		} else {
			echo '<script>alert("Username already exists!")</script>'; 
		}
	} else if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['remember_login'])) {
        if(check_user($_POST['username'], $_POST['password']) == TRUE) {    
            
            setcookie('username', $_POST['username']);
            setcookie('password', $_POST['password']);
            setcookie('remember_login', $_POST['remember_login'] ? 'true' : 'false');
            $loggedin = 'true';
            $user = $_POST['username'];
            
        } else {
            echo '<script>alert("Incorrect username or password!")</script>';
        }
    } else {
        echo '<script>alert("Invalid post request!")</script>';
    }
}

if(isset($_COOKIE['remember_login'])
	&& isset($_COOKIE['username'])
	&& isset($_COOKIE['password'])) {
	if(check_user($_COOKIE['username'], $_COOKIE['password']) === TRUE) {
		$loggedin = 'true';
		$user = $_COOKIE['username'];
	}
}

?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Anything Signin</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
        <style>
            html, body {
                height: 100%;
            }
        </style>
    </head>
    <body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark">
         <div class="container-fluid">
            <a href="#" class="navbar-brand mr-3">Anything.com</a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
               <div class="navbar-nav ml-auto">
                  <?php if($loggedin === 'false')  { echo '
                  <li class="nav-item">
                     <button class="btn btn-success navbar-btn mr-3" data-toggle="modal" data-target="#registerModal">
                       Register
                     </button>
                  </li>
                  <li class="nav-item">
                     <button class="btn btn-danger navbar-btn mr-4" data-toggle="modal" data-target="#loginModal">Login</button>
                  </li>
                  ';} ?>
                  <?php if($loggedin === 'true') { echo '
                  <li class="nav-item">
                     <a class="nav-link mr-2" href="#">Welcome,'. $user .'</a>
                  </li>
                  <li class="nav-item">
                     <button id="logout" class="btn btn-danger navbar-btn mr-1">Logout</button>
                  </li>
                  '; } ?>
               </div>
            </div>
         </div>
      </nav>
        <div class="modal" id="registerModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="" id="registrationForm">
                        <div class="modal-header">
                            <h4 class="modal-title">Register</h4>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div class="input-group mb-3">
                                   <div class="input-group-prepend">
                                      <span class="input-group-text" id="basic-addon1">@</span>
                                   </div>
                                   <input id="reg_username" type="text" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1">
                                </div>
                                <div class="input-group mb-3">
                                   <input id="reg_password" type="password" class="form-control" placeholder="Password" aria-label="Password">
                                </div>
                                <div class="input-group mb-3">
                                   <input id="reg_hint" type="text" class="form-control" placeholder="Hint" aria-label="Hint">
                                </div>
                            </div>
                            <div id="regSuccessAlert" class="alert alert-success alert-dismissible fade collapse" role="alert">
                                <strong>Success!</strong> Your complaint has been submitted!
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div id="regErrorAlert" class="alert alert-danger alert-dismissible fade collapse" role="alert">
                                <strong>Error!</strong> Your complaint couldn't be submitted
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#registrationModal">Submit</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal" id="loginModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="" id="loginForm">
                        <div class="modal-header">
                            <h4 class="modal-title">Login</h4>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div class="input-group mb-3">
                                   <div class="input-group-prepend">
                                      <span class="input-group-text" id="basic-addon1">@</span>
                                   </div>
                                   <input id="login_username" type="text" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1">
                                </div>
                                <div class="input-group mb-3">
                                   <input id="login_password" type="password" class="form-control" placeholder="Password" aria-label="Password">
                                </div>
                                <div class="input-group mb-3">
                                    <a id="forgot_password" class="link">Forgot password</a>
                                </div>
                                <div class="input-group mb-3" class="checkbox">
                                    <label>
                                        <input id="remember_me" type="checkbox" value="remember-me"> Remember me
                                    </label>
                                </div>
                            </div>
                            <div id="regSuccessAlert" class="alert alert-success alert-dismissible fade collapse" role="alert">
                                <strong>Success!</strong> Your complaint has been submitted!
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div id="regErrorAlert" class="alert alert-danger alert-dismissible fade collapse" role="alert">
                                <strong>Error!</strong> Your complaint couldn't be submitted
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#registrationModal">Login</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" crossorigin="anonymous"></script>
        
        <script>
        $("#forgot_password").click(function(event) {
           var login_username = $('#login_username').val();
           
           document.body.innerHTML += '<form id="temp_form" action="/" method="post"><input type="hidden" name="username" value="' + login_username + '"><input type="hidden" name="forgot_password"></form>';
           document.getElementById("temp_form").submit();
        });
        
        $("#logout").click(function(event) {
           document.cookie.split(";").forEach(function(c) { document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); });
           document.body.innerHTML += '<form id="temp_form" action="/" method="post"><input name="logout"></form>';
           document.getElementById("temp_form").submit();
        });
        
        $("#loginForm").submit(function(event) {
         
           event.preventDefault();
           var login_username = $('#login_username').val();  
           var login_password = $('#login_password').val();
           var remember_me = $('#remember_me').is(":checked");
           document.body.innerHTML += '<form id="temp_form" action="/" method="post"><input type="hidden" name="username" value="' + login_username + '"><input type="hidden" name="password" value="' + login_password + '"><input type="hidden" name="remember_login" value="' + remember_me + '"></form>';
           document.getElementById("temp_form").submit();
         });
         
        $("#registrationForm").submit(function(event) {
         
           event.preventDefault();
         
           var reg_username = $('#reg_username').val();  
           var reg_password = $('#reg_password').val();
           var reg_hint = $('#reg_hint').val(); 
           document.body.innerHTML += '<form id="temp_form" action="/" method="post"><input type="hidden" name="username" value="' + reg_username + '"><input type="hidden" name="password" value="' + reg_password + '"><input type="hidden" name="hint" value="' + reg_hint + '"></form>';
           document.getElementById("temp_form").submit();
         });
         $('.alert').on('close.bs.alert', function (event) {
           event.preventDefault();
           $(this).addClass('collapse');
           $(this).removeClass('show');
         });
        </script>
    </body>
</html>
