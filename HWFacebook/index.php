<?php
/* include the PHP Facebook Client Library to help
  with the API calls and make life easy */
require_once('inc/facebook/php/facebook.php');

/* initialize the facebook API with your application API Key
  and Secret */
$facebook = new Facebook("75877dd750ec5e0865a8af179870d7f9","04bdbae113e0a64ab14cc9c59ad72b2f");

/* require the user to be logged into Facebook before
  using the application. If they are not logged in they
  will first be directed to a Facebook login page and then
  back to the application's page. require_login() returns
  the user's unique ID which we will store in fb_user */
$fb_user = $facebook->require_login();

var_dump($facebook->fb_params);
/* now we will say:
  Hello USER_NAME! Welcome to my first application! */
?>

Hello <fb:name uid='<?php echo $fb_user; ?>' useyou='false' possessive='true' />! Welcome to my first application!

<script src="js/jquery-1.3.2.min.js" type="text/javascript"></script>

<script> 
	<!-- 
	var element = document.getElementById("test");
//	element.setTextValue("Snuh!!!!");
	element.appendChild($("Hello World How Are You Today!"));
	-->
</script>

<div id="test">
	Hello.
</div>

<?php

/* We'll also echo some information that will
  help us see what's going on with the Facebook API: */
echo "<pre>Debug:" . print_r($facebook,true) . "</pre>";

?>
