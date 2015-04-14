package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.*;


import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.create_new_object_activities.NewBoxActivity;
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


    private static int boxIndex;//permet de récipérer toutes les infos de la boîte (à partir de ListeBoitesActivity)
    private static ArrayList<String> listeNomAliment;
    private static ArrayList<String> listeMarqueAliment;
    private static ArrayList<String> listeBoiteID;
    private static ArrayList<String> listeFavoris;
    private static ArrayList<String> listeHistoriqueAliment;
    private String refBdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_box);

            listeNomAliment = new ArrayList<>();
            listeBoiteID = new ArrayList<>();
            listeMarqueAliment = new ArrayList<>();
            listeFavoris = new ArrayList<>();
            listeHistoriqueAliment = new ArrayList<>();
            //Récupère les informations de la boîte pour les afficher :
            TextView textElement = (TextView) findViewById(R.id.boxName_BoxActivity);
            textElement.setText(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getName());

            TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
            textElement2.setText("(Réfrigérateur : " + ListeBoitesActivity.getRefrigerateur().getName() + ")");

            afficheImage();

            refBdd = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getReferenceBdd();

            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getConnectedBdd()== false){//On se connecte à la BDD et on affiche les aliments
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).reinitialiseListeAliments();//On va réécrire sur cette liste, on efface donc le contenu précédent
                new RecupalimBDD().execute();
            } else{
                if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().size()==0){//Si la liste est vide, on affiche un message
                    TextView textElement3 = (TextView) findViewById(R.id.message_BoxActivity);
                    textElement3.setText("Il n'y a aucun aliment dans cette boîte pour l'instant");

                    TextView textElement4 = (TextView) findViewById(R.id.resultat2);
                    textElement4.setText(" ");
                }
                else{//On affiche les aliments déjà contenus dans la boîte
                    afficheListeAliments();
                }
            }
        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
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
        switch (item.getItemId()) {
            case R.id.action_refresh:
                actualiseBox();
                return true;
            case R.id.action_rename:
                optionsBox();
                return true;
            case R.id.action_delete:
                MessageDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public static void setBoxIndex(int index){boxIndex=index;}

    public void sendMessageAlimentSelected(View view, int index){
        AlimentActivity.setBoxIndex(boxIndex);
        AlimentActivity.setAlimentIndex(index);
        Intent intent = new Intent(this, AlimentActivity.class);
        startActivity(intent);
    }



    public void optionsBox(){
        BoxOptionsActivity.setBoxIndex(boxIndex);
        Intent intent = new Intent(this,BoxOptionsActivity.class);
        startActivity(intent);
    }

    class RecupalimBDD extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/alimrecup.php?boiteid="+refBdd);
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
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);

                    //Met les donnÃ©es ds la liste Ã  afficher
                    // Ici pas besoin d'afficher les données

                    result += "\n\t" + array.getString(i);
                    BoxActivity.listeBoiteID.add(json_data.getString(0));
                    BoxActivity.listeNomAliment.add(json_data.getString(1));
                    BoxActivity.listeHistoriqueAliment.add(json_data.getString(4));
                    BoxActivity.listeFavoris.add(json_data.getString(7));
                    BoxActivity.listeMarqueAliment.add(json_data.getString(9));


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
                String marque = listeMarqueAliment.get(k);
                boolean favori;
                String historique = listeHistoriqueAliment.get(k);
                String alimID = listeBoiteID.get(k);
                //marque = listeMarqueAliment.get(k);
                if ( listeFavoris.get(k).compareTo("0")==0){favori = false;}
                else {favori = true;}

                Aliment aliment = new Aliment(nom,marque, favori, historique,
                        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getName(),
                        alimID,ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getType());
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().add(aliment);
            }

            //Affichage des aliments

            int sizeListAliments = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().size();
            //Toast.makeText(getApplicationContext(), "Nombre d'aliments dans listeNomAliment : "+listeNomAliment.size(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Nombre d'aliments à afficher : "+sizeListAliments,Toast.LENGTH_SHORT).show();


            if(sizeListAliments==0){
                TextView textElement = (TextView) findViewById(R.id.message_BoxActivity);
                textElement.setText("Il n'y a aucun aliment dans cette boîte pour l'instant");

                TextView textElement2 = (TextView) findViewById(R.id.resultat2);
                textElement2.setText(" ");

                //PARTIE TEMPORAIRE : on affiche un aliment d'exemple
                String historique = "0000-00-00";
                Aliment aliment = new Aliment("Aliment exemple temporaire","marque", true, historique,
                        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getName(),"0","Fruits");
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().add(aliment);

                final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);
                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;
                map = new HashMap<String, String>();
                map.put("aliment",
                        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(0).getAlimentName());
                map.put("img", String.valueOf(R.drawable.erreur));
                SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.listview_box,
                        new String[] {"aliment","img"}, new int[] {R.id.nom_aliment_affiche,R.id.imgAlim});
                listItem.add(map);
                listViewAliments.setAdapter(mSchedule);
                listViewAliments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    // argument position gives the index of item which is clicked

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                        int indexBox = position;
                        sendMessageAlimentSelected(view, indexBox);
                    }
                });

                //FIN PARTIE TEMPORAIRE
            }
            else{
                //On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).setConnectedBdd(true);

                TextView textElement = (TextView) findViewById(R.id.resultat2);
                textElement.setText(" ");
                /*ListView listAffichage=(ListView)findViewById(R.id.liste_aliments);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, listeNomAliment);
                listAffichage.setAdapter(arrayAdapter);*/


                // Get the reference of listViewFrigos (pour l'affichage de la liste)
                final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);

                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;

                for (int i =0;i<sizeListAliments;i++){
                    //on insère la référence aux éléments à afficher
                    map = new HashMap<String, String>();
                    map.put("aliment",
                            ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(i).getAlimentName());
                    if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(i).isAlimentFavori()==true){
                        map.put("img", String.valueOf(R.drawable.fav));
                    } else {map.put("img", String.valueOf(R.drawable.favn));}
                    //enfin on ajoute cette hashMap dans la arrayList
                    listItem.add(map);
                }

                //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
                SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.listview_box,
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
    }

    public void afficheImage(){
        //Affiche l'image du type de la boîte
        //METTRE LES BONNES IMAGES !
        String type = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getType();
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

    public void afficheListeAliments(){
        TextView textElement = (TextView) findViewById(R.id.resultat2);
        textElement.setText(" ");
                /*ListView listAffichage=(ListView)findViewById(R.id.liste_aliments);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, listeNomAliment);
                listAffichage.setAdapter(arrayAdapter);*/


        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);

        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

        HashMap<String, String> map;

        for (int i =0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().size();i++){
            //on insère la référence aux éléments à afficher
            map = new HashMap<String, String>();
            map.put("aliment", ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(i).getAlimentName());
            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(i).isAlimentFavori()==true){
                map.put("img", String.valueOf(R.drawable.fav));
            } else {map.put("img", String.valueOf(R.drawable.favn));}
            //enfin on ajoute cette hashMap dans la arrayList
            listItem.add(map);

        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.listview_box,
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


    public void actualiseBox(){
        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).setConnectedBdd(false);
        Intent intent = new Intent(this,BoxActivity.class);
        startActivity(intent);
    }

    public void MessageDelete(){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(BoxActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer la boite " +
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getName()+" ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void delete(){
        String nameFrigo = ListeBoitesActivity.getRefrigerateur().getName();
        String nameBoite = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getName();
        refBdd = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getReferenceBdd();

        new DeleteBoite().execute();

        //On supprime la boîte dans la liste dynamique :
        for(int j =0;j<ListeBoitesActivity.getRefrigerateur().getBoxes().size();j++){
            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(j).getName().compareTo(nameBoite)==0){
                ListeBoitesActivity.getRefrigerateur().getBoxes().remove(j);
            }
        }
        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
                out2.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getReferenceBdd());
                out2.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getName());
                out2.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getType());
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,ListeBoitesActivity.class);
        startActivity(intent);
    }

    class DeleteBoite extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/deleteboite.php?boiteID=" + refBdd);
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
                JSONObject object = new JSONObject(result);
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    //Met les donnÃ©es ds la liste Ã  afficher
                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }

    }

}
