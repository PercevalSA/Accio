package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.recettes.RecetteMarmitonActivity;

public class OptionsRecettesActivity extends ActionBarActivity {

    private boolean vegetarien = false;
    private boolean sansCuisson = false;
    private int typePlat=0;
    private int difficulte=0;
    private int cout=0;
    protected static String aliments;//Aliments à rechercher, à mettre sous le bon format (ok pour l'exemple actuel)

    boolean food=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_recettes);

        Toast toast = Toast.makeText(getApplicationContext(), "A adapter", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_recettes, menu);
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

    public void selectedVegetarien(View view){
        if(vegetarien==true){vegetarien = false;
            Toast.makeText(getApplicationContext(), "végétarien décoché",Toast.LENGTH_SHORT).show();
        } else {vegetarien = true;
            Toast.makeText(getApplicationContext(), "végétarien coché",Toast.LENGTH_SHORT).show();}

    }

    public void selectedSansCuisson(View view){
        if (sansCuisson==true){sansCuisson = false;}
        else {sansCuisson = true;}
    }

    public void selectedTypePlat1(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.type_plat_type1);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.type_plat_type2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.type_plat_type3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            typePlat = 1;
        } else {//On remet typePlat à 0 :
            typePlat = 0;
        }
    }

    public void selectedTypePlat2(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.type_plat_type2);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.type_plat_type1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.type_plat_type3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            typePlat = 2;
        } else {//On remet typePlat à 0 :
            typePlat = 0;
        }
    }

    public void selectedTypePlat3(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.type_plat_type3);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.type_plat_type2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.type_plat_type1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            typePlat = 3;
        } else {//On remet typePlat à 0 :
            typePlat = 0;
        }
    }

    public void selectedDifficulte1(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.difficulte1);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.difficulte2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte4);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            difficulte = 1;
        } else {//On remet typePlat à 0 :
            difficulte = 0;
        }
    }

    public void selectedDifficulte2(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.difficulte2);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.difficulte1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte4);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            difficulte = 2;
        } else {//On remet typePlat à 0 :
            difficulte = 0;
        }
    }

    public void selectedDifficulte3(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.difficulte3);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.difficulte2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte4);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            difficulte = 3;
        } else {//On remet typePlat à 0 :
            difficulte = 0;
        }
    }

    public void selectedDifficulte4(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.difficulte4);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.difficulte2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.difficulte3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            difficulte = 4;
        } else {//On remet difficulte à 0 :
            difficulte = 0;
        }
    }

    public void selectedCout1(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.cout1);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.cout2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.cout3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            cout = 1;
        } else {//On remet typePlat à 0 :
            cout = 0;
        }
    }

    public void selectedCout2(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.cout2);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.cout1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.cout3);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            cout = 2;
        } else {//On remet typePlat à 0 :
            cout = 0;
        }
    }

    public void selectedCout3(View view){
        // Is the view now checked?
        CheckBox checkBox = (CheckBox) findViewById(R.id.cout3);
        boolean checked = checkBox.isChecked();
        if (checked == true) {//Si on vient de cocher, on décoche tous les autres

            CheckBox checkBox2 = (CheckBox) findViewById(R.id.cout2);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            checkBox2 = (CheckBox) findViewById(R.id.cout1);
            checked = checkBox.isChecked();
            if (checked == true) {
                checkBox2.setChecked(false);
            }

            //On indique que typePlat vaut 1:
            cout = 3;
        } else {//On remet typePlat à 0 :
            cout = 0;
        }
    }

    public void sendMessageAfficheRecette(View view){
        String adresseWeb = getURL(aliments, vegetarien, sansCuisson, food, cout, difficulte,typePlat);
        RecetteMarmitonActivity.adresseWeb = adresseWeb;

        Toast toast = Toast.makeText(getApplicationContext(), adresseWeb, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        Intent intent = new Intent(this,RecetteMarmitonActivity.class);
        startActivity(intent);
    }

    public static String getURL (String recherche,
                                 boolean vegan,
                                 boolean cru,
                                 boolean Food,
                                 int cost,
                                 int difficult,
                                 int typePlat) {

        String url="http://m.marmiton.org/recettes/recherche.aspx?aqt="+recherche;

//+"&pht=1"
        switch(typePlat){
            case 1 : url+="&dt=entree";
                break;

            case 2 : url+="&dt=platprincipal";
                break;

            case 3 : url+="&dt=dessert";
                break;
        }

        if (vegan == true)
            url+="&veg=1";
        if (cru == true)
            url+="&rct=1";
     //   if (Food == true)
       //     url+="&st=1";


        switch (cost) {
            case 1 : url+="&exp=1";
                break;

            case 2 : url+="&exp=2";
                break;

            case 3 : url+="&exp=3";
                break;
        }

        switch (difficult) {
            case 1 : url+="&dif=1";
                break;

            case 2 : url+="&dif=2";
                break;

            case 3 : url+="&dif=3";
                break;

            case 4 : url+="&dif=4";
                break;
        }

        Log.i("getURL", "URL: "+url);
        return url;


    }

}






/*
    adresse de recherche (uniquement pour les recettes)
    http://www.marmiton.org/recettes/recherche.aspx?aqt=$MOTS_CLEF

        Option pour la recherche :
        photo : &pht=1
        vegetarien : &veg=1
        sans cuisson ; &rct=1

        uniquement dans les ingrédients : &st=1

       cout :
           bon marché : &exp=1
           moyen : &exp=2
           assez cher : &exp=3

       difficultée :
           très facie : &dif=1
           facile : &dif=2
           moyenne : &dif=3
           difficile : &dif=4

       plat principal :
       entrée : &dt=entree
       dessert : &dt=dessert
       plat ppl : &dt=platprincipal
           */