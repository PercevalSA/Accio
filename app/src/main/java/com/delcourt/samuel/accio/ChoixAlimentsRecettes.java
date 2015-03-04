package com.delcourt.samuel.accio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ChoixAlimentsRecettes extends ActionBarActivity {

    private ArrayList<String> listeAlimentsProposes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_aliments_recettes);

        //Juste pour l'exemple :
        listeAlimentsProposes.add("banane");

        //On affiche cette liste :
        afficheListeAlimentsProposes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_aliments_recettes, menu);
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

    public void alimentSelectionne(int position){
        String name = listeAlimentsProposes.get(position);
        OptionsRecettesActivity.aliments=name;

        //Envoie sur la page d'options
        Intent intent = new Intent(this,OptionsRecettesActivity.class);
        startActivity(intent);

    }

    public void afficheListeAlimentsProposes(){
        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        ListView frigoList=(ListView)findViewById(R.id.listeAlimentsProposes);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listeAlimentsProposes);
        // Set The Adapter
        frigoList.setAdapter(arrayAdapter);


        //register onClickListener to handle click events on each item
        frigoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                alimentSelectionne(position);
            }
        });
    }
}
