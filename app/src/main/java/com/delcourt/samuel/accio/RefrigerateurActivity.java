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

import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class RefrigerateurActivity extends ActionBarActivity {

    public static Refrigerateur refrigerateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerateur);

        try{chargementRéfrigerateur();}//On récupère toutes les infos du frigo en accédant à la mémoire de l'appli(fichiers textes)
        catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement frigo", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
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

    public void openSearch(){
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public void sendMessageContent(View view){
        Intent intent = new Intent(this,ListeBoitesActivity.class);
        startActivity(intent);
    }

    public void sendMessageRecipes(View view){
        Intent intent = new Intent(this,RecipesActivity.class);
        startActivity(intent);
    }

    public void sendMessageFavorite(View view){
        Intent intent = new Intent(this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void sendMessageHistorique(View view){
        Intent intent = new Intent(this,HistoriqueActivity.class);
        startActivity(intent);
    }

    public void sendMessageHelp(View view){
        Uri webpage = Uri.parse("http://www.android-help.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public void chargementRéfrigerateur() {

        //ON RECREE LE REFRIGERATEUR AVEC SES BOITES
        //Le nom du réfrigérateur a été spécifié lors du choix du frigo. On récupère maintenant la liste des boîtes

        // ATTENTION : les boites ne connaissent pas encore leur référence dans la base de données

        //Lecture de la liste des boîtes et création des boîtes (pour l'instant vides)
        InputStream instream = null;
        String nameFrigo = refrigerateur.getName();
        refrigerateur = new Refrigerateur(nameFrigo);//Réinitialise l'ensemble du réfrigérateur (pour tenir compte d'éventuelles modif)
        try {
            instream = openFileInput(nameFrigo + "Boxes.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);

            while(sc.hasNextLine() == true){//On recrée la liste des boites et la liste des noms des boîtes

                String refBdd = sc.nextLine();
                String name = sc.nextLine();
                String type = sc.nextLine();

                Box box = new Box(refBdd,name,type);
                refrigerateur.getBoxes().add(box);
            }
            
        } catch (FileNotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement boites", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        //On connecte la Bdd et pr chaque boîte on remplit la liste des aliments
        try{
            int nbBoites = refrigerateur.getBoxes().size();
            for (int j = 0; j <nbBoites;j++){
                String refBdd = refrigerateur.getBoxes().get(j).getReferenceBdd();
                ArrayList<String> listeAliments = refrigerateur.getBoxes().get(j).getListeAliments();
                //ON SE CONNECTE EN UTILISANT LA REFERENCE...
                //ON REMPLIT LA LISTE

                //Toast temporaire
                Toast.makeText(getApplicationContext(), "Pas encore de connexion à la bdd",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur de connexion à la base de données", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
