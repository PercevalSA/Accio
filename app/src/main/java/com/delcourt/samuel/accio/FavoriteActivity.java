package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.interaction.BDD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FavoriteActivity extends ActionBarActivity {

    String result = null;
    static ArrayList<String> listeAlimentsAffichage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        ListView listAliments=(ListView)findViewById(R.id.listeViewListeAliments1);
        listeAlimentsAffichage = new ArrayList<>();

        new BDD2().execute();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listeAlimentsAffichage);
        listAliments.setAdapter(arrayAdapter);


    }

    class BDD2 extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // aliment recherché
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("nomCategorie", "Legume"));
            ArrayList<String> donnees = new ArrayList<String>();

            // Envoi de la requête avec HTTPPost
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.1.52/pact/connection2.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la réponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaîne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des données JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script. un code html pouquoi ?
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    donnees.add(json_data.getString("Nom"));
                    Log.i("log_tag", "BoiteID: " + json_data.getInt("BoiteID") +
                                    ", Nom: " + json_data.getString("Nom") +
                                    ", Categorie: " + json_data.getString("Categorie")
                    );

                    //Met les données ds la liste à afficher
                    FavoriteActivity.listeAlimentsAffichage.add(json_data.getString("Nom"));


                    result += "\n\t" + jArray.getJSONObject(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String result) {

        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
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
}