package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.delcourt.samuel.accio.create_new_object_activities.NewBoxActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class ListeBoitesActivity extends ActionBarActivity {

    //public static int nombreBoites;
    public static ArrayList<String> listeBoitesNames = new ArrayList<>();
    public String frigoName = com.delcourt.samuel.accio.RefrigerateurActivity.refrigerateur.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_boites);


        //Leccture de la liste des boîtes
        InputStream instream = null;
        listeBoitesNames = new ArrayList<>();//réinitialise la liste
        try {
            instream = openFileInput(frigoName + "Boxes.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);

            while(sc.hasNextLine() == true){//On recrée la liste des frigos : listeFrigosNames
                String name = sc.nextLine();
                listeBoitesNames.add(name);
            }
        } catch (FileNotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ce réfrigérateur ne contient pas encore de boite Accio", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

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
        textElement.setText("Boites Accio du réfrigérateur : " + com.delcourt.samuel.accio.RefrigerateurActivity.refrigerateur.name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_boites, menu);
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

    public void sendMessageBoxSelected(View view, int index){//A COMPLETER !!!
        Intent intent = new Intent(this, BoxActivity.class);
        startActivity(intent);
    }

    public void sendMessageNewBox(View view){
        Intent intent = new Intent(this, NewBoxActivity.class);
        startActivity(intent);
        }

}
