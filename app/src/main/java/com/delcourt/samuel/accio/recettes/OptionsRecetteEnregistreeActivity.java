package com.delcourt.samuel.accio.recettes;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class OptionsRecetteEnregistreeActivity extends ActionBarActivity {

    private static String recetteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_options_recette_enregistree);

        } catch (Exception e){
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_recette_enregistree, menu);
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

    public static void setRecetteName(String newName){recetteName=newName;}

    public void sendMessageRename(View view){
        int k = 0;

        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_recette);
        final String newName = editText.getText().toString();

        //On s'assure qu'aucune recette du même nom n'a encore été créé
        for (int i=0;i< MenuRecettesActivity.getListeRecettesEnregistrées().size();i++){
            if (newName.compareTo(MenuRecettesActivity.getListeRecettesEnregistrées().get(i).getName()) == 0){
                k++;
            }
        }

        if(newName.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nouveau nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else if (k > 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Une recette possédant ce nom existe déjà", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else{
            //on créé une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(OptionsRecetteEnregistreeActivity.this);
            //on attribue un titre à notre boite de dialogue
            adb.setTitle("Confirmation");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez-vous renommer la recette ''" + recetteName + "'' en : ''" + newName + "'' ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    rename(newName);
                }
            });
            //on affiche la boite de dialogue
            adb.show();
        }


    }

    public void sendMessageDelete(View view){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(OptionsRecetteEnregistreeActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer la boite " + recetteName+" ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void rename(String newName){

        //On change le nom de la boîte dans la liste dynamique :
        for(int j =0;j<MenuRecettesActivity.getListeRecettesEnregistrées().size();j++){
            if(MenuRecettesActivity.getListeRecettesEnregistrées().get(j).getName().compareTo(recetteName)==0){
                MenuRecettesActivity.getListeRecettesEnregistrées().get(j).setName(newName);
            }
        }
        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("recettes_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<MenuRecettesActivity.getListeRecettesEnregistrées().size();i++){
                out2.println(MenuRecettesActivity.getListeRecettesEnregistrées().get(i).getName());
                out2.println(MenuRecettesActivity.getListeRecettesEnregistrées().get(i).getAdresseWeb());
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,MenuRecettesActivity.class);
        startActivity(intent);
    }

    public void delete(){
        //On supprime la boîte dans la liste dynamique :
        for(int j =0;j<MenuRecettesActivity.getListeRecettesEnregistrées().size();j++){
            if(MenuRecettesActivity.getListeRecettesEnregistrées().get(j).getName().compareTo(recetteName)==0){
                MenuRecettesActivity.getListeRecettesEnregistrées().remove(j);
            }
        }
        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("recettes_file.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<MenuRecettesActivity.getListeRecettesEnregistrées().size();i++){
                out2.println(MenuRecettesActivity.getListeRecettesEnregistrées().get(i).getName());
                out2.println(MenuRecettesActivity.getListeRecettesEnregistrées().get(i).getAdresseWeb());
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,MenuRecettesActivity.class);
        startActivity(intent);
    }

}
