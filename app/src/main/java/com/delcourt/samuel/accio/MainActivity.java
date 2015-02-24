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

import com.delcourt.samuel.accio.main_sous_activities.NewFrigoActivity;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import static android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends ActionBarActivity { //Permet la gestion des réfrigérateurs

    public static ArrayList<Refrigerateur> listeFrigos;
    public static int numberFrigos = 0;
    public ArrayList<String> listeFrigosNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listeFrigos = new ArrayList<Refrigerateur>();

        try { //Récupère le nombre total de frigos qui ont été créés
            FileReader fr = new FileReader("NombreFrigos_file");
            BufferedReader br = new BufferedReader(fr);
            Scanner sc = new Scanner(br);
            numberFrigos=sc.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try { //Recrée dans la liste listeFrigos tous les frigos, à partir des données permanentes
            InputStream instream = openFileInput("Frigos_file.txt");


                // prepare the file for reading
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line= buffreader.readLine();
            Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG).show();

            // close the file again
            instream.close();


        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "erreur lecture", Toast.LENGTH_LONG).show();;
        }

        // Get the reference of listViewFrigos
        ListView frigoList=(ListView)findViewById(R.id.listViewFrigos);

        listeFrigosNames=new ArrayList<String>();
        getFrigosNames();
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listeFrigosNames);
        // Set The Adapter
        frigoList.setAdapter(arrayAdapter);

        //register onClickListener to handle click events on each item
        frigoList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessageFrigoSelected(view);
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

    public void sendMessageFrigoSelected(View view){
        Intent intent = new Intent(this,MenuActivity.class);
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

    public void getFrigosNames(){
        listeFrigosNames.add("Coucou1");
        listeFrigosNames.add("Coucou2");
        int i =0;
        for (i=0;i<numberFrigos;i++)
        {
            listeFrigosNames.add(listeFrigos.get(i).getName());
        }
    }

}
