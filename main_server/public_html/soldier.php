<?php 
   $user = $_GET['user'];
   ?>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
      <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <style> 
         html,body {
         height: 100%;
         } 
      </style>
      <title>Soldier Portal</title>
   </head>
   <body>
      <nav class="navbar navbar-expand-md navbar-dark bg-dark">
         <div class="container-fluid">
            <a href="#" class="navbar-brand mr-3">Soldier Portal</a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
               <div class="navbar-nav ml-auto">
                  <li class="nav-item">
                     <button class="btn btn-danger navbar-btn" data-toggle="modal" data-target="#complaintModal">File Complaint</button>
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
               <form action="" id="complaintForm">
                  <div class="modal-header">
                     <h4 class="modal-title">Complaint Form</h4>
                     <button type="button" class="close" data-dismiss="modal">&times;</button>
                  </div>
                  <div class="modal-body">
                     <div class="form-group">
                        <label for="complaint">Enter Complaint:</label>
                        <textarea class="form-control" rows="5" id="complaint"></textarea>
                     </div>
                     <div id="successAlert" class="alert alert-success alert-dismissible fade collapse" role="alert">
                        <strong>Success!</strong> Your complaint has been submitted!
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                     </div>
                     <div id="errorAlert" class="alert alert-danger alert-dismissible fade collapse" role="alert">
                        <strong>Error!</strong> Your complaint couldn't be submitted
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                     </div>
                  </div>
                  <div class="modal-footer">
                     <button type="submit" class="btn btn-primary">Submit</button>
                     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                  </div>
               </form>
            </div>
         </div>
      </div>
      <div class="container-fluid d-flex flex-column" style="height: calc(100% - 56px);">
         <div class="row flex-fill" style="min-height:0">
            <div class="col mh-100 p-3">
               <div class="card  mh-100">
                  <div class="card-header">
                     Past & Future Positions
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <table class="table table-striped table-bordered table-hover w-100">
                           <thead>
                              <tr>
                                 <th>Date</th>
                                 <th>Location</th>
                              </tr>
                           </thead>
                           <tbody>
                              <?php
                                 $fpath = "/home/" . $user . "/post_allotted";
                                 if(!file_exists($fpath)) {
                                   header('Location: /');
                                   exit();
                                 }
                                 $f = fopen($fpath, "r");
                                 while (($data = fgetcsv($f)) !== false) {
                                   echo "<tr>";
                                   $date = strtotime($data[0]);
                                   if(date('d M Y', $date) == date('d M Y', time())) {
                                     $t_date = date('d M Y', $date);
                                     $t_loc = (htmlspecialchars($data[1] . " " . $data[2]));
                                   }
                                   echo "<td>" . date('d M Y', $date) . "</td>";
                                   echo "<td>" . htmlspecialchars($data[1] . " " . $data[2]) . "</td>";
                                   echo "</tr>\n";
                                 }
                                 fclose($f);
                                 ?>
                           </tbody>
                        </table>
                     </div>
                  </div>
               </div>
            </div>
            <div class="col mh-100 p-3">
               <div class="card  h-100">
                  <div class="card-header">
                     Today's Position
                  </div>
                  <div style="overflow-y: scroll;">
                     <div class="card-body">
                        <h5 class="card-text"><?php echo $t_date; ?>  </h5>
                        <h2 class="card-title"><?php echo $t_loc; $t_loc = preg_replace("/[^0-9\. ]/", "", $t_loc);?></h2>
                        <div id="mapdiv" style="width: 100%; height: 300px"></div>
                        <script src="http://www.openlayers.org/api/OpenLayers.js"></script>
                        <script>
                           map = new OpenLayers.Map("mapdiv");
                           map.addLayer(new OpenLayers.Layer.OSM());
                           
                           var lonLat = new OpenLayers.LonLat(<?php echo explode(" ", $t_loc)[3]; ?>, <?php echo explode(" ", $t_loc)[1]; ?>)
                                 .transform(
                                   new OpenLayers.Projection("EPSG:4326"),
                                   map.getProjectionObject()
                                 );
                                 
                           var zoom=8;
                           
                           var markers = new OpenLayers.Layer.Markers( "Markers" );
                           map.addLayer(markers);
                           
                           markers.addMarker(new OpenLayers.Marker(lonLat));
                           
                           map.setCenter (lonLat, zoom);
                        </script>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <script>
         $("#complaintForm").submit(function(event) {
         
           event.preventDefault();
         
           var complaint = $('#complaint').val();         
           $.ajax({
           type: "POST",
           url: "../Complaint.php",
           data: { 
             user: "<?php echo $user; ?>",
             complaint: complaint,
           },
           success: function(data) {
             $('#successAlert').removeClass('collapse');
             $('#successAlert').addClass('show');
           },
           fail: function() {
             $('#errorAlert').removeClass('collapse');
             $('#errorAlert').addClass('show');
           }
           });
         });
         $('.alert').on('close.bs.alert', function (event) {
           event.preventDefault();
           $(this).addClass('collapse');
           $(this).removeClass('show');
         });
      </script>
   </body>
</html>