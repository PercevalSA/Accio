package com.delcourt.samuel.accio.recettes;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RecetteMarmitonActivity extends ActionBarActivity {

    private static String adresseWeb;
    private static int autorisationAffichageToast=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recette_marmiton);

            if(Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor()==0) {
                //Si il y a une connexion à Internet :
                WebView webview = (WebView)findViewById(R.id.marmiton_web_view);
                webview.setWebViewClient(new WebViewClient());
                webview.getSettings().setJavaScriptEnabled(true);
                //on prend le userAgent en desktop pour avoir les option de recherche
                String ua = "Mozilla/5.0 (X11; U; Linux i686; fr-FR; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
                webview.getSettings().setUserAgentString(ua);
                webview.loadUrl(adresseWeb);
            }
            else{
                WebView webview = (WebView)findViewById(R.id.marmiton_web_view);
                webview.setVisibility(View.INVISIBLE);
                TextView text = (TextView) findViewById(R.id.message_chargement_marmiton);
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
        getMenuInflater().inflate(R.menu.menu_recette_marmiton, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_refresh_recette_enregitree:
                sendMessageRefresh();
                return true;
            case R.id.action_enregister:
                sendMessageEnregistrerRecette();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setAutorisationAffichageToast(int i){autorisationAffichageToast=i;}

    public int getAutorisationAffichageToast(){return autorisationAffichageToast;}

    public void sendMessageRefresh(){
        Intent intent = new Intent(this,RecetteMarmitonActivity.class);
        startActivity(intent);
    }

    public static void setAdresseWeb(String adresse){adresseWeb=adresse;}

    public void sendMessageEnregistrerRecette(){
        WebView webview = (WebView)findViewById(R.id.marmiton_web_view);
        if(webview.getVisibility()==View.VISIBLE) {
            //Transmet l'adresse à enregistrer
            EnregistrerRecetteActivity.setUrl(webview.getOriginalUrl());

            Intent intent = new Intent(this,EnregistrerRecetteActivity.class);
            startActivity(intent);
        } else if(getAutorisationAffichageToast()==1){
            Toast toast = Toast.makeText(getApplicationContext(), "Aucune recette n'est affichée." +
                    "\nAffichez une recette avant de l'enregistrer", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            setAutorisationAffichageToast(0);
            ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(2);
            stpe.schedule(new ToastShown(),2500, TimeUnit.MILLISECONDS);
        }

    }

    class ToastShown implements Runnable {

        public ToastShown(){}

        public void run(){
            setAutorisationAffichageToast(1);
        }

    }
}
