package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.structures.Aliment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class AjoutAlimentActivity extends ActionBarActivity {

    private static String product = null;
    private static String manufacturer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ajout_aliment);
            connection();
        }
        catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajout_aliment, menu);
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

    class RequeteAjoutAliment extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/check-manufacturer-product.php");
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return result.toString();
        }

        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {
            if(resultat.compareTo("product")==0){
                afficheFenetreProduct();
            } else if (resultat.compareTo("manufacturer")==0){
                afficheFenetreManufacturer();
            } else {
                connection();
            }


        }

        public void afficheFenetreProduct(){
            //on créé une boite de dialogue
            AlertDialog.Builder alert = new AlertDialog.Builder(AjoutAlimentActivity.this);
            alert.setTitle("Produit");
            alert.setMessage("Indiquer le nom du produit :");
            final EditText editText = new EditText(AjoutAlimentActivity.this);
            alert.setView(editText);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String value = editText.getText().toString().trim();
                    AjoutAlimentActivity.product=value;
                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    new RequeteAjoutAlimentProduct().execute();
                }
            });
            alert.show();
        }

        public void afficheFenetreManufacturer(){
            AlertDialog.Builder alert = new AlertDialog.Builder(AjoutAlimentActivity.this);
            alert.setTitle("Marque");
            alert.setMessage("Indiquer la marque du produit :");
            final EditText editText = new EditText(AjoutAlimentActivity.this);
            alert.setView(editText);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String value = editText.getText().toString().trim();
                    AjoutAlimentActivity.manufacturer=value;
                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    new RequeteAjoutAlimentManufacturer().execute();
                }
            });
            alert.show();
        }
    }

    public void connection(){
        new RequeteAjoutAliment().execute();
    }


    class RequeteAjoutAlimentProduct extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/connection-add-product-name.php?product="+product);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return result.toString();
        }

        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {
            try {
                Thread.sleep(2000);
                connection();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "sleep pas réussi", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class RequeteAjoutAlimentManufacturer extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/connection-add-manufacturer-name.php?manufacturer="+manufacturer);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return result.toString();
        }

        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {
            try {
                Thread.sleep(2000);
                connection();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "sleep pas réussi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
