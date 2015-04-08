package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.delcourt.samuel.accio.create_new_object_activities.NewBoxActivity;
import com.delcourt.samuel.accio.options_activities.FrigoOptionsActivity;
import com.delcourt.samuel.accio.recettes.MenuRecettesActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ListeBoitesActivity extends ActionBarActivity {

    private static Refrigerateur refrigerateur;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_liste_boites);

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


            afficheListeBoites();

            TextView textElement = (TextView) findViewById(R.id.messageBoitesduFrigo);
            textElement.setText("Boites Accio du réfrigérateur : " + ListeBoitesActivity.getRefrigerateur().getName());

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
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
            SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichage_liste_boites,
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



}
