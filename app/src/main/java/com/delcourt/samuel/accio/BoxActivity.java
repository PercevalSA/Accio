package com.delcourt.samuel.accio;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.sql.*;


import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.util.ArrayList;
import java.util.HashMap;

public class BoxActivity extends ActionBarActivity {

    public static Box boite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        //Récupère les informations de la boîte pour les afficher :
        TextView textElement = (TextView) findViewById(R.id.boxName_BoxActivity);
        textElement.setText(boite.getName());

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
        textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

        //Affiche l'image du type de la boîte
        //METTRE LES BONNES IMAGES !
        String type = boite.getType();
        ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_boxActivity);
        if (type.compareTo("Fruits")==0){ textElement3.setImageResource(R.drawable.ic_launcher);}
        else if (type.compareTo("Légumes")==0){textElement3.setImageResource(R.drawable.ic_launcher);}
        else if (type.compareTo("Produits laitiers")==0){textElement3.setImageResource(R.drawable.ic_launcher);}
        else if (type.compareTo("Poisson")==0){textElement3.setImageResource(R.drawable.ic_launcher);}
        else if (type.compareTo("Viande")==0){textElement3.setImageResource(R.drawable.ic_launcher);}
        else if (type.compareTo("Sauces et condiments")==0){textElement3.setImageResource(R.drawable.ic_launcher);}
        else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
            textElement3.setImageResource(R.drawable.ic_launcher);
            //On affiche un toast
            Toast.makeText(getApplicationContext(), "Le type de la boîte n'a pas été reconnu",
                    Toast.LENGTH_SHORT).show();
        }

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

        if( RefrigerateurActivity.refrigerateur.getConnectionBdd() == true){//Si on a réussi à se connecter à la base de données
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
        else {//C'est le cas où l'on n'a pas pu se connecter à la BDD
            TextView textElement = (TextView) findViewById(R.id.message_BoxActivity);
            textElement.setText("Impossible de se connecter à la base de données");
        }

    }

    public void sendMessageAlimentSelected(View view, int index){

    }
}
