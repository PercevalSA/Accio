package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.options_activities.FrigoOptionsActivity;
import com.delcourt.samuel.accio.recettes.MenuRecettesActivity;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class RefrigerateurActivity extends ActionBarActivity {

    private static Refrigerateur refrigerateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_refrigerateur);

            if(refrigerateur.isRefrigerateurCreated()==false){//Si le frigo n'a pas encore été créé, on le crée.

                boolean chargementReussi = creationRéfrigerateur();

                if (chargementReussi == false) {//si le chargement du frigo ou des boîtes a échoué, on affiche un message
                    Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement du frigo (liste des boîtes Accio inaccessible)", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }

            TextView textElement = (TextView) findViewById(R.id.frigoNameMenu);
            textElement.setText("Réfrigérateur : " + refrigerateur.getName());

        } catch (Exception e){
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
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

    public static Refrigerateur getRefrigerateur(){return refrigerateur;}

    public static void setRefrigerateur(Refrigerateur frigo){refrigerateur=frigo;}

    public void sendMessageContent(View view) {
        Intent intent = new Intent(this, ListeBoitesActivity.class);
        startActivity(intent);
    }

    public void sendMessageRecipes(View view) {
        Intent intent = new Intent(this, MenuRecettesActivity.class);
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

    public boolean creationRéfrigerateur() {

        //ON RECREE LE REFRIGERATEUR AVEC SES BOITES
        //Le nom du réfrigérateur a été spécifié lors du choix du frigo. On récupère maintenant la liste des boîtes

        // ATTENTION : les boites ne connaissent pas encore leur référence dans la base de données

        //Lecture de la liste des boîtes et création des boîtes (pour l'instant vides)
        boolean creationReussie;
        InputStream instream = null;
        String nameFrigo = refrigerateur.getName();
        refrigerateur = new Refrigerateur(nameFrigo);//Réinitialise l'ensemble du réfrigérateur
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
            sc.close();
            creationReussie = true;
            refrigerateur.setRefrigerateurCreated();

        } catch (FileNotFoundException e) {
            creationReussie = false;
        }
        return creationReussie;
    }

    public void sendMessageOptionsFrigo(View view){
        Intent intent = new Intent(this, FrigoOptionsActivity.class);
        startActivity(intent);
    }


   }


