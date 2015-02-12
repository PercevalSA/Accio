package com.delcourt.samuel.accio.main_sous_activities;

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

        int k = 0; //permet de s'assurer que le nom du nouveau frigo n'existe pas encore

        Intent intent = new Intent(this,MainActivity.class);

        EditText editText = (EditText) findViewById(R.id.nameFrigo); //Récupère le nom du frigo
        String messageName = editText.getText().toString();


        if (messageName.length() == 0){ //S'assure que le nom n'est pas vide
            Toast.makeText(getApplicationContext(), "Nom invalide", Toast.LENGTH_LONG).show();
        }
        else {
            for (int i=0;i<MainActivity.numberFrigos;i++){
                if (messageName.compareTo(MainActivity.listeFrigos.get(i).name) == 0){
                    k++;
                }
            }

            if (k > 0){
                Toast.makeText(getApplicationContext(), "Un réfrigérateur possédant ce nom existe déjà", Toast.LENGTH_LONG).show();
            }
            else{
                Refrigerateur newFrigo = new Refrigerateur(messageName);
                MainActivity.listeFrigos.add(newFrigo);
                MainActivity.numberFrigos++;

                startActivity(intent); //Renvoie sur la page d'accueil
            }
        }
    }
}
