package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.delcourt.samuel.accio.create_new_object_activities.NewFrigoActivity;
import com.delcourt.samuel.accio.structures.DataSimulee;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;

//Cette classe gère la gestion des frigos (dans lesquels sont réparties les boîtes).

//Elle n'a pour cela besoin que des noms des frigos : pour cette raison, elle (et les classes directement associées) lit et écrit dans un
//fichier texte listeFrigos.txt (pour l'instant une ArrayList dataFrigoNames).

//Cette classe a également besoin de connaître le nombre  de frigos : elle le lit (et peut de même le modifier) dans le fichier
//texte nombreFrigos.txt (pour l'instant un int dataNombreFrigos).

public class MainActivity extends ActionBarActivity { //Permet la gestion des réfrigérateurs

    public static int nombreFrigos;
    public static ArrayList<String> listeFrigosNames;
    public static DataSimulee dataSimulee = new DataSimulee();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //récupère les données à chaque ouverture de l'activité (=actualisation permanente) :
        nombreFrigos= dataSimulee.dataNombreFrigos;//récupère la valeur dans les données
        listeFrigosNames = dataSimulee.dataFrigoNames; //récupère la liste des noms des frigos


        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        ListView frigoList=(ListView)findViewById(R.id.listViewFrigos);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listeFrigosNames);
        // Set The Adapter
        frigoList.setAdapter(arrayAdapter);


        //register onClickListener to handle click events on each item
        frigoList.setOnItemClickListener(new OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                int indexFrigo = position;
                sendMessageFrigoSelected(view, indexFrigo);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void sendMessageFrigoSelected(View view, int indexName){

        Intent intent = new Intent(this, MenuActivity.class);//Lance l'activité MenuActivity, avec le nom du frigo sélectionné en message
        String frigoName = listeFrigosNames.get(indexName);

        int index=0;//Récupère le fichier du frigo correspondant et le charge en mémoire dans MenuActivity
        for (int i=0;i<MainActivity.dataSimulee.dataNombreFrigos;i++){
            if (frigoName.compareTo(MainActivity.dataSimulee.dataListeFrigos.get(i).name)==0) {
                index = i;
                break;
            }
        }
        MenuActivity.refrigerateur = MainActivity.dataSimulee.dataListeFrigos.get(index);
        startActivity(intent);
    }

    public void sendMessageNouveau(View view){
        Intent intent = new Intent(this,NewFrigoActivity.class);
        startActivity(intent);
    }

    public void openSearch(){
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }
}
