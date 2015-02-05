package com.delcourt.samuel.accio.main_sous_activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.delcourt.samuel.accio.MainActivity;
import com.delcourt.samuel.accio.R;

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

    public void newFrigo(){

        //Comment récupérer le texte (le nom du frigo) ??

        /*
        MainActivity.createFrigo(message); //Création du nouveau frigo en utilisant la méthode écrite dans la classe de référence MainActivity

        Intent intent = new Intent(this,MainActivity.class); //Renvoie sur la page d'accueil
        startActivity(intent);

        */
    }
}
