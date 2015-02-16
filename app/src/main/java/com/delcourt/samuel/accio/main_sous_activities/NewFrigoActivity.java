package com.delcourt.samuel.accio.main_sous_activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.delcourt.samuel.accio.MainActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NewFrigoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_frigo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_frigo, menu);
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

    public void newFrigo(View view) throws IOException {

        Intent intent = new Intent(this, MainActivity.class);

        EditText editText = (EditText) findViewById(R.id.nameFrigo); //Récupère le nom du frigo
        String messageName = editText.getText().toString();

        //Refrigerateur newFrigo = new Refrigerateur(messageName);
        //MainActivity.listeFrigos.add(newFrigo);
        int N = MainActivity.numberFrigos + 1;

       //Sauve le nom du frigo dans les données
           try{ FileWriter fw = new FileWriter("Frigos_file",true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.print(messageName);//écrit le nom du frigo
            pw.close();
        } catch (IOException e) {
               Toast.makeText(getApplicationContext(), "erreur", Toast.LENGTH_LONG).show();}

        try { //Sauve le nombre total de frigos dans les données, remplace le fichier (et donc la valeur) précédent
            PrintWriter pw = new PrintWriter("NombreFrigos_file");
            pw.print(N);//écrit le nombre total de frigos
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();}

        startActivity(intent); //Renvoie sur la page d'accueil

    }
}