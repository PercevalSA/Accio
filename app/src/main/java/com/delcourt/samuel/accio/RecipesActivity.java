package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.delcourt.samuel.accio.create_new_object_activities.NewFrigoActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Recette;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class RecipesActivity extends ActionBarActivity {

    ArrayList<Recette> listeRecettes = AccueilActivity.listeRecettesEnregistrées;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        afficheRecettes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                // on mettra la méthode openSettings() quand elle sera cree
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearch(){
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public void sendMessageNouvelleRecette(View view){
        Intent intent = new Intent(this,OptionsRecettesActivity.class);
        startActivity(intent);
    }

    public void afficheRecettes(){
        ArrayList<String> listeRecettesNames = new ArrayList<>();
        for(int i = 0 ; i<listeRecettes.size();i++){
            String name = listeRecettes.get(i).getName();
            listeRecettesNames.add(name);
        }
        ListView recettesList=(ListView)findViewById(R.id.list_recettes);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listeRecettesNames);
        recettesList.setAdapter(arrayAdapter);
        recettesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                sendMessageReceetteSelected(position);
            }
        });
    }

    public void sendMessageReceetteSelected(int position){
        String adresseWeb = listeRecettes.get(position).getAdresseWeb();
        RecetteMarmitonActivity.adresseWeb = adresseWeb;
        Intent intent = new Intent(this,RecetteMarmitonActivity.class);
        startActivity(intent);
    }
}
