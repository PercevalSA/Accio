<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
    mysql_connect("niam.rezel.net","accio","pact53");
    mysql_select_db("accio");

		$manufacturer = $_GET['manufacturer'];

    $q=mysql_query("SELECT MarqueID FROM marque WHERE Nom = '$manufacturer'");

    if (!$q) {
      die('Could not query:' . mysql_error());
    }

    if(mysql_num_rows($q) === 0){
      echo "no";
    }

    else{
      echo mysql_fetch_row($q)[0];
    }

    mysql_close();
?>
