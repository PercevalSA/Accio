package com.delcourt.samuel.accio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.structures.Box;


public class HistoriqueBoiteActivity extends ActionBarActivity {

    private static Box boite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_historique_boite);

            afficheImage();
            //Récupère les informations de la boîte pour les afficher :
            TextView textElement = (TextView) findViewById(R.id.boxName_historique);
            textElement.setText(boite.getName());

            TextView textElement2 = (TextView) findViewById(R.id.frigoName_historique);
            textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.getRefrigerateur().getName() + ")");
        } catch (Exception e){
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historique_boite, menu);
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

    public static void setBox(Box box){boite=box;}

    public void afficheImage(){
        //Affiche l'image du type de la boîte
        //METTRE LES BONNES IMAGES !
        String type = boite.getType();
        ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_historique);
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
