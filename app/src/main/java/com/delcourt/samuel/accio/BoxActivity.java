package com.delcourt.samuel.accio;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;


import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.create_new_object_activities.NewFrigoActivity;
import com.delcourt.samuel.accio.options_activities.BoxOptionsActivity;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;
import android.os.AsyncTask;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class BoxActivity extends ActionBarActivity {

    public static Box boite;
    static ArrayList<String> listeNomAliment;
    static ArrayList<String> listeMarqueAliment;
    public String refBdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        listeNomAliment = new ArrayList<>();

        //Récupère les informations de la boîte pour les afficher :
        TextView textElement = (TextView) findViewById(R.id.boxName_BoxActivity);
        textElement.setText(boite.getName() + " " + boite.getReferenceBdd());

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
        textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

        afficheImage();

        recupAliment();

        afficheAliments();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void afficheAliments(){
        int sizeListAliments = boite.getListeAliments().size();
        Toast.makeText(getApplicationContext(), "Nombre d'aliments dans listeNomAliment : "+listeNomAliment.size(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Nombre d'aliments à afficher : "+sizeListAliments,Toast.LENGTH_SHORT).show();


            if(sizeListAliments==0){
                TextView textElement = (TextView) findViewById(R.id.message_BoxActivity);
                textElement.setText("Il n'y a aucun aliment dans cette boîte pour l'instant");
            }
            else{
                // Get the reference of listViewFrigos (pour l'affichage de la liste)
                final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);

                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;

                for (int i =0;i<sizeListAliments;i++){
                    //on insère la référence aux éléments à afficher
                    map = new HashMap<String, String>();
                    map.put("aliment", boite.getListeAliments().get(i).getAlimentName());

                    //enfin on ajoute cette hashMap dans la arrayList
                    listItem.add(map);

                }

                //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
                SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichage_liste_boites,
                        new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});

                //On attribue à notre listView l'adapter que l'on vient de créer
                listViewAliments.setAdapter(mSchedule);


                //register onClickListener to handle click events on each item
                listViewAliments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    // argument position gives the index of item which is clicked

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                        int indexBox = position;
                        sendMessageAlimentSelected(view, indexBox);
                    }
                });
            }

    }

    public void sendMessageAlimentSelected(View view, int index){

    }

    public void sendMessageOptionsBox(View view){
        Intent intent = new Intent(this,BoxOptionsActivity.class);
        startActivity(intent);
    }


    class RecupalimBDD extends AsyncTask<String, Void, String> {

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
                HttpGet httpget = new HttpGet("http://137.194.20.223/pact/alimrecup.php?boiteid="+refBdd);
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
                    //RefrigerateurActivity.listeNomAliment.add(json_data.getString(1));
                    result += "\n\t" + array.getString(i);
                    BoxActivity.listeNomAliment.add(array.getString(i));

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









    public boolean recupAliment() {//On connecte la Bdd et pr chaque boîte on remplit la liste des aliments et celle des favoris
        boolean retour;
        try {

                refBdd = boite.getReferenceBdd();
                    Toast toast3 = Toast.makeText(getApplicationContext(), refBdd, Toast.LENGTH_LONG);
                    toast3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast3.show();
                // A COMPLETER
                new RecupalimBDD().execute();

                int nbAliment = listeNomAliment.size();

                for(int k =0; k < nbAliment; k++){

                    String nom = listeNomAliment.get(k);;
                    String marque = null;
                    boolean favori = false;
                    ArrayList<String> historique = new ArrayList<>();

                    //marque = listeMarqueAliment.get(k);
                    // !!!!!!! CONNECTION BDD !!!!!!
                    //On se connecte à la bdd et on récupère les infos : nom, favori (mettre true ou false), marque, on crée la liste historique

                    Aliment aliment = new Aliment(nom,marque, favori, historique);
                    boite.getListeAliments().add(aliment);
                }

            RefrigerateurActivity.refrigerateur.setConnectionBdd(true);//Permet au reste de l'appli que la connection à la base de données a bien eu lieu
            retour = true;

        } catch (Exception e) {
            Log.e("log_tag", "Erreur dans la récupération des aliments : " + e.toString());
            RefrigerateurActivity.refrigerateur.setConnectionBdd(true);//Permet au reste de l'appli de savoir que la connection à la bdd n'a pas eu lieu
            retour = true;
        }
        return retour;
   }

    public void afficheImage(){
        //Affiche l'image du type de la boîte
        //METTRE LES BONNES IMAGES !
        String type = boite.getType();
        ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_boxActivity);
        if (type.compareTo("Fruits")==0){ textElement3.setImageResource(R.drawable.ic_fruit);}
        else if (type.compareTo("Légumes")==0){textElement3.setImageResource(R.drawable.ic_legume);}
        else if (type.compareTo("Produits laitiers")==0){textElement3.setImageResource(R.drawable.ic_produit_laitier);}
        else if (type.compareTo("Poisson")==0){textElement3.setImageResource(R.drawable.ic_poisson);}
        else if (type.compareTo("Viande")==0){textElement3.setImageResource(R.drawable.ic_viande);}
        else if (type.compareTo("Sauces et condiments")==0){textElement3.setImageResource(R.drawable.ic_condiment);}
        else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
            textElement3.setImageResource(R.drawable.ic_launcher);
            //On affiche un toast
            Toast.makeText(getApplicationContext(), "Le type de la boîte n'a pas été reconnu",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
