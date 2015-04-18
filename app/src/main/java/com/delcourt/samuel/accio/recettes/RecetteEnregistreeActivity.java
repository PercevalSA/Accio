package com.delcourt.samuel.accio.recettes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RecetteEnregistreeActivity extends ActionBarActivity {

    private static String adresseWeb;
    private static String recetteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recette_favorie);

            if(Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor()==0){
                //Si il y a une connexion à Internet :
                WebView webview = (WebView)findViewById(R.id.marmiton_web_view_favori);
                webview.setWebViewClient(new WebViewClient());
                webview.getSettings().setJavaScriptEnabled(true);
                //on prend le userAgent en desktop pour avoir les option de recherche
                String ua = "Mozilla/5.0 (X11; U; Linux i686; fr-FR; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
                webview.getSettings().setUserAgentString(ua);
                webview.loadUrl(adresseWeb);
            }
            else {
                TextView text = (TextView) findViewById(R.id.message_chargement_marmiton2);
                text.setText("Vous n'avez pas de connexion à internet");
            }

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recette_favorie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_rename_recette:
                sendMessageRename();
                return true;
            case R.id.action_delete_recette:
                sendMessageDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setRecetteName(String name){
        recetteName=name;
    }

    public static void setAdresseWeb(String adresse){adresseWeb=adresse;}

    public void sendMessageOptionsRecette(View view){
        Intent intent = new Intent(this,OptionsRecetteEnregistreeActivity.class);
        startActivity(intent);
    }

    public void sendMessageDelete(){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(RecetteEnregistreeActivity.this);
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
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        //on affiche la boite de dialogue
        adb.show();
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

    public void sendMessageRename(){
        Intent intent = new Intent(this, OptionsRecetteEnregistreeActivity.class);
        startActivity(intent);
    }
}
