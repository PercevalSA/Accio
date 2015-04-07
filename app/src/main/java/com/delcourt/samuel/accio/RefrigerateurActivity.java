package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class RefrigerateurActivity extends ActionBarActivity {
    //Cette classe est la classe centrale. Elle contient l'objet static Refrigerateur refrigerateur, qui lui contient à son tour toutes les
    // infos et caractéristiques du réfrigérateur auquel on s'intéresse. Ainsi, lorsqu'une autre classe a besoin d'infos sur les boites
    // ou leurs aliments etc., elle les cherche directement ici.

    //private static Refrigerateur refrigerateur;
    //Navigation drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_refrigerateur);
            //navigation drawer
            mDrawerList = (ListView)findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            /*if(refrigerateur.isRefrigerateurCreated()==false){//Si le frigo n'a pas encore été créé, on le crée.

                boolean chargementReussi = creationRéfrigerateur();

                if (chargementReussi == false) {//si le chargement du frigo ou des boîtes a échoué, on affiche un message
                    Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement du frigo (liste des boîtes Accio inaccessible)", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }

            TextView textElement = (TextView) findViewById(R.id.frigoNameMenu);
            textElement.setText("Réfrigérateur : " + refrigerateur.getName());*/

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
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
            case R.id.action_rename:
                optionsFrigo();
                return true;
            case R.id.action_delete:

                return true;

            default:

                // Activate the navigation drawer toggle
                if (mDrawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDrawerItems() {
        String[] osArray = { "Contenu", "Recette", "Favoris", "Ajout","Accueil" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);
            }
        });

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void openSearch() {
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

   /* public static Refrigerateur getRefrigerateur(){return refrigerateur;}

    public static void setRefrigerateur(Refrigerateur frigo){refrigerateur=frigo;}*/

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

   /* public boolean creationRéfrigerateur() {

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
    }*/

        public void optionsFrigo(){
        Intent intent = new Intent(this, FrigoOptionsActivity.class);
        startActivity(intent);
    }

    /*public void messageDeleteFrigo(){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(RefrigerateurActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer le réfrigérateur " + RefrigerateurActivity.getRefrigerateur().getName()+
                " ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void delete(){
        String nameFrigo = RefrigerateurActivity.getRefrigerateur().getName();
        int index = AccueilActivity.getListeFrigosNames().indexOf(nameFrigo);
        AccueilActivity.getListeFrigosNames().remove(index);

        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<AccueilActivity.getListeFrigosNames().size();i++){
                out2.println(AccueilActivity.getListeFrigosNames().get(i));
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste frigos", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,AccueilActivity.class);
        startActivity(intent);
    }*/

    public void sendMessageAjoutAliment(View view){
        Intent intent = new Intent(this, AjoutAlimentActivity.class);
        startActivity(intent);
    }

    public void itemSelected(int position){
        //Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();
        if(position ==0){
            Intent intent = new Intent(this, ListeBoitesActivity.class);
            startActivity(intent);
        }

       if(position ==1){
           Intent intent = new Intent(this, MenuRecettesActivity.class);
           startActivity(intent);
       }

        if(position ==2){
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }

        if(position ==3){
            Intent intent = new Intent(this, AjoutAlimentActivity.class);
            startActivity(intent);
        }

        if(position ==4){
            Intent intent = new Intent(this, AccueilActivity.class);
            startActivity(intent);
        }
    }

   }


