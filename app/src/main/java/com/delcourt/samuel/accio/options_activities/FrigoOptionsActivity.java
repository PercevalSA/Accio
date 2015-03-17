package com.delcourt.samuel.accio.options_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.BoxActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FrigoOptionsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frigo_options);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frigo_options, menu);
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

    public void sendMessageRenameFrigo(View view){
        int k = 0;

        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_frigo);
        final String newName = editText.getText().toString();

        //On s'assure qu'aucun frigo du même nom n'a encore été créé
        for (int i=0;i< AccueilActivity.listeFrigosNames.size();i++){
            if (newName.compareTo(AccueilActivity.listeFrigosNames.get(i)) == 0){
                k++;
            }
        }
        if(newName.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nouveau nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else if (k > 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Un réfrigérateur possédant ce nom existe déjà", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else{
            //on créé une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(FrigoOptionsActivity.this);
            //on attribue un titre à notre boite de dialogue
            adb.setTitle("Confirmation");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez-vous renommer le réfrigérateur ''" + RefrigerateurActivity.refrigerateur.getName() + "'' en : ''" + newName + "'' ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    rename(newName);
                }
            });
            //on affiche la boite de dialogue
            adb.show();
        }
    }

    public void sendMessageDeleteFrigo(View view){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(FrigoOptionsActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer le réfrigérateur " + RefrigerateurActivity.refrigerateur.getName()+
                " ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void rename(String newName){
        String nameFrigo = RefrigerateurActivity.refrigerateur.getName();

        //On change le nom du frigo dans la liste dynamique :
        int index = AccueilActivity.listeFrigosNames.indexOf(nameFrigo);
        AccueilActivity.listeFrigosNames.set(index, newName);

        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<AccueilActivity.listeFrigosNames.size();i++){
                out2.println(AccueilActivity.listeFrigosNames.get(i));
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste frigos", Toast.LENGTH_SHORT).show();
        }

        listeboites(newName);

        Intent intent = new Intent(this,AccueilActivity.class);
        startActivity(intent);
    }

    public void delete(){
        String nameFrigo = RefrigerateurActivity.refrigerateur.getName();
        int index = AccueilActivity.listeFrigosNames.indexOf(nameFrigo);
        AccueilActivity.listeFrigosNames.remove(index);

        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<AccueilActivity.listeFrigosNames.size();i++){
                out2.println(AccueilActivity.listeFrigosNames.get(i));
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste frigos", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,AccueilActivity.class);
        startActivity(intent);
    }

    public void listeboites(String newName){
        //On crée le fichier contenant la liste des boîtes etc

        try {//Ajoute le nom du nouveau frigo dans frigos_file.txt (ne remplace pas le fichier mais écrit à la suite)
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(newName + "Boxes.txt",MODE_APPEND));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out = new PrintWriter(bw);
            for(int i =0;i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
                out.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getReferenceBdd());
                out.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName());
                out.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
            }
            out.close();
        } catch (java.io.IOException e) {
            Toast.makeText(getApplicationContext(), "erreur recréation liste boîtes", Toast.LENGTH_SHORT).show();
        }
    }
}
