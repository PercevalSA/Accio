package com.delcourt.samuel.accio.create_new_object_activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NewBoxActivity extends ActionBarActivity {

    public ArrayList<String> listTypesBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_box);

        ListView typesList = (ListView) findViewById(R.id.listViewTypeBox);

        // Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

        // On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        listTypesBoxes = new ArrayList<>();
        getTypesNames();
        for (int i = 0; i < 6; i++) {
            map = new HashMap<String, String>();
            map.put("type", listTypesBoxes.get(i));
            listItem.add(map);
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.liste_categories,
                new String[] {"type"}, new int[] {R.id.type});

        //On attribue à notre listView l'adapter que l'on vient de créer
        typesList.setAdapter(mSchedule);



        //register onClickListener to handle click events on each item
        typesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                
            }
        });
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

    public void sendMessageNewBox(View view){

        Intent intent = new Intent(this,AccueilActivity.class);
            //RECUPERE LES DIFFERENTES INFOS
        //Récupère le nom de la boîte
        EditText editText = (EditText) findViewById(R.id.name_boite);
        String newBoiteName = editText.getText().toString();
        //Récupère le numéro (identifiant de la boîte)
        EditText editText2 = (EditText) findViewById(R.id.numero_boite);
        String numeroBoite = editText2.getText().toString();
        //Récupère le code de la boîte
        EditText editText3 = (EditText) findViewById(R.id.code_activation_boite);
        String codeBoite = editText3.getText().toString();

    }

    public void sendMessageHelp(View view){

    }

    void getTypesNames() {
        listTypesBoxes.add("Fruits");
        listTypesBoxes.add("Légumes");
        listTypesBoxes.add("Produits laitiers");
        listTypesBoxes.add("Poisson");
        listTypesBoxes.add("Viande");
        listTypesBoxes.add("Sauces et condiments");
    }

    public void MyHandler(View v) {
        CheckBox cb = (CheckBox) v;
        //on récupère la position à l'aide du tag défini dans la classe MyListAdapter
        int position = Integer.parseInt(cb.getTag().toString());

        //On change la couleur
        if (cb.isChecked()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Coché", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Décoché", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
