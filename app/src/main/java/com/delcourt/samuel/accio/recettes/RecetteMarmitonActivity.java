package com.delcourt.samuel.accio.recettes;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;


public class RecetteMarmitonActivity extends ActionBarActivity {

    protected static String adresseWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recette_marmiton);

            WebView webview = (WebView)findViewById(R.id.marmiton_web_view);
            webview.setWebViewClient(new WebViewClient());
            webview.getSettings().setJavaScriptEnabled(true);
            //on prend le userAgent en desktop pour avoir les option de recherche
            String ua = "Mozilla/5.0 (X11; U; Linux i686; fr-FR; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
            webview.getSettings().setUserAgentString(ua);
            webview.loadUrl(adresseWeb);

        } catch (Exception e){
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessageEnregistrerRecette(View view){
        //Transmet l'adresse à enregistrer
        WebView webview = (WebView)findViewById(R.id.marmiton_web_view);
        EnregistrerRecetteActivity.url = webview.getOriginalUrl();

        Intent intent = new Intent(this,EnregistrerRecetteActivity.class);
        startActivity(intent);
    }
}
