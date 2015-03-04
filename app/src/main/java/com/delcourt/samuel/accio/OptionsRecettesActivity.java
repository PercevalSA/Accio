package com.delcourt.samuel.accio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.delcourt.samuel.accio.R;

public class OptionsRecettesActivity extends ActionBarActivity {

    private boolean vegetarien = false;
    private boolean sansCuisson = false;
    private int typePlat=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_recettes);
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

    public void sendMessageAfficheRecette(int position){
        //RECUPERER L'ADRESSE

        /*String adresseWeb = A RECUPERER;
        RecetteMarmitonActivity.adresseWeb = adresseWeb;*/
        Intent intent = new Intent(this,OptionsRecettesActivity.class);
        startActivity(intent);
    }
}
