package com.delcourt.samuel.accio.create_new_object_activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.help_activities.AideNouvelleBoiteActivity;
import com.delcourt.samuel.accio.ListeBoitesActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
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
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NewBoxActivity extends ActionBarActivity {

    private String typeBox = null;
    private String newBoiteName= null;

    //Attributs nécessaires pour la connexion à la base de données
    private static ArrayList<String> listeRefBdd;
    private String nameFrigo = null;
    private String newBoiteNameEnco= null;
    private String typeBoxEnco = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_box);
            listeRefBdd = new ArrayList<>();
            getTypes();
        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_box, menu);
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

    public void sendMessageNewBox(View view) throws UnsupportedEncodingException {



            //RECUPERE LES DIFFERENTES INFOS

        //Récupère le nom de la boîte
        EditText editText = (EditText) findViewById(R.id.name_boite);
        newBoiteName = editText.getText().toString();

        //Récupère le numéro (identifiant de la boîte)
        EditText editText2 = (EditText) findViewById(R.id.numero_boite);
        String numeroBoite = editText2.getText().toString();

        //Récupère le code de la boîte
        EditText editText3 = (EditText) findViewById(R.id.code_activation_boite);
        String codeBoite = editText3.getText().toString();

            //ON S'ASSURE QUE LES INFOS SONT COHERENTES

        if (numeroBoite.length() == 0 || codeBoite.compareTo("accio") != 0){//Le code entré est incorrect ou le numéro de la boîte n'a pas été saisi
            //REMARQUE : pour l'instant, le code de toutes les boîtes est : accio
            Toast toast = Toast.makeText(getApplicationContext(), "Le code entré est incorrect", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
        else {
            if (newBoiteName.length() == 0){ //Si le nom est vide
                Toast toast = Toast.makeText(getApplicationContext(), "Le nom de la nouvelle boîte n'a pas été renseigné", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

            else{//On s'assure qu'un seul type de boîte a été déclaré
                if (typeBox==null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas choisi le type de la boîte", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else{//IL FAUT COMMUNIQUER CETTE INFO A LA BDD !

                    //On s'assure qu'aucune boîte du même nom n'a encore été créée
                    int k = 0;
                    for (int i=0;i< RefrigerateurActivity.getRefrigerateur().getBoxes().size();i++){
                        if (newBoiteName.compareTo(RefrigerateurActivity.getRefrigerateur().getBoxes().get(i).getName()) == 0){
                            k++;
                        }
                    }
                    if(k > 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Une boîte possédant ce nom existe déjà dans ce réfrigérateur", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }

                    else {//Dans ce cas, c'est bon, on peut créer la nouvelle boîte
                            //Ajoute le nom du nouveau frigo dans frigos_file.txt (ne remplace pas le fichier mais écrit à la suite)
                            nameFrigo = RefrigerateurActivity.getRefrigerateur().getName();
                            newBoiteNameEnco = URLEncoder.encode(newBoiteName, "UTF-8");
                            typeBoxEnco = URLEncoder.encode(typeBox,"UTF-8");
                        // C'est ici qu'on se connecte à la BDD
                        new CreaBoite().execute();
                        TextView textElement = (TextView) findViewById(R.id.message_new_box);
                        textElement.setText("Connexion à la base de données");
                        /*try{new CreaBoite().execute();
                            TextView textElement = (TextView) findViewById(R.id.message_new_box);
                            textElement.setText("Connexion à la base de données");}
                        catch(Exception e){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Erreur de connexion à la base de données", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }*/



                    }


                }

            }

        }

    }

    class CreaBoite extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/creaboite.php?nomBoite=" +newBoiteNameEnco+ "&cateBoite=" + typeBoxEnco);
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
                    NewBoxActivity.listeRefBdd.add(json_data.getString(0));

                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String result) {

            TextView textElement = (TextView) findViewById(R.id.message_new_box);
            textElement.setText(" ");

            try{String RefBdd = listeRefBdd.get(0);
                Intent intent = new Intent(getApplicationContext(),ListeBoitesActivity.class);
                try {
                    OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt", MODE_APPEND));
                    BufferedWriter bw = new BufferedWriter(outStream);
                    PrintWriter out2 = new PrintWriter(bw);
                    out2.println(RefBdd);
                    out2.println(newBoiteName);
                    out2.println(typeBox);
                    out2.close();

                    //L'ensemble du réfrigérateur n'a pas encore été recréé : il faut donc ajouter cette nouvelle boîte à la liste dynamique
                    Box newBox = new Box(RefBdd, newBoiteName, typeBox);
                    RefrigerateurActivity.getRefrigerateur().getBoxes().add(newBox);

                    //la boîte a été crée, on retourne sur l'activité précédente :
                    startActivity(intent);

                } catch (java.io.IOException e) {
                    Toast.makeText(getApplicationContext(), "erreur écriture boîte", Toast.LENGTH_SHORT).show();
                }
            }
            catch(IndexOutOfBoundsException e){//Cette exception est envoyée lorsqu'il y a eu un problème de connexion
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Erreur de connexion à la base de données", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    }




    public void sendMessageHelp(View view){
        Intent intent = new Intent(this,AideNouvelleBoiteActivity.class);
        startActivity(intent);
    }

    void getTypes() {

        ImageView imFruits = (ImageView) findViewById(R.id.imgNewBox1);
        imFruits.setImageResource(R.drawable.ic_fruit);
        CheckBox fruits = (CheckBox) findViewById(R.id.checkNewBox1);
        fruits.setText("Fruits");

        ImageView imLegumes = (ImageView) findViewById(R.id.imgNewBox2);
        imLegumes.setImageResource(R.drawable.ic_legume);
        CheckBox legumes = (CheckBox) findViewById(R.id.checkNewBox2);
        legumes.setText("Légumes");

        ImageView imProdLait = (ImageView) findViewById(R.id.imgNewBox3);
        imProdLait.setImageResource(R.drawable.ic_produit_laitier);
        CheckBox prodLait = (CheckBox) findViewById(R.id.checkNewBox3);
        prodLait.setText("Produits laitiers");

        ImageView imFish = (ImageView) findViewById(R.id.imgNewBox4);
        imFish.setImageResource(R.drawable.ic_poisson);
        CheckBox fish = (CheckBox) findViewById(R.id.checkNewBox4);
        fish.setText("Poisson");

        ImageView imViande = (ImageView) findViewById(R.id.imgNewBox5);
        imViande.setImageResource(R.drawable.ic_viande);
        CheckBox viande = (CheckBox) findViewById(R.id.checkNewBox5);
        viande.setText("Viande");

        ImageView imCond = (ImageView) findViewById(R.id.imgNewBox6);
        imCond.setImageResource(R.drawable.ic_condiment);
        CheckBox condiments = (CheckBox) findViewById(R.id.checkNewBox6);
        condiments.setText("Sauces et condiments");
    }

    public void selectedBox1(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox1);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox2);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox3);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox4);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox5);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox6);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Fruits";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

    public void selectedBox2(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox2);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox1);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox3);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox4);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox5);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox6);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Légumes";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

    public void selectedBox3(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox3);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox2);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox1);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox4);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox5);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox6);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Produits laitiers";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

    public void selectedBox4(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox4);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox2);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox3);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox1);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox5);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox6);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Poisson";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

    public void selectedBox5(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox5);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox2);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox3);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox4);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox1);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox6);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Viande";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

    public void selectedBox6(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkNewBox6);
        boolean checked = checkBox.isChecked();
        if (checked) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkNewBox2);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox3);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox4);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox5);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.checkNewBox1);
            checked = checkBox.isChecked();
            if (checked) {
                checkBox2.setChecked(false);
            }
            typeBox="Sauces et condiments";
        } else {//On remet typeBox à null :
            typeBox=null;
        }
    }

}
