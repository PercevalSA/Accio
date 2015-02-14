package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContentActivity extends ActionBarActivity {

    public static int nombreBoites;
    public static ArrayList<String> listeBoitesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //récupère les données à chaque ouverture de l'activité (=actualisation permanente) :
        nombreBoites= MenuActivity.refrigerateur.numberBoxes;//récupère la valeur dans les données
        listeBoitesNames = MenuActivity.refrigerateur.listBoxes; //récupère la liste des noms des frigos


        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        ListView boxesList=(ListView)findViewById(R.id.listeViewBoites);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listeBoitesNames);
        // Set The Adapter
        boxesList.setAdapter(arrayAdapter);


        //register onClickListener to handle click events on each item
        boxesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                int indexBox = position;
                sendMessageBoxSelected(view, indexBox);
            }
        });

        TextView textElement = (TextView) findViewById(R.id.messageBoitesduFrigo);
        textElement.setText("Boites Accio du réfrigérateur : " + MenuActivity.refrigerateur.name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
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

    public void sendMessageBoxSelected(View view, int index){

    }

    public void sendMessageNewBox(View view){

    }

}
