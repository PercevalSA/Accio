package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.delcourt.samuel.accio.create_new_object_activities.NewBoxActivity;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ListeBoitesActivity extends ActionBarActivity {

    private static Refrigerateur refrigerateur;

    private static ArrayList<String> listeFavorisNames=new ArrayList<>();//Liste des noms des aliments favoris
    private static ArrayList<String> listeFavorisAbsentsNames=new ArrayList<>();//Liste des noms des aliments favoris absents

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

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



    public static ArrayList<String> getListeFavorisAbsentsNames() {
        return listeFavorisAbsentsNames;
    }

    public static ArrayList<String> getListeFavorisNames() {
        return listeFavorisNames;
    }

    public static void setListeFavorisNames(ArrayList<String> listeFavorisNames) {
        ListeBoitesActivity.listeFavorisNames = listeFavorisNames;
    }

    public static void setListeFavorisAbsentsNames(ArrayList<String> listeFavorisAbsentsNames) {
        ListeBoitesActivity.listeFavorisAbsentsNames = listeFavorisAbsentsNames;
    }
    public static void resetListeFavorisAbsentsNames(){listeFavorisAbsentsNames=new ArrayList<>();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_liste_boites);

            ImageButton fabImageButton = (ImageButton) findViewById(R.id.fab_image_button);
            fabImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageNewBox(v);
                }
            });

            mDrawerList = (ListView)findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();

            addDrawerItems();
            setupDrawer();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            if(refrigerateur.isRefrigerateurCreated()==false){//Si le frigo n'a pas encore été créé, on le crée.

                boolean chargementReussi = creationRéfrigerateur();

                if (chargementReussi == false) {//si le chargement du frigo ou des boîtes a échoué, on affiche un message
                    Toast toast = Toast.makeText(getApplicationContext(), "Erreur chargement du frigo (liste des boîtes Accio inaccessible)", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }

            lectureListeFavoris();

            afficheListeBoites();

            chargeAliments();

            TextView textElement = (TextView) findViewById(R.id.messageBoitesduFrigo);
            textElement.setText("Boites Accio du réfrigérateur : " + ListeBoitesActivity.getRefrigerateur().getName());

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }

    public void chargeAliments(){

        //Initialisation pour connecter à la bdd
        namesBoitesNonConnection = new ArrayList<>();
        listeNomAliment = new ArrayList<>();
        listeBoiteID = new ArrayList<>();
        listeMarqueAliment = new ArrayList<>();
        listeFavoris = new ArrayList<>();
        listeHistoriqueAliment = new ArrayList<>();

        for(int i=0; i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
            //On prend les références des boîtes pas encore chargées
            if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getConnectedBdd()==false){
                numerosBoitesAConnecter.add(i);
            }
        }
        //On lance les connexions aux bdd successives :
        if(numerosBoitesAConnecter.size()!=0){
            boite=ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
            refBdd=boite.getReferenceBdd();
            TextView textElement = (TextView) findViewById(R.id.message_chargement);
            textElement.setText("Chargement des aliments de la boîte "+boite.getName());
            new BDDListeAliments().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_boites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_rename:
                optionsFrigo();
                return true;
            case R.id.action_delete:
                messageDeleteFrigo();
            default:
                if (mDrawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }

    public static Refrigerateur getRefrigerateur(){return refrigerateur;}

    public static void setRefrigerateur(Refrigerateur frigo){refrigerateur=frigo;}

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

    public void optionsFrigo(){
        Intent intent = new Intent(this, FrigoOptionsActivity.class);
        startActivity(intent);
    }

    public void messageDeleteFrigo(){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(ListeBoitesActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer le réfrigérateur " + ListeBoitesActivity.getRefrigerateur().getName()+
                " ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void delete(){

        String nameFrigo = ListeBoitesActivity.getRefrigerateur().getName();
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
    }

    public void sendMessageBoxSelected(View view, int index){
        BoxActivity.setBoxIndex(index);
        Intent intent = new Intent(this, BoxActivity.class);
        startActivity(intent);
    }

    public void sendMessageNewBox(View view){
        Intent intent = new Intent(this, NewBoxActivity.class);
        startActivity(intent);
    }

    public void addDrawerItems(){
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

        HashMap<String, String> map;

        map = new HashMap<String, String>();
        map.put("titre","Accueil");
        map.put("image",String.valueOf(R.drawable.ic_home));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("titre","Recette");
        map.put("image",String.valueOf(R.drawable.ic_recipe));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("titre","Favoris");
        map.put("image",String.valueOf(R.drawable.ic_fav));
        listItem.add(map);

        map = new HashMap<String, String>();
        map.put("titre","Ajout d'aliment");
        map.put("image",String.valueOf(R.drawable.ic_ajout));
        listItem.add(map);

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.menu_options,
                new String[] {"titre","image"}, new int[] {R.id.titre_menu,R.id.imgMenu});

        //On attribue à notre listView l'adapter que l'on vient de créer
        mDrawerList.setAdapter(mSchedule);


        //register onClickListener to handle click events on each item
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
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

    public void itemSelected(int position){
        //Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();

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

        if(position ==0){
            Intent intent = new Intent(this, AccueilActivity.class);
            startActivity(intent);
        }
    }

    public void afficheListeBoites(){
        int numberBoxes = ListeBoitesActivity.getRefrigerateur().getBoxes().size();

        if(numberBoxes==0){//Si pas de boîte, on affiche un message
            TextView textElement = (TextView) findViewById(R.id.message_liste_boites);
            textElement.setText("Ce réfrigérateur ne contient pas encore de boîte Accio");
        }

        else {//On affiche la liste des boîtes

            // Get the reference of listViewFrigos (pour l'affichage de la liste)
            final ListView listViewBoxes=(ListView)findViewById(R.id.listeViewBoites);

            //Création de la ArrayList qui nous permettra de remplir la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

            //On déclare la HashMap qui contiendra les informations pour un item
            HashMap<String, String> map;


            for(int i=0;i<numberBoxes;i++){


                //on insère la référence aux éléments à afficher
                map = new HashMap<String, String>();
                map.put("titre", ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getName());
                map.put("description", ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getType());
                //Récupère le nom de l'image à affihcer
                String type = ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getType();
                //MODIFIER LES NOMS DES IMAGES A AFFICHER
                if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
                else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
                else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
                else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
                else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
                else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
                else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
                    map.put("img", String.valueOf(R.drawable.ic_launcher));
                    //On affiche un toast
                    Toast.makeText(getApplicationContext(), "Le type de la boîte n'a pas été reconnu",
                            Toast.LENGTH_SHORT).show();
                }

                //enfin on ajoute cette hashMap dans la arrayList
                listItem.add(map);
            }

            //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
            SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.liste_boites_listview,
                    new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});

            //On attribue à notre listView l'adapter que l'on vient de créer
            listViewBoxes.setAdapter(mSchedule);


            //register onClickListener to handle click events on each item
            listViewBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                // argument position gives the index of item which is clicked

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    int indexBox = position;
                    sendMessageBoxSelected(view, indexBox);
                }
            });
        }
    }

    class BDDListeAliments extends AsyncTask<String, Void, String> {

        private boolean connectionSuccessful = true;

        public void setConnectionSuccessful(boolean b) {
            connectionSuccessful = b;
        }

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            ListeBoitesActivity.listeBoiteID = new ArrayList<>();
            ListeBoitesActivity.listeNomAliment = new ArrayList<>();
            ListeBoitesActivity.listeFavoris = new ArrayList<>();

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/alimrecup.php?boiteid=" + refBdd);
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
                    ListeBoitesActivity.listeBoiteID.add(json_data.getString(0));
                    ListeBoitesActivity.listeNomAliment.add(json_data.getString(1));
                    ListeBoitesActivity.listeHistoriqueAliment.add(json_data.getString(4));
                    ListeBoitesActivity.listeFavoris.add(json_data.getString(7));
                    ListeBoitesActivity.listeMarqueAliment.add(json_data.getString(9));


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
                //Ne lève une exception que si la boîte est vide => Pas un problème !
            }
            return result;
        }


        protected void onPostExecute(String resultat) {

            TextView textElement0 = (TextView) findViewById(R.id.message_chargement);
            textElement0.setText("");

            if (connectionSuccessful == false) {
                namesBoitesNonConnection.add(boite.getName());
                Toast.makeText(getApplicationContext(), "Pas d'accès à la base de données", Toast.LENGTH_SHORT).show();
            } else { //La connexion à la base de données a fonctionné.
                //On supprime réinitialise la liste des aliments de la boîte pour la réécrire :
                boite.getListeAliments().clear();

                boite.setConnectedBdd(true);//On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd

                int nbAliment = listeNomAliment.size();
                for (int k = 0; k < nbAliment; k++) {

                    String nom = listeNomAliment.get(k);
                    String marque = listeMarqueAliment.get(k);
                    boolean favori;
                    String historique = listeHistoriqueAliment.get(k);
                    String alimID = listeBoiteID.get(k);
                    //marque = listeMarqueAliment.get(k);
                    if (listeFavoris.get(k).compareTo("0") == 0) {
                        favori = false;
                    } else {
                        favori = true;
                    }

                    Aliment aliment = new Aliment(nom, marque, favori, historique, boite.getName(), alimID, boite.getType());
                    boite.getListeAliments().add(aliment);

                }


                //Si il reste des boîtes à connecter, on les connecte.
                numerosBoitesAConnecter.remove(0);
                if (numerosBoitesAConnecter.size() != 0) {
                    boite = ListeBoitesActivity.getRefrigerateur().getBoxes().get(numerosBoitesAConnecter.get(0));
                    refBdd = boite.getReferenceBdd();

                    TextView textElement = (TextView) findViewById(R.id.message_chargement);
                    textElement.setText("Chargement des aliments de la boîte " + boite.getName());

                    new BDDListeAliments().execute();
                } else if (namesBoitesNonConnection.size() == 0) {//Si toutes les connexions ont réussi
                    //Tous les aliments ont été récupérés : on peut s'occuper des favoris
                    gestionFavoris();
                }
            }
        }
    }

    public void lectureListeFavoris() {//LECTURE LISTE FAVORIS

        listeFavorisNames=new ArrayList<>();

        InputStream instream = null;
        String nameFrigo = refrigerateur.getName();
        try {
            instream = openFileInput(nameFrigo + "Favoris.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);

            while (sc.hasNextLine() == true) {//On recrée la liste des favoris
                String favoriteName = sc.nextLine();
                listeFavorisNames.add(favoriteName);
            }
            sc.close();
        } catch (FileNotFoundException e) {//Survient si le fichier texte favoris du frigo n'a pas encore été créé
            Log.e("log_tag", "Error " + e.toString());
            try {
                OutputStreamWriter outStream = null;
                outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Favoris.txt",MODE_APPEND));

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void gestionFavoris(){

        //On cherche les aliments favoris absent du réfrigérateur
        listeFavorisAbsentsNames=new ArrayList<>();
        int nbFavoris = listeFavorisNames.size();
        for (int i=0;i<nbFavoris;i++){
            //On cherche dans toutes les boîtes si cet aliment s'y trouve
            int found = 0;
            for(int j=0;j<refrigerateur.getBoxes().size();j++){
                for(int k=0;k<refrigerateur.getBoxes().get(j).getListeAliments().size();k++){
                    if(listeFavorisNames.get(i).compareTo(refrigerateur.getBoxes().get(j).getListeAliments().get(k).getAlimentName())==0){
                        found=1;
                        break;
                    }
                }
            }
            //On vient de regarder tous les aliments
            if(found==0){
                listeFavorisAbsentsNames.add(listeFavorisNames.get(i));
            }
        }



        //Si il certains favoris sont absents du frigo, on l'affiche dans une alertDialog :
        if(listeFavorisAbsentsNames.size()!=0){

            String nomsAliments="-"+listeFavorisAbsentsNames.get(0);
            for(int i=1;i<listeFavorisAbsentsNames.size();i++){
                nomsAliments=nomsAliments + "\n-" + listeFavorisAbsentsNames.get(i);
            }

            AlertDialog.Builder adb = new AlertDialog.Builder(ListeBoitesActivity.this);
            adb.setTitle("Favoris");
            adb.setMessage(refrigerateur.getName() + " : les aliments favoris suivants sont absents :\n\n"
                    + nomsAliments);
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            adb.show();
        }

    }
}
