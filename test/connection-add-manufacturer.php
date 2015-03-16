<?php

  $conn = mysql_connect("niam.rezel.net","accio","pact53");

  $manufacturer = $_GET['manufacturer'];

	if(! $conn )
	{
  	die('Could not connect: ' . mysql_error());
	}

	$sql = 	"INSERT INTO marque ".
       		"(Nom) ".
       		"VALUES ".
       		"('$manufacturer')";

	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not enter data: ' . mysql_error());
	}
	// echo "Marque rajoutée";

  $q=mysql_query("SELECT MarqueID FROM marque WHERE Nom = '$manufacturer'");

  if (!$q) {
    die('Could not query:' . mysql_error());
  }

  echo mysql_fetch_row($q)[0];

	mysql_close($conn);

?>
