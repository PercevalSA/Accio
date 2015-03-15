package com.delcourt.samuel.accio.options_activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.BoxActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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

    public void sendMessageRename(View view){
        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_boite);
        String newName = editText.getText().toString();

        rename(newName);
    }

    public void rename(String newName){

        if(newName.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nouveau nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else{
            String nameFrigo = RefrigerateurActivity.refrigerateur.getName();
            //On change le nom de la boîte dans la liste dynamique :
            String nameBoite = boite.getName();
            for(int j =0;j<RefrigerateurActivity.refrigerateur.getBoxes().size();j++){
                if(RefrigerateurActivity.refrigerateur.getBoxes().get(j).getName() == nameBoite){
                    RefrigerateurActivity.refrigerateur.getBoxes().get(j).setName(newName);
                }
            }
            try {
                OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt",MODE_PRIVATE));
                BufferedWriter bw = new BufferedWriter(outStream);
                PrintWriter out2 = new PrintWriter(bw);
                for(int i=0;i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
                    out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getReferenceBdd());
                    out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName());
                    out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
                }
                out2.close();

            } catch (FileNotFoundException e1) {
                Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this,BoxActivity.class);
            startActivity(intent);
        }

    }
}
