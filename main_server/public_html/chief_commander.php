<?php 
   $user = 'ChiefCommander';
   ?>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
      <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <link rel="stylesheet" href="https://unpkg.com/bootstrap-table@1.16.0/dist/bootstrap-table.min.css">
      <script src="https://unpkg.com/bootstrap-table@1.16.0/dist/bootstrap-table.min.js"></script>
      <title>Chief Commander</title>
   </head>
   <style>
      html,body {
      height: 100%;
      }
   </style>
   <body>
      <nav class="navbar navbar-expand-md navbar-dark bg-dark">
         <div class="container-fluid">
            <a href="#" class="navbar-brand mr-3">Chief Commander Portal</a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
               <div class="navbar-nav ml-auto">
                  <li class="nav-item">
                     <button class="btn btn-danger navbar-btn" onclick="loadData()" data-toggle="modal" data-target="#complaintModal">View Complaints</button>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link" href="#">Welcome, <?php echo $user; ?></a>
                  </li>
               </div>
            </div>
         </div>
      </nav>
      <div class="modal" id="complaintModal">
         <div class="modal-dialog">
            <div class="modal-content">
               <div class="modal-header">
                  <h4 class="modal-title">Complaints</h4>
                  <button type="button" class="close" data-dismiss="modal">&times;</button>
               </div>
               <div class="modal-body">
                  <table id="complaints-table" class="table table-striped table-bordered table-hover w-100">
                  </table>
               </div>
               <div class="modal-footer">
                  <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
               </div>
            </div>
         </div>
      </div>
      <div class="container-fluid d-flex flex-column" style="height: calc(100% - 56px);">
         <div class="row flex-fill" style="min-height:0">
            <div class="col mh-100 p-3">
               <div class="card  mh-100">
                  <div class="card-header">
                     Past & Future Attendance Records
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <div id="cards">
                           <?php
                              $fpath = "/home/" . $user . "/attendance_record";
                              if(!file_exists($fpath)) {
                                shell_exec("bash /scripts/finalAttendance");
                              }
                              $f = fopen($fpath, "r");
                              $record = array();
                              while (($data = fgetcsv($f)) !== false) {
                                if(!array_key_exists($data[0], $record)) $record[$data[0]] = array();
                                $temp_data = $record[$data[0]];
                                if(sizeof($temp_data)===0) $tempdata = array();
                                array_push($temp_data, htmlspecialchars($data[1]));
                                $record[$data[0]] = $temp_data;
                              }
                              foreach($record as $date_key => $present_array) {
                                $date = strtotime($date_key);
                                $date_str = date('d M Y', $date);
                                if(date('d M Y', $date) == date('d M Y', time())) { $t_date = $date; $t_present = $present_array; }
                                echo "<div class='card'> \n";
                                echo "  <div class='card-header' id='heading{$date_key}'>\n";
                                echo "    <h5 class='mb-0'>\n";
                                echo "      <button class='btn btn-link collapsed' data-toggle='collapse' data-target='#collapse{$date_key}' aria-expanded='false' aria-controls='collapse{$date_key}'>\n";
                                echo "        {$date_str}\n";
                                echo "      </button>\n";
                                echo "    </h5>\n";
                                echo "  </div>\n";
                                echo "  <div id='collapse{$date_key}' class='collapse' aria-labelledby='heading{$date_key}' data-parent='#cards'>\n";
                                echo "    <div class='card-body'>\n";
                                foreach($present_array as $id => $value) {
                                  $flavor = (strpos($value, "Army") !== false) ? 'danger'
                                          : ((strpos($value, "Navy") !== false) ? 'info' : 'secondary');
                                  echo "      <span class='badge badge-{$flavor}'> {$value} </span>\n";
                                }
                                echo "    </div>\n";
                                echo "  </div>\n";
                                echo "</div>\n";
                              }
                              fclose($f);
                              ?>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
            <div class="col mh-100 p-3">
               <div class="card  h-100">
                  <div class="card-header">
                     Today's Record
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <h2 class="card-title"><?php echo date('d M Y', $t_date); ?></h2>
                        <h5 class="card-text">
                           <?php 
                              foreach($t_present as $id => $value) {
                                $flavor = (strpos($value, "Army") !== false) ? 'danger'
                                        : ((strpos($value, "Navy") !== false) ? 'info' : 'secondary');
                                echo "      <span class='badge badge-{$flavor}'> {$value} </span>\n";
                              }
                              ?>
                        </h5>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <script>
         function loadData() {
         $.getJSON("../Complaint.php", function(data, status){
           if(status === "success") {
             $('#complaints-table').bootstrapTable({
         columns: [{
         field: 'user',
         title: 'Soldier'
         }, {
         field: 'complaint',
         title: 'Complaint'
         }],
         data: data
         });
           } else {
             alert("Cannot get complaint details!");
           }
         });
         }
      </script>
   </body>
</html>