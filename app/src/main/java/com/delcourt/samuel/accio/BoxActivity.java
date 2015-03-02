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
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.util.ArrayList;

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

        //A ADAPTER EN FONCTION DU TYPE DE BOITE
        ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_boxActivity);
        textElement3.setImageResource(R.drawable.ic_launcher);

        afficheAliments();

        Toast.makeText(getApplicationContext(), "L'image devra correspondre à la catégorie",
                Toast.LENGTH_SHORT).show();

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
        ArrayList<String> liste = boite.getListeAliments();
        int size = liste.size();
        if( RefrigerateurActivity.refrigerateur.getConnectionBdd() == true){//Si on a réussi à se connecter à la base de données
            if(size==0){
                ListView frigoList=(ListView)findViewById(R.id.liste_aliments);
                liste = new ArrayList<>();
                liste.add("Il n'y a aucun aliment dans cette boîte pour l'instant");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, liste);
                frigoList.setAdapter(arrayAdapter);

            } else {
                ListView frigoList=(ListView)findViewById(R.id.liste_aliments);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, liste);
                frigoList.setAdapter(arrayAdapter);
                frigoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    }
                });

            }
        }else{//Si pas de connection à la base de données, on l'indique
            ListView frigoList=(ListView)findViewById(R.id.liste_aliments);
            liste = new ArrayList<>();
            liste.add("La base de données n'est pas accessible");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, liste);
            frigoList.setAdapter(arrayAdapter);
        }

    }
}
