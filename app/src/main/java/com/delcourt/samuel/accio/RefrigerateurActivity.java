package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Scanner;


public class RefrigerateurActivity extends ActionBarActivity {

    public static Refrigerateur refrigerateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerateur);

        //Toast temporaire
        Toast.makeText(getApplicationContext(), "Pas encore de connexion à la bdd",Toast.LENGTH_SHORT).show();

        //On récupère toutes les infos du frigo en accédant à la mémoire de l'appli(fichiers textes)
        boolean chargementReussi = chargementRéfrigerateur();
        if (chargementReussi==false){//si le chargement du frigo ou des boîtes a échoué, on affiche un message
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement du frigo (liste des boîtes Accio inaccessible)", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        boolean connectionReussie = connectionBDD();
        if (connectionReussie == false) {//Si la connection a échoué, on affiche un message
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur de connexion à la base de données", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        TextView textElement = (TextView) findViewById(R.id.frigoNameMenu);
        textElement.setText("Réfrigérateur : " + refrigerateur.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refrigerateur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                // on mettra la méthode openSettings() quand elle sera cree
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearch() {
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public void sendMessageContent(View view) {
        Intent intent = new Intent(this, ListeBoitesActivity.class);
        startActivity(intent);
    }

    public void sendMessageRecipes(View view) {
        Intent intent = new Intent(this, RecipesActivity.class);
        startActivity(intent);
    }

    public void sendMessageFavorite(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void sendMessageHistorique(View view) {
        Intent intent = new Intent(this, HistoriqueActivity.class);
        startActivity(intent);
    }

    public void sendMessageHelp(View view) {
        Uri webpage = Uri.parse("http://www.android-help.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public boolean chargementRéfrigerateur() {

        //ON RECREE LE REFRIGERATEUR AVEC SES BOITES
        //Le nom du réfrigérateur a été spécifié lors du choix du frigo. On récupère maintenant la liste des boîtes

        // ATTENTION : les boites ne connaissent pas encore leur référence dans la base de données

        //Lecture de la liste des boîtes et création des boîtes (pour l'instant vides)
        boolean chargementReussi;
        InputStream instream = null;
        String nameFrigo = refrigerateur.getName();
        refrigerateur = new Refrigerateur(nameFrigo);//Réinitialise l'ensemble du réfrigérateur (pour tenir compte d'éventuelles modif)
        try {
            instream = openFileInput(nameFrigo + "Boxes.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);

            while (sc.hasNextLine() == true) {//On recrée la liste des boites et la liste des noms des boîtes

                String refBdd = sc.nextLine();
                String name = sc.nextLine();
                String type = sc.nextLine();

                Box box = new Box(refBdd, name, type);
                refrigerateur.getBoxes().add(box);
            }
            chargementReussi = true;

        } catch (FileNotFoundException e) {
            chargementReussi = false;
        }
        return chargementReussi;
    }



    public static boolean connectionBDD() {//On connecte la Bdd et pr chaque boîte on remplit la liste des aliments et celle des favoris
        boolean retour;
        try {
            int nbBoites = refrigerateur.getBoxes().size();
            for (int j = 0; j < nbBoites; j++) {
                String refBdd = refrigerateur.getBoxes().get(j).getReferenceBdd();

                //Pour chaque aliment de la boîte en question dans la base de donnée, on crée un objet : aliment = new Aliment(nom,favori,historique)
                // et on l'ajoute à la liste des aliments : refrigerateur.getBoxes().get(j).getListeAliments().add(Aliment)
                //Remarque : historique est un array-list, il faut peut etre le créer avant



                refrigerateur.setConnectionBdd(true);//Permet au reste de l'appli que la connection à la base de données a bien eu lieu
            }
            retour = true;

        } catch (Exception e) {
            refrigerateur.setConnectionBdd(false);//Permet au reste de l'appli de savoir que la connection à la bdd n'a pas eu lieu
            retour = false;
        }
        return retour;
    }
}
