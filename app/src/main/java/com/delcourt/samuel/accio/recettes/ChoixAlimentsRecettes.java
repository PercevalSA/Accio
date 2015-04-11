package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.ListeBoitesActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.AlimentRecette;
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


public class ChoixAlimentsRecettes extends ActionBarActivity {

    private ArrayList<AlimentRecette> listeAlimentsProposes = new ArrayList<>();
    private ArrayList<Integer> numerosBoitesAConnecter = new ArrayList<>();

    private ArrayList<String> namesBoitesNonConnection;
    private Button boutonChercher;

    //Attributs utiles pour la connexion à la BDD
    private String refBdd;
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
            setContentView(R.layout.activity_choix_aliments_recettes);
            boutonChercher =  (Button) findViewById(R.id.bouton_ok_recette);
            listeNomAliment = new ArrayList<>();
            listeBoiteID = new ArrayList<>();
            listeMarqueAliment = new ArrayList<>();
            listeFavoris = new ArrayList<>();
            listeHistoriqueAliment = new ArrayList<>();

            EditText editText = (EditText) findViewById(R.id.edit_text_recette);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
//              getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    } else if (!hasFocus) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }
                }
            });
            editText.setFocusable(false);

            namesBoitesNonConnection = new ArrayList<>();

            createListeAlimentsProposes();

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_aliments_recettes, menu);
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

    public void createListeAlimentsProposes(){
        for(int i=0;i< ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){//On parcourt toutes les boîtes
            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getConnectedBdd()==true){
                //Si il y a déjà eu connection, on récupère les aliments
                for(int j=0;j<ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().size();j++){
                    AlimentRecette aliment = new AlimentRecette(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().get(j).getAlimentName(),
                            ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getType());
                    listeAlimentsProposes.add(aliment);
                }
            } else {//pas encore eu de connexion -> on prend l'indice de la boîte pour la charger ensuite
                numerosBoitesAConnecter.add(i);
            }

        }

        afficheListeAlimentsProposes();

        //On lance les connexions aux bdd successives :
        if(numerosBoitesAConnecter.size()!=0){
            boite=ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
            refBdd=boite.getReferenceBdd();
            TextView textElement = (TextView) findViewById(R.id.message_chargement_recettes);
            textElement.setText("Chargement des aliments de la boîte "+boite.getName());
            boutonChercher.setVisibility(View.INVISIBLE);
            new BDDRecettes().execute();
        }
    }

    public void afficheListeAlimentsProposes(){

        if(namesBoitesNonConnection.size()>0){//Il y a eu des pb de connexion à la BDD

            TextView textElement = (TextView) findViewById(R.id.message_recette);
            textElement.setText("Impossible de charger la liste des aliments de votre réfrigérateur");

            /*if(listeAlimentsProposes.size()==0 && numerosBoitesAConnecter.size()==0){
                //Juste pour l'exemple :
                AlimentRecette exemple = new AlimentRecette("banane","a");
                listeAlimentsProposes.add(exemple);

            }*/

        } else {//Cas où toutes les boites ont bien été chargées :


            // Get the reference of listViewFrigos (pour l'affichage de la liste)
            final ListView listViewBoxes=(ListView)findViewById(R.id.listeAlimentsProposes);

            //Création de la ArrayList qui nous permettra de remplir la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

            //On déclare la HashMap qui contiendra les informations pour un item
            HashMap<String, String> map;

            if(listeAlimentsProposes.size()==0 && numerosBoitesAConnecter.size()==0){
                //Juste pour l'exemple :
                AlimentRecette exemple = new AlimentRecette("banane","a");
                listeAlimentsProposes.add(exemple);
                afficheListeAlimentsProposes();

                TextView textElement = (TextView) findViewById(R.id.message_recette);
                textElement.setText("Il n'y a aucun aliment dans ce réfrigérateur");
            }


            for(int i=0;i<listeAlimentsProposes.size();i++){

                //on insère la référence aux éléments à afficher
                map = new HashMap<>();
                map.put("nom", listeAlimentsProposes.get(i).getName());

                String type = listeAlimentsProposes.get(i).getType();
                if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
                else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
                else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
                else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
                else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
                else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
                else {//Sinon (type non reconnu, ne devrait jamais arriver) - c'est le cas de l'aliment banane mis par défaut
                    map.put("img", String.valueOf(R.drawable.erreur));
                }

                if(listeAlimentsProposes.get(i).isSelected()) {
                    map.put("selected", String.valueOf(R.drawable.validate));
                } else {map.put("selected", String.valueOf(R.drawable.validaten));}

                //enfin on ajoute cette hashMap dans la arrayList
                listItem.add(map);
            }

            //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
            SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.liste_aliments_recette,
                    new String[] {"nom", "img", "selected"}, new int[] {R.id.nom_aliment_recette, R.id.img_aliment_recette, R.id.aliment_recette_selected});

            //On attribue à notre listView l'adapter que l'on vient de créer
            listViewBoxes.setAdapter(mSchedule);


            //register onClickListener to handle click events on each item
            listViewBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                // argument position gives the index of item which is clicked

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    boxSelected(position);
                }
            });
        }


    }

    public void boxSelected(int index){
        if(listeAlimentsProposes.get(index).isSelected()==false){
            listeAlimentsProposes.get(index).setSelected(true);
            afficheListeAlimentsProposes();
        }else{
            listeAlimentsProposes.get(index).setSelected(false);
            afficheListeAlimentsProposes();
        }
    }

    public void sendMessageChercher(View view){
        String aliments=prepareRechercheAliments();
        if (aliments.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas choisi d'aliments", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else{
            Intent intent = new Intent(this, OptionsRecettesActivity.class);
            OptionsRecettesActivity.setAliments(aliments);
            startActivity(intent);
        }

    }

    public String prepareRechercheAliments(){
        String aliments;
        ArrayList<String> alimentsSelected = new ArrayList<>();

        //Récupère ce qui a été coché
        EditText editText = (EditText) findViewById(R.id.edit_text_recette);
        String recup = editText.getText().toString();

        for(int i=0;i<listeAlimentsProposes.size();i++){
            if(listeAlimentsProposes.get(i).isSelected()){
                alimentsSelected.add(listeAlimentsProposes.get(i).getName());
            }
        }

        if(alimentsSelected.size()==0){//Lorsque l'on n'a rien sélectionné, ou juste un texte supplémentaire
            aliments=recup;
            //Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_SHORT).show();
        }

        else if(recup.length()==0 && alimentsSelected.size()!=0){//Un aliment a été sélectionné, sans texte supplémentaire
            aliments = recup;
            for(int i=0;i<alimentsSelected.size()-1;i++){
                aliments=aliments+alimentsSelected.get(i)+"-";
            }
            aliments=aliments+alimentsSelected.get(alimentsSelected.size()-1);//On ajoute le dernier
        }

        else{//Lorsque aliment sélectionné et texte supplémentaire
            aliments = recup;
            for(int j=0;j<alimentsSelected.size();j++){
                aliments=aliments+"-"+ alimentsSelected.get(j);
            }
        }

        return aliments;
    }

    class BDDRecettes extends AsyncTask<String, Void, String> {

        private boolean connectionSuccessful=true;

        public void setConnectionSuccessful(boolean b){connectionSuccessful=b;}

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            ChoixAlimentsRecettes.listeBoiteID=new ArrayList<>();
            ChoixAlimentsRecettes.listeNomAliment=new ArrayList<>();
            ChoixAlimentsRecettes.listeFavoris=new ArrayList<>();

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
                    ChoixAlimentsRecettes.listeBoiteID.add(json_data.getString(0));
                    ChoixAlimentsRecettes.listeNomAliment.add(json_data.getString(1));
                    ChoixAlimentsRecettes.listeHistoriqueAliment.add(json_data.getString(4));
                    ChoixAlimentsRecettes.listeFavoris.add(json_data.getString(7));
                    ChoixAlimentsRecettes.listeMarqueAliment.add(json_data.getString(9));


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
                setConnectionSuccessful(false);
            }
            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {

            //On supprime réinitialise la liste des aliments de la boîte pour la réécrire :
            boite.getListeAliments().clear();

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

                AlimentRecette alimentRecette = new AlimentRecette(nom,boite.getType());
                listeAlimentsProposes.add(alimentRecette);
            }

            if(connectionSuccessful==false){
                namesBoitesNonConnection.add(boite.getName());
                TextView textElement = (TextView) findViewById(R.id.message_recette);
                textElement.setText("Impossible de charger la liste des aliments de votre réfrigérateur");

            } else { //La connexion à la base de données a fonctionné. On affiche tous les aliments
                //Affichage des aliments

                int sizeListAliments = listeAlimentsProposes.size();

                if(sizeListAliments==0){
                    TextView textElement = (TextView) findViewById(R.id.message_recette);
                    textElement.setText("Il n'y a aucun aliment dans ce réfrigérateur");
                }
                else{
                    boite.setConnectedBdd(true);//On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd

                    TextView textElement = (TextView) findViewById(R.id.message_chargement_recettes);
                    textElement.setText(" ");

                    boutonChercher.setVisibility(View.VISIBLE);

                    // Get the reference of listViewFrigos (pour l'affichage de la liste)
                    final ListView listViewBoxes=(ListView)findViewById(R.id.listeAlimentsProposes);

                    //Création de la ArrayList qui nous permettra de remplir la listView
                    ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                    //On déclare la HashMap qui contiendra les informations pour un item
                    HashMap<String, String> map;

                    for(int i=0;i<listeAlimentsProposes.size();i++){

                        //on insère la référence aux éléments à afficher
                        map = new HashMap<>();
                        map.put("nom", listeAlimentsProposes.get(i).getName());

                        String type = listeAlimentsProposes.get(i).getType();
                        if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
                        else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
                        else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
                        else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
                        else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
                        else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
                        else {//Sinon (type non reconnu, ne devrait jamais arriver) - c'est le cas de l'aliment banane mis par défaut
                            map.put("img", String.valueOf(R.drawable.erreur));
                        }

                        if(listeAlimentsProposes.get(i).isSelected()) {
                            map.put("selected", String.valueOf(R.drawable.validate));
                        } else {map.put("selected", String.valueOf(R.drawable.validaten));}

                        //enfin on ajoute cette hashMap dans la arrayList
                        listItem.add(map);
                    }

                    //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
                    SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.liste_aliments_recette,
                            new String[] {"nom", "img", "selected"}, new int[] {R.id.nom_aliment_recette, R.id.img_aliment_recette, R.id.aliment_recette_selected});

                    //On attribue à notre listView l'adapter que l'on vient de créer
                    listViewBoxes.setAdapter(mSchedule);


                    //register onClickListener to handle click events on each item
                    listViewBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        // argument position gives the index of item which is clicked

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                            boxSelected(position);
                        }
                    });
                }
            }

            //La liaison avec la base de données est terminée : on enlève le message
            TextView textElement = (TextView) findViewById(R.id.message_chargement_recettes);
            textElement.setText("");

            boutonChercher.setVisibility(View.VISIBLE);

            //Si il reste des boîtes à connecter, on les connecte.
            if(numerosBoitesAConnecter.size()>0)numerosBoitesAConnecter.remove(0);
            if(numerosBoitesAConnecter.size()!=0){
                boite= ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
                refBdd=boite.getReferenceBdd();

                textElement.setText("Chargement des aliments de la boîte "+boite.getName());
                boutonChercher.setVisibility(View.INVISIBLE);

                new BDDRecettes().execute();
            } else {
                if(namesBoitesNonConnection.size()==0){//Si toutes les connexions ont réussi
                    Toast.makeText(getApplicationContext(), "Actualisation réussie",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    public void actualise(){
        for(int i=0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
            ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).setConnectedBdd(false);
        }
        Intent intent = new Intent(this,ChoixAlimentsRecettes.class);
        startActivity(intent);
    }
}
