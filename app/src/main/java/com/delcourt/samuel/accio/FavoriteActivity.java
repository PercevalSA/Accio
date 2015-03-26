package com.delcourt.samuel.accio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.interaction.BDD;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends ActionBarActivity {

    static ArrayList<Aliment> listeAlimentFavoris;
    private ArrayList<Integer> numerosBoitesAConnecter = new ArrayList<>();

    public static String refBdd;
    static ArrayList<String> listeMarqueAliment;
    static ArrayList<String> listeNomAliment;
    static ArrayList<String> listeBoiteID;
    static ArrayList<String> listeFavoris;
    public static Box boite;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        listeNomAliment = new ArrayList<>();
        listeBoiteID = new ArrayList<>();
        listeMarqueAliment = new ArrayList<>();
        listeFavoris = new ArrayList<>();

        chargeFavoris();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
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
                // on mettra la mÃ©thode openSettings() quand elle sera cree
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

    public void chargeFavoris(){
        listeAlimentFavoris=new ArrayList<>();
        for(int i=0; i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){//On charge toutes les boîtes pas encore chargées
            if(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getConnectedBdd()==false){
                numerosBoitesAConnecter.add(i);
            }
            else{
                for(int j=0;j<RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().size();j++){
                    Aliment aliment = RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().get(j);
                    if(aliment.isAlimentFavori()==true){
                        listeAlimentFavoris.add(aliment);
                    }
                }
            }
        }
        afficheFavoris();//Affiche immédiatement les aliments des boîtes déjà chargées
        //On lance les connexions aux bdd successives :
        if(numerosBoitesAConnecter.size()!=0){
            boite=RefrigerateurActivity.refrigerateur.getBoxes().get(numerosBoitesAConnecter.get(0));
            refBdd=boite.getReferenceBdd();
            TextView textElement = (TextView) findViewById(R.id.message_chargement_favoris);
            textElement.setText("Chargement des aliments de la boîte "+boite.getName());
            new BDDFavorite().execute();
        }
    }

    class BDDFavorite extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            FavoriteActivity.listeBoiteID=new ArrayList<>();
            FavoriteActivity.listeNomAliment=new ArrayList<>();
            FavoriteActivity.listeFavoris=new ArrayList<>();

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/alimrecup.php?boiteid="+ refBdd);
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
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);

                    //Met les donnÃ©es ds la liste Ã  afficher
                    // Ici pas besoin d'afficher les données

                    result += "\n\t" + array.getString(i);
                    FavoriteActivity.listeBoiteID.add(json_data.getString(0));
                    FavoriteActivity.listeNomAliment.add(json_data.getString(1));
                    FavoriteActivity.listeFavoris.add(json_data.getString(7));


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {


            int nbAliment = listeNomAliment.size();
            for(int k =0; k < nbAliment; k++){

                String nom = listeNomAliment.get(k);
                String marque = null;
                boolean favori;
                ArrayList<String> historique = new ArrayList<>();
                String alimID = listeBoiteID.get(k);
                //marque = listeMarqueAliment.get(k);
                if ( listeFavoris.get(k).compareTo("0")==0){favori = false;}
                else {favori = true;}

                Aliment aliment = new Aliment(nom,marque, favori, historique,boite.getName(),alimID,boite.getType());
                boite.getListeAliments().add(aliment);

                if (aliment.isAlimentFavori()== true){
                    listeAlimentFavoris.add(aliment);
                }
            }

            //Affichage des aliments

            int sizeListAliments = listeAlimentFavoris.size();

            if(sizeListAliments==0){
                TextView textElement = (TextView) findViewById(R.id.resultat);
                textElement.setText("Il n'y a aucun aliment favori dans ce réfrigérateur");
            }
            else{
                boite.setConnectedBdd(true);//On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd

                TextView textElement = (TextView) findViewById(R.id.resultat);
                textElement.setText(" ");

                // Get the reference of listViewFrigos (pour l'affichage de la liste)
                final ListView listViewAliments=(ListView)findViewById(R.id.listeViewListeAliments1);

                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;

                for (int i =0;i<sizeListAliments;i++){
                    //on insère la référence aux éléments à afficher
                    map = new HashMap<String, String>();
                    map.put("aliment", listeAlimentFavoris.get(i).getAlimentName());
                    String type = listeAlimentFavoris.get(i).getType();
                    if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
                    else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
                    else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
                    else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
                    else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
                    else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
                    else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
                        map.put("img", String.valueOf(R.drawable.ic_launcher));
                    }

                    //enfin on ajoute cette hashMap dans la arrayList
                    listItem.add(map);
                }

                //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
                SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.affichage_aliments,
                        new String[] {"aliment","img"}, new int[] {R.id.nom_aliment_affiche,R.id.imgAlim});

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

            TextView textElement = (TextView) findViewById(R.id.message_chargement_favoris);
            textElement.setText("");

            //Si il reste des boîtes à connecter, on les connecte.
            numerosBoitesAConnecter.remove(0);
            if(numerosBoitesAConnecter.size()!=0){
                boite=RefrigerateurActivity.refrigerateur.getBoxes().get(numerosBoitesAConnecter.get(0));
                refBdd=boite.getReferenceBdd();

                textElement.setText("Chargement des aliments de la boîte "+boite.getName());

                new BDDFavorite().execute();
            }

        }
    }

    public void sendMessageAlimentSelected(View view, int i){}

    public void afficheFavoris() {
        int sizeListAliments = listeAlimentFavoris.size();

        if (sizeListAliments == 0) {
            TextView textElement = (TextView) findViewById(R.id.resultat);
            textElement.setText("Il n'y a aucun aliment favori dans ce réfrigérateur");


        } else {

            TextView textElement = (TextView) findViewById(R.id.resultat);
            textElement.setText(" ");

            // Get the reference of listViewFrigos (pour l'affichage de la liste)
            final ListView listViewAliments = (ListView) findViewById(R.id.listeViewListeAliments1);

            //Création de la ArrayList qui nous permettra de remplir la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

            HashMap<String, String> map;

            for (int i = 0; i < sizeListAliments; i++) {
                //on insère la référence aux éléments à afficher
                map = new HashMap<String, String>();
                map.put("aliment", listeAlimentFavoris.get(i).getAlimentName());
                String type = listeAlimentFavoris.get(i).getType();
                if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
                else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
                else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
                else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
                else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
                else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
                else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
                    map.put("img", String.valueOf(R.drawable.ic_launcher));
                }

                //enfin on ajoute cette hashMap dans la arrayList
                listItem.add(map);
            }

            //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
            SimpleAdapter mSchedule = new SimpleAdapter(getApplicationContext(), listItem, R.layout.affichage_aliments,
                    new String[]{"aliment", "img"}, new int[]{R.id.nom_aliment_affiche, R.id.imgAlim});

            //On attribue à notre listView l'adapter que l'on vient de créer
            listViewAliments.setAdapter(mSchedule);


            //register onClickListener to handle click events on each item
            listViewAliments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // argument position gives the index of item which is clicked

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    int indexBox = position;
                    sendMessageAlimentSelected(view, indexBox);
                }
            });
        }
    }

}