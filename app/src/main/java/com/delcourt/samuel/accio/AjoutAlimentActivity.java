package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;


public class AjoutAlimentActivity extends ActionBarActivity {

    private static String product = null;
    private static String manufacturer = null;
    private static int numConnection = 0;
    private Thread thread = new Thread(new AjoutAlimThread());
    private int connecter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ajout_aliment);
            if(Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor()==0){
                setConnecter(1);
                thread.start();//On lance le thread qui gère les accès aux bdd etc.
            }
            else {
                Toast.makeText(getApplicationContext(),"Erreur : vous n'avez pas de connexion à internet", Toast.LENGTH_SHORT).show();
            }
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

    @Override
    protected void onStop() {
        super.onStop();
        connecter=0;
        //thread.interrupt();//Interruption du thread : plus besoin d'essayer de se connecter aux bdd qd on quitte l'activité
    }

    private void setConnecter(int i){connecter=i;}

    private int getConnecter(){return connecter;}

    class RequeteAjoutAliment extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            //String result = "";

            //InputStream is = null;

            String httpResponse;
            httpResponse = null;

            // Envoi de la requÃªte avec HTTPGet

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/check-manufacturer-product.php");
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                httpResponse = EntityUtils.toString(entity);
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return httpResponse;
        }

        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {
            if(resultat.compareTo("product")==0){
                afficheFenetreProduct();
            } else if (resultat.compareTo("manufacturer")==0){
                afficheFenetreManufacturer();
            } else {
                try {
                    connection();
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(),"erreur connection - postEx principale", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
                    AjoutAlimentActivity.product=value.replace(" ","+");
                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    new RequeteAjoutAlimentProduct().execute();
                }
            });
            alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
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
                    AjoutAlimentActivity.manufacturer=value.replace(" ","+");
                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    new RequeteAjoutAlimentManufacturer().execute();
                }
            });
            alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            alert.show();
        }
    }

    public void connection() throws InterruptedException {
        //On affiche un texte, il se réactualise en permanence
        TextView textElement = (TextView) findViewById(R.id.message_ajout_aliment);
        textElement.setText("Attente d'une requête. Boucle n°"+numConnection+"\nMessage product :"+product+
                "\nMessage manufacturer :"+manufacturer);
        thread.sleep(2000);//On attend 2 s
        if(getConnecter()==1){
            numConnection++;
            new RequeteAjoutAliment().execute();
        }
    }

    class RequeteAjoutAlimentProduct extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/add-content.php?file=product&name="+product);
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
            Toast toast = Toast.makeText(getApplicationContext(), "Nom bien récupéré", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            try {
                connection();
            } catch (InterruptedException e) {
                Toast toast2 = Toast.makeText(getApplicationContext(), "Problème : Connexion", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast2.show();
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
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/add-content.php?file=manufacturer&name="+manufacturer);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return result.toString();
        }

        protected void onPostExecute(String resultat) {
            Toast toast = Toast.makeText(getApplicationContext(), "Marque bien récupérée", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            try {
                connection();
            } catch (InterruptedException e) {
                Toast toast2 = Toast.makeText(getApplicationContext(), "Problème : Connexion", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast2.show();
            }
        }
    }

    class AjoutAlimThread implements Runnable{

        @Override
        public void run() {
            try {
                connection();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"erreur connection-thread", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
