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
import com.delcourt.samuel.accio.structures.DataSimulee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import static android.widget.AdapterView.OnItemClickListener;

//Cette classe gère la gestion des frigos (dans lesquels sont réparties les boîtes).

//Elle n'a pour cela besoin que des noms des frigos : pour cette raison, elle (et les classes directement associées) lit et écrit dans un
//fichier texte listeFrigos.txt (pour l'instant une ArrayList dataFrigoNames).

//Cette classe a également besoin de connaître le nombre  de frigos : elle le lit (et peut de même le modifier) dans le fichier
//texte nombreFrigos.txt (pour l'instant un int dataNombreFrigos).

public class MainActivity extends ActionBarActivity { //Permet la gestion des réfrigérateurs


    public static int nombreFrigos;
    public static ArrayList<String> listeFrigosNames;
    public static DataSimulee dataSimulee = new DataSimulee();//Petit à petit, dataSimulee va disparaître : tout sera fait à
    // partir de initialisationFrigoReference()



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //LECTURE DES FICHIERS

        //Leccture nombre de frigos
        InputStream instream = null;
        try {
            instream = openFileInput("nombre_frigos_file.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            nombreFrigos= buffreader.read();
            Toast.makeText(getApplicationContext(), "nb frigos : " + nombreFrigos, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {//A lieu à la première utilisation d'accio. On crée alors le frigo de référence (utile pour nous)
            try {
                initialisationFrigoReference();
                nombreFrigos=1;
                Toast.makeText(getApplicationContext(), "initialisé, nb frigos :" + nombreFrigos, Toast.LENGTH_SHORT).show();
            } catch (IOException e1) {Toast.makeText(getApplicationContext(), "erreur lecture nb frigos", Toast.LENGTH_SHORT).show();}

            } catch (IOException e) {
            e.printStackTrace();
        }

        /*instream = null;
        try {
            instream = openFileInput("Frigos_file.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);

            int i;
            int n = nombreFrigos;
            for(i=0;i<2;i++){//2 sera remplacé par n après
                try{String name = sc.next();//ce bloc try est aussi temporaire
                    i++;
                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();}
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"exception", Toast.LENGTH_LONG).show();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            */



        //récupère les données à chaque ouverture de l'activité (=actualisation permanente) :
        listeFrigosNames = dataSimulee.dataFrigoNames; //récupère la liste des noms des frigos
        //FIN LECTURE DES FICHIERS


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
        for (int i=0;i<nombreFrigos;i++){
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

    public void initialisationFrigoReference() throws IOException {//A CONTINUER
        //crée le fichier nombre_frigos, un frigo :
        FileOutputStream fos = openFileOutput("nombre_frigos_file.txt", Context.MODE_PRIVATE);
        fos.write(1);
        fos.close();

    }
}
