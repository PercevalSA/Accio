package com.delcourt.samuel.accio.options_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.ListeBoitesActivity;
import com.delcourt.samuel.accio.R;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FrigoOptionsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_frigo_options);

            Button button = (Button) findViewById(R.id.create);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendMessageRenameFrigo();
                }
            });

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }

        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_frigo);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frigo_options, menu);
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

    public void sendMessageRenameFrigo(){
        int k = 0;

        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_frigo);
        final String newName = editText.getText().toString();

        //On s'assure qu'aucun frigo du même nom n'a encore été créé
        for (int i=0;i< AccueilActivity.getListeFrigosNames().size();i++){
            if (newName.compareTo(AccueilActivity.getListeFrigosNames().get(i)) == 0){
                k++;
            }
        }
        if(newName.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nouveau nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            //Désactive le bouton le temps de l'affichage du Toast
            Button button = (Button) findViewById(R.id.bouton_renommer_frigo);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                }
            });
            ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(2);
            stpe.schedule(new ToastShown(button),2500, TimeUnit.MILLISECONDS);

        }else if (k > 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Un réfrigérateur possédant ce nom existe déjà", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            //Désactive le bouton le temps de l'affichage du Toast
            Button button = (Button) findViewById(R.id.bouton_renommer_frigo);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                }
            });
            ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(2);
            stpe.schedule(new ToastShown(button),2500, TimeUnit.MILLISECONDS);

        }
        else{
            //on créé une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(FrigoOptionsActivity.this);
            //on attribue un titre à notre boite de dialogue
            adb.setTitle("Confirmation");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez-vous renommer le réfrigérateur ''" + ListeBoitesActivity.getRefrigerateur().getName() + "'' en : ''" + newName + "'' ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    rename(newName);
                }
            });
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            //on affiche la boite de dialogue
            adb.show();
        }
    }

    public void rename(String newName){
        String nameFrigo = ListeBoitesActivity.getRefrigerateur().getName();

        //On change le nom du frigo dans la liste dynamique :
        int index = AccueilActivity.getListeFrigosNames().indexOf(nameFrigo);
        AccueilActivity.getListeFrigosNames().set(index, newName);

        //On change le nom du frigo dans ListeBoitesActivity
        ListeBoitesActivity.getRefrigerateur().setName(newName);

        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<AccueilActivity.getListeFrigosNames().size();i++){
                out2.println(AccueilActivity.getListeFrigosNames().get(i));
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste frigos", Toast.LENGTH_SHORT).show();
        }

        listeboites(newName);

        Intent intent = new Intent(this,ListeBoitesActivity.class);
        startActivity(intent);
    }

    public void listeboites(String newName){
        //On crée le fichier contenant la liste des boîtes etc

        try {//Ajoute le nom du nouveau frigo dans frigos_file.txt (ne remplace pas le fichier mais écrit à la suite)
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(newName + "Boxes.txt",MODE_APPEND));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out = new PrintWriter(bw);
            for(int i =0;i<ListeBoitesActivity.getRefrigerateur().getBoxes().size();i++){
                out.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getReferenceBdd());
                out.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getName());
                out.println(ListeBoitesActivity.getRefrigerateur().getBoxes().get(i).getType());
            }
            out.close();
        } catch (java.io.IOException e) {
            Toast.makeText(getApplicationContext(), "erreur recréation liste boîtes", Toast.LENGTH_SHORT).show();
        }
    }

    class ToastShown implements Runnable {

        private Button button;

        public ToastShown(Button button){
            this.button = button;
        }

        public void run(){
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendMessageRenameFrigo();
                }
            });
        }

    }
}
