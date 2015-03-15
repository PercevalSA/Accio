package com.delcourt.samuel.accio.options_activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.delcourt.samuel.accio.BoxActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.Box;

public class BoxOptionsActivity extends ActionBarActivity {

    private Box boite = BoxActivity.boite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_options);

        //Récupère les informations de la boîte pour les afficher :
        TextView textElement = (TextView) findViewById(R.id.boxName_BoxOptionActivity);
        textElement.setText(boite.getName());

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxOptionActivity);
        textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

        //Affiche l'image du type de la boîte
        //METTRE LES BONNES IMAGES !
        String type = boite.getType();
        ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_boxOptionActivity);
        if (type.compareTo("Fruits")==0){ textElement3.setImageResource(R.drawable.ic_fruit);}
        else if (type.compareTo("Légumes")==0){textElement3.setImageResource(R.drawable.ic_legume);}
        else if (type.compareTo("Produits laitiers")==0){textElement3.setImageResource(R.drawable.ic_produit_laitier);}
        else if (type.compareTo("Poisson")==0){textElement3.setImageResource(R.drawable.ic_poisson);}
        else if (type.compareTo("Viande")==0){textElement3.setImageResource(R.drawable.ic_viande);}
        else if (type.compareTo("Sauces et condiments")==0){textElement3.setImageResource(R.drawable.ic_condiment);}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_options, menu);
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
}
