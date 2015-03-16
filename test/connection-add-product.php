<?php

	$dbhost = 'niam.rezel.net';
	$dbuser = 'accio';
	$dbpass = 'pact53';
	$conn = mysql_connect($dbhost, $dbuser, $dbpass);
	if(! $conn )
	{
  	die('Could not connect: ' . mysql_error());
	}

  $nom = $_GET['nom'];
  $codebarre = $_GET['codebarre'];
	$boite = $_GET['boite'];
	$marque = $_GET['marque'];

	$sql = 	"INSERT INTO aliment ".
       		"(AlimentID, Nom, CodeBarre, BoiteID, HistoriqueID, MarqueID, CorrespondID) ".
       		"VALUES ".
       		"(NULL,'$nom','$codebarre','$boite',NULL,'$marque',NULL)";
	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not enter data: ' . mysql_error());
	}
	echo "Aliment rajouté\n";
	mysql_close($conn);

?>
