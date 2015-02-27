package com.delcourt.samuel.accio;

import android.content.Context;
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
import com.delcourt.samuel.accio.structures.Refrigerateur;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static android.widget.AdapterView.OnItemClickListener;

//Cette classe gère la gestion des frigos (dans lesquels sont réparties les boîtes).

//Elle n'a pour cela besoin que des noms des frigos : pour cette raison, elle (et les classes directement associées) lit et écrit dans un
//fichier texte liste_frigos_file.txt

public class MainActivity extends ActionBarActivity { //Permet la gestion des réfrigérateurs


    public static int nombreFrigos;
    public static ArrayList<String> listeFrigosNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LECTURE DES FICHIERS

        InputStream instream;

        try {
            instream = openFileInput("frigos_file.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);
            listeFrigosNames = new ArrayList<>();//réinitialise la liste
            while(sc.hasNextLine() == true){//On recrée la liste des frigos : listeFrigosNames
                String name = sc.nextLine();
                listeFrigosNames.add(name);
                }
        } catch (FileNotFoundException e) {//A lieu à la première utilisation d'accio. On crée alors le frigo de référence (suite) (utile pour nous)
            try {
                OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_APPEND));
                BufferedWriter bw = new BufferedWriter(outStream);
                PrintWriter out2 = new PrintWriter(bw);
                out2.println("Réfrigérateur essai");
                out2.close();

                nombreFrigos = 1;//initialise les données locales
                listeFrigosNames.add("Réfrigérateur essai");//initialise les données locales
                initialisationFrigoExemple();//Permet la suite de l'initialisation du frigo de référence (càd l'exemple)
            } catch (FileNotFoundException e1) {
                Toast.makeText(getApplicationContext(), "liste frigo not found", Toast.LENGTH_SHORT).show();
            }
        }

        //FIN LECTURE DES FICHIERS


        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        ListView frigoList=(ListView)findViewById(R.id.listViewFrigos);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listeFrigosNames);
        // Set The Adapter
        frigoList.setAdapter(arrayAdapter);


        //register onClickListener to handle click events on each item
        frigoList.setOnItemClickListener(new OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                sendMessageFrigoSelected(position);
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

    public void sendMessageFrigoSelected(int indexName){
        Intent intent = new Intent(this, MenuActivity.class);//Lance l'activité MenuActivity, avec le nom du frigo sélectionné en message
        String frigoName = listeFrigosNames.get(indexName);
        MenuActivity.refrigerateur = new Refrigerateur(frigoName);
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

    public void initialisationFrigoExemple() throws FileNotFoundException {

        //On crée le fichier contenant la liste des boites de Réfrigérateur essai
        OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("Réfrigérateur essaiBoxes.txt",MODE_APPEND));
        BufferedWriter bw = new BufferedWriter(outStream);
        PrintWriter out2 = new PrintWriter(bw);
        out2.println("Fruits (exemple)");
        out2.println("Légumes (exemple)");
        out2.close();
    }
}

