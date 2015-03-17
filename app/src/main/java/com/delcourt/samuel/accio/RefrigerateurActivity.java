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

    public static Refrigerateur refrigerateur;
    //static ArrayList<String> listeAlimentsAffichage; Utile lorsqu'on a besoin d'afficher, mais pas ici
    static ArrayList<String> listeNomAliment;
    static ArrayList<String> listeMarqueAliment;
    public String refBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerateur);
        listeNomAliment = new ArrayList<>();

        //On récupère toutes les infos du frigo en accédant à la mémoire de l'appli(fichiers textes)
        boolean chargementReussi = chargementRéfrigerateur();
        if (chargementReussi == false) {//si le chargement du frigo ou des boîtes a échoué, on affiche un message
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
            sc.close();
            chargementReussi = true;

        } catch (FileNotFoundException e) {
            chargementReussi = false;
        }
        return chargementReussi;
    }


    class Recupalim extends AsyncTask<String, Void, String> {

       // ArrayList<String> listAffich = new ArrayList<>(); Pas besoin on affiche pas


        protected String doInBackground(String... urls) {

            String result = "";
            String resultat = "";

            //listAffich = new ArrayList<>();

            InputStream is = null;

            // aliment recherchÃ©
            //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("nomCategorie", "Legume"));
            //ArrayList<String> donnees = new ArrayList<String>();

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://137.194.22.176/pact/alimrecup.php?boiteid=3");
                //httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la rÃ©ponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaÃ®ne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                //Log.i("lol", "COUCOU: "+ object.toString());
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);

                    //Met les donnÃ©es ds la liste Ã  afficher
                    // Ici pas besoin d'afficher les données
                    RefrigerateurActivity.listeNomAliment.add(json_data.getString(1));
                    result += "\n\t" + array.getString(i);


                   // resultat += "\n\t" + "ID: " + json_data.getInt(0) + ", Nom: " + json_data.getString(1) + ", Catégorie: " + json_data.getString(2);
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {

           /* Pas bsoin car normalement pas d'affichage quand on récupère les trucs de la BDD.
            // Permet d'afficher le result dans l'appli malgré les erreurs.
            TextView textElement = (TextView) findViewById(R.id.resultat);
            textElement.setText(" ");

            ListView listAffichage = (ListView) findViewById(R.id.listeViewListeAliments1);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listeAlimentsAffichage);
            listAffichage.setAdapter(arrayAdapter);

            */

        }
    }


    public boolean connectionBDD() {//On connecte la Bdd et pr chaque boîte on remplit la liste des aliments et celle des favoris
        boolean retour;
        try {
            int nbBoites = refrigerateur.getBoxes().size();
            for (int j = 0; j < nbBoites; j++) {
                refBdd = refrigerateur.getBoxes().get(j).getReferenceBdd();

                // A COMPLETER
                new Recupalim().execute();

                int nbAliment = listeNomAliment.size();
                for(int k =0; k < nbAliment; k++){

                    String nom = null;
                    String marque = null;
                    boolean favori = false;
                    ArrayList<String> historique = new ArrayList<>();

                    nom = listeNomAliment.get(k);

                    Toast toast = Toast.makeText(getApplicationContext(), nom, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    //marque = listeMarqueAliment.get(k);
                    // !!!!!!! CONNECTION BDD !!!!!!
                    //On se connecte à la bdd et on récupère les infos : nom, favori (mettre true ou false), marque, on crée la liste historique

                    Aliment aliment = new Aliment(nom,marque, favori, historique);
                    refrigerateur.getBoxes().get(j).getListeAliments().add(aliment);
                }


            }
            refrigerateur.setConnectionBdd(true);//Permet au reste de l'appli que la connection à la base de données a bien eu lieu
            retour = true;

        } catch (Exception e) {
            refrigerateur.setConnectionBdd(false);//Permet au reste de l'appli de savoir que la connection à la bdd n'a pas eu lieu
            retour = false;
        }
        return retour;
    }

    public void sendMessageOptionsFrigo(View view){
        Intent intent = new Intent(this, FrigoOptionsActivity.class);
        startActivity(intent);
    }
}
