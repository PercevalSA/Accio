package com.delcourt.samuel.accio;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends ActionBarActivity {

    private static ArrayList<Aliment> listeAlimentFavoris;
    private ArrayList<Integer> numerosBoitesAConnecter = new ArrayList<>();
    private String refBdd;
    private ArrayList<String> namesBoitesNonConnection;

    //Attributs static : utiles dans la classe se connectant à la BDD
    private static ArrayList<String> listeMarqueAliment;
    private static ArrayList<String> listeNomAliment;
    private static ArrayList<String> listeBoiteID;
    private static ArrayList<String> listeFavoris;
    private static Box boite;
    private static ArrayList<String> listeHistoriqueAliment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favoris);

            namesBoitesNonConnection = new ArrayList<>();

            listeNomAliment = new ArrayList<>();
            listeBoiteID = new ArrayList<>();
            listeMarqueAliment = new ArrayList<>();
            listeFavoris = new ArrayList<>();
            listeHistoriqueAliment = new ArrayList<>();

            chargeFavoris();

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
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
                case R.id.action_refresh:
                    actualise();
                    return true;
                default:

                    return super.onOptionsItemSelected(item);

        }
    }



    public void chargeFavoris(){
        listeAlimentFavoris=new ArrayList<>();
        for(int i=0; i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){//On prend les références des boîtes pas encore chargées
            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getConnectedBdd()==false){
                numerosBoitesAConnecter.add(i);
            }
            else{
                for(int j=0;j<ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().size();j++){
                    Aliment aliment = ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().get(j);
                    if(aliment.isAlimentFavori()==true){
                        listeAlimentFavoris.add(aliment);
                    }
                }
            }
        }
        afficheFavoris();//Affiche immédiatement les aliments des boîtes déjà chargées
        //On lance les connexions aux bdd successives :
        if(numerosBoitesAConnecter.size()!=0){
            boite=ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
            refBdd=boite.getReferenceBdd();
            TextView textElement = (TextView) findViewById(R.id.message_chargement_favoris);
            textElement.setText("Chargement des aliments de la boîte "+boite.getName());
            new BDDFavorite().execute();
        }
    }

    class BDDFavorite extends AsyncTask<String, Void, String> {

        private boolean connectionSuccessful=true;

        public void setConnectionSuccessful(boolean b){connectionSuccessful=b;}

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
                setConnectionSuccessful(false);
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
                setConnectionSuccessful(false);
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
                    FavoriteActivity.listeHistoriqueAliment.add(json_data.getString(4));
                    FavoriteActivity.listeFavoris.add(json_data.getString(7));
                    FavoriteActivity.listeMarqueAliment.add(json_data.getString(9));


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
                //Ne lève une exception que si la boîte est vide => Pas un problème !
            }
            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {

            //On enlève le message précédent s'il y en avait un
            TextView textElement0 = (TextView) findViewById(R.id.resultat);
            textElement0.setText("");

            if(connectionSuccessful==false){
                namesBoitesNonConnection.add(boite.getName());
                TextView textElement = (TextView) findViewById(R.id.text_non_connect_favoris);
                textElement.setText("Problème de connexion");
                ImageView image = (ImageView) findViewById(R.id.im_non_connect);
                image.setImageResource(R.drawable.erreur);

            } else { //La connexion à la base de données a fonctionné. On crée la liste des aliments, on affiche les favoris

                //On supprime réinitialise la liste des aliments de la boîte pour la réécrire :
                boite.getListeAliments().clear();

                boite.setConnectedBdd(true);//On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd

                int nbAliment = listeNomAliment.size();
                for(int k =0; k < nbAliment; k++){

                    String nom = listeNomAliment.get(k);
                    String marque = listeMarqueAliment.get(k);
                    boolean favori;
                    String historique = listeHistoriqueAliment.get(k);
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
            }

            //Dans tous les cas, on enlève le message indiquant le chargement, et on essaie de se connecter aux autres boîtes
            TextView textElement = (TextView) findViewById(R.id.message_chargement_favoris);
            textElement.setText("");

            //Si il reste des boîtes à connecter, on les connecte.
            numerosBoitesAConnecter.remove(0);
            if(numerosBoitesAConnecter.size()!=0) {
                boite = ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
                refBdd = boite.getReferenceBdd();

                textElement.setText("Chargement des aliments de la boîte " + boite.getName());

                new BDDFavorite().execute();
            } else {
                if(namesBoitesNonConnection.size()==0){//Si toutes les connexions ont réussi
                    Toast.makeText(getApplicationContext(), "Actualisation réussie",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void sendMessageAlimentSelected(View view, int index){
        Aliment aliment = listeAlimentFavoris.get(index);
        String alimentName = aliment.getAlimentName();
        String boxName=aliment.getBoxName();
        int boxIndex=-1;
        int alimentIndex=-1;

        //On récupère l'index de la boîte :
        for(int i=0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
            if((ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getName()).compareTo(boxName)==0){
                boxIndex=i;
            }
        }

        //On récupère l'index de l'aliment :
        for(int i=0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().size();i++){
            if((ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(i).getAlimentName())
                    .compareTo(alimentName)==0){
                alimentIndex=i;
            }
        }

        if(boxIndex<0 || alimentIndex<0){
            Toast.makeText(getApplicationContext(), "Problème... \n boxIndex = "+boxIndex+"\n alimentIndex = "+alimentIndex,Toast.LENGTH_SHORT).show();
        }else{
            BoxActivity.setBoxIndex(boxIndex);
            AlimentActivity.setBoxIndex(boxIndex);
            AlimentActivity.setAlimentIndex(alimentIndex);

            Intent intent = new Intent(this, AlimentActivity.class);
            startActivity(intent);
        }


    }

    public void afficheFavoris() {

        if(namesBoitesNonConnection.size()>0){//Il y a eu des pb de connexion à la BDD

            TextView textElement = (TextView) findViewById(R.id.text_non_connect_favoris);
            textElement.setText("Problème de connexion");
            ImageView image = (ImageView) findViewById(R.id.im_non_connect);
            image.setImageResource(R.drawable.erreur);

        } else { //Cas où toutes les boites ont bien été chargées :
            int sizeListAliments = listeAlimentFavoris.size();

            if (sizeListAliments == 0 && numerosBoitesAConnecter.size()==0) {
            //N'a lieu que si toutes les boîtes ont été chargées et le frigo ne possède aucun aliment favori
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

    public void actualise(){
        for(int i=0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
            ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).setConnectedBdd(false);
        }
        Intent intent = new Intent(this,FavoriteActivity.class);
        startActivity(intent);
    }

}