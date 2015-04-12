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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.help_activities.AideAliment;
import com.delcourt.samuel.accio.options_activities.BoxOptionsActivity;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AlimentActivity extends ActionBarActivity {

    private static int boxIndex;
    private static int alimentIndex;
    private String alimID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_aliment);
            afficheEnTete();

        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aliment, menu);
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

    public static void setBoxIndex(int index){boxIndex=index;}

    public static void setAlimentIndex(int index){alimentIndex=index;}

    public void afficheEnTete() throws ParseException {
        TextView textElement = (TextView) findViewById(R.id.boxName_AlimentActivity);
        textElement.setText("Boîte : "+
                ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getBoxName());

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
        textElement2.setText("(Réfrigérateur : " + ListeBoitesActivity.getRefrigerateur().getName() + ")");

        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).isAlimentFavori()==true){image.setImageResource(R.drawable.fav);
        }else{image.setImageResource(R.drawable.favn);}

        TextView textElement3 = (TextView) findViewById(R.id.nameAliment);
        textElement3.setText(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentName());

        TextView textElement4 = (TextView) findViewById(R.id.marqueAliment);
        if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentMarque()=="null"){textElement4.setText("");} else {
        textElement4.setText("Marque : " + ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentMarque());}


        TextView textElement5 = (TextView) findViewById(R.id.marqueHistorique);
        String strDate = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentHistorique();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date= df.parse(strDate);
        DateFormat newFormat = new SimpleDateFormat("d MMMM yyyy");
        strDate= newFormat.format(date);
        textElement5.setText("Cet aliment a été ajouté le " + strDate + ".");
    }

    public void sendMessageHelp(View view){
        Intent intent = new Intent(this,AideAliment.class);
        startActivity(intent);
    }

    public void sendMessageFavori(View view){
        AlertDialog.Builder adb = new AlertDialog.Builder(AlimentActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Favori");

        if(ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).isAlimentFavori()==false){
            adb.setMessage("Voulez-vous ajouter l'aliment : "+
                    ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentName()+" à la liste des favoris ?");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    declareFavori();
                }
            });
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            adb.show();
        } else{
            adb.setMessage("Voulez-vous retirer l'aliment : "+
                    ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getAlimentName()+" de la liste des favoris ?");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    declareNonFavori();
                }
            });
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            adb.show();
        }
    }

    public void declareFavori(){
        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).setFavori(true);
        image.setImageResource(R.drawable.fav);
        alimID=ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getalimID();
        new DeclareFavori().execute();

    }

    public void declareNonFavori(){
        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).setFavori(false);
        image.setImageResource(R.drawable.favn);
        alimID = ListeBoitesActivity.getRefrigerateur().getBoxes().get(boxIndex).getListeAliments().get(alimentIndex).getalimID();
        new DeclareNonFavori().execute();

    }

    class DeclareFavori extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/declarefavori.php?alimID="+ alimID);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la rÃ©ponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaÃ®ne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                //Log.i("lol", "COUCOU: "+ object.toString());
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);
                    //Met les donnÃ©es ds la liste Ã  afficher
                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }

    }

    class DeclareNonFavori extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/declarenonfavori.php?alimID="+ alimID);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la rÃ©ponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaÃ®ne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                //Log.i("lol", "COUCOU: "+ object.toString());
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);
                    //Met les donnÃ©es ds la liste Ã  afficher
                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }

    }

}
