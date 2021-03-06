package com.delcourt.samuel.accio.create_new_object_activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;
import java.io.BufferedWriter;
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

        Intent intent = new Intent(this,AccueilActivity.class);

        EditText editText = (EditText) findViewById(R.id.nameFrigo); //Récupère le nom du frigo
        String newFrigoName = editText.getText().toString();
        int N = AccueilActivity.nombreFrigos + 1;


        if (newFrigoName.length() == 0){ //Si le nom est vide, envoie un message
            Toast toast = Toast.makeText(getApplicationContext(), "Nom invalide", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else { //On s'assure qu'aucun frigo du même nom n'a encore été créé
            for (int i=0;i< AccueilActivity.nombreFrigos;i++){
                if (newFrigoName.compareTo(AccueilActivity.listeFrigosNames.get(i)) == 0){
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
                } catch (java.io.IOException e) {
                    Toast.makeText(getApplicationContext(), "erreur écriture frigo", Toast.LENGTH_SHORT).show();
                }
                try {//Crée le fichier contenant la liste des boîtes de ce frigo
                    OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(newFrigoName + "Boxes.txt",MODE_APPEND));
                    outStream.close();
                } catch (java.io.IOException e) {
                    Toast.makeText(getApplicationContext(), "erreur création fichier liste boîtes", Toast.LENGTH_SHORT).show();
                }

                startActivity(intent); //Renvoie sur la page d'accueil. La page d'acceuil se charge elle même de mettre à jour les données modifiées
            }
        }
    }
}