package com.delcourt.samuel.accio.create_new_object_activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.delcourt.samuel.accio.MainActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class NewFrigoActivity extends ActionBarActivity {

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
        String newFrigoName = editText.getText().toString();
        int N = MainActivity.nombreFrigos + 1;


        if (newFrigoName.length() == 0){ //Si le nom est vide, envoie un message
            Toast toast = Toast.makeText(getApplicationContext(), "Nom invalide", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else { //On s'assure qu'aucun frigo du même nom n'a encore été créé
            for (int i=0;i<MainActivity.nombreFrigos;i++){
                if (newFrigoName.compareTo(MainActivity.listeFrigosNames.get(i)) == 0){
                    k++;
                }
            }

            if (k > 0){
                Toast toast = Toast.makeText(getApplicationContext(), "Un réfrigérateur possédant ce nom existe déjà", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else{//On modifie les données sauvegardées

                try {//Ajoute le nom du nouveau frigo dans frigos_file.txt (ne remplace pas le fichier mais écrit à la suite)
                    OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_APPEND));
                    BufferedWriter bw = new BufferedWriter(outStream);
                    PrintWriter out = new PrintWriter(bw);
                    out.println(newFrigoName);
                    out.close();
                    Toast.makeText(getApplicationContext(), "écriture nom ok", Toast.LENGTH_SHORT).show();
                } catch (java.io.IOException e) {
                    Toast.makeText(getApplicationContext(), "erreur écriture frigo", Toast.LENGTH_SHORT).show();
                }

                try { //Sauve le nombre total de frigos dans les données, remplace le fichier (et donc la valeur) précédent
                    FileOutputStream fos = openFileOutput("nombre_frigos_file.txt", Context.MODE_PRIVATE);
                    fos.write(N);
                    fos.close();
                    Toast.makeText(getApplicationContext(), "nb frig sauv, val=" + N, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();}



                //CODE TEST PRECEDENT :
                MainActivity.dataSimulee.dataFrigoNames.add(newFrigoName);
                Refrigerateur newFrigo = new Refrigerateur(newFrigoName);
                MainActivity.dataSimulee.dataListeFrigos.add(newFrigo); //Crée DEUX nouveaux fichiers textes, pour l'instant
                //vides, qui contiendront pour l'un le nom des boîtes de ce frigo, pour l'autre le nombre de boîtes contenues par le frigo
                //(cf description dans DataSimulée).
                //FIN DU CODE TEST PRECEDENT


                startActivity(intent); //Renvoie sur la page d'accueil. La page d'acceuil se charge elle même de mettre à jour les données modifiées
            }
        }
    }
}
