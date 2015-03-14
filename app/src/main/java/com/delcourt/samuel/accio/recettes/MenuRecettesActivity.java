package com.delcourt.samuel.accio.recettes;

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

import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Recette;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuRecettesActivity extends ActionBarActivity {

    protected static ArrayList<Recette> listeRecettesEnregistrées;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        readRecettes();
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
        Intent intent = new Intent(this,ChoixAlimentsRecettes.class);
        startActivity(intent);
    }

    public void afficheRecettes(){
        ArrayList<String> listeRecettesNames = new ArrayList<>();
        for(int i = 0 ; i<listeRecettesEnregistrées.size();i++){
            String name = listeRecettesEnregistrées.get(i).getName();
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
                sendMessageRecetteEnregistreeSelected(position);
            }
        });
    }

    public void sendMessageRecetteEnregistreeSelected(int position){
        String adresseWeb = listeRecettesEnregistrées.get(position).getAdresseWeb();
        RecetteEnregistreeActivity.adresseWeb = adresseWeb;
        Intent intent = new Intent(this,RecetteEnregistreeActivity.class);
        startActivity(intent);
    }

    public void readRecettes(){
        listeRecettesEnregistrées = new ArrayList<>();//réinitialise la liste
        try {
            InputStream instream = openFileInput("recettes_file.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);
            while(sc.hasNextLine() == true){//On recrée la liste des frigos : listeFrigosNames
                String name = sc.nextLine();
                String adresse = sc.nextLine();
                Recette recette = new Recette(name,adresse);
                listeRecettesEnregistrées.add(recette);
            }
            sc.close();
        } catch (FileNotFoundException e) {//A lieu à la première utilisation d'accio.
            // On crée alors deux recettes d'exemple
            try {
                OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("recettes_file.txt",MODE_APPEND));
                BufferedWriter bw = new BufferedWriter(outStream);
                PrintWriter out2 = new PrintWriter(bw);
                out2.println("Gâteau au chocolat");
                out2.println("http://www.marmiton.org/recettes/recette_gateau-au-chocolat-des-ecoliers_20654.aspx");
                out2.println("Tarte aux pommes");
                out2.println("http://www.marmiton.org/recettes/recette_tarte-aux-pommes_18588.aspx");
                out2.close();

                //On ajoute deux recettes d'exemple à la liste dynamique des recettes
                Recette recette1 = new Recette("Gâteau au chocolat","http://www.marmiton.org/recettes/recette_gateau-au-chocolat-des-ecoliers_20654.aspx");
                Recette recette2 = new Recette("Tarte aux pommes","http://www.marmiton.org/recettes/recette_tarte-aux-pommes_18588.aspx");
                listeRecettesEnregistrées.add(recette1);
                listeRecettesEnregistrées.add(recette2);

            } catch (FileNotFoundException e1) {
                Toast.makeText(getApplicationContext(), "liste recettes not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
