package com.delcourt.samuel.accio.main_sous_activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.delcourt.samuel.accio.MainActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.util.ArrayList;

public class NewFrigoActivity extends ActionBarActivity {

    public ArrayList a;


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

    public void newFrigo(View view){

        Intent intent = new Intent(this,MainActivity.class);

        EditText editText = (EditText) findViewById(R.id.nameFrigo); //Récupère le nom du frigo
        String messageName = editText.getText().toString();

        Refrigerateur newFrigo = new Refrigerateur(messageName);
        MainActivity.listeFrigos.add(newFrigo); //SOURCE D ERREUR...
        //MainActivity.frigoNamesList.add(messageName);

        startActivity(intent); //Renvoie sur la page d'accueil
    }
}
