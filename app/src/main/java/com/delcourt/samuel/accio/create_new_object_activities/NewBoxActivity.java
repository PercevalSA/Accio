package com.delcourt.samuel.accio.create_new_object_activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.delcourt.samuel.accio.R;

import java.util.ArrayList;

public class NewBoxActivity extends ActionBarActivity {

    public ArrayList<String> listTypesBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_box);

        // Get the reference of List
        ListView animalList=(ListView)findViewById(R.id.listViewTypeBox);

        getTypesNames();
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listTypesBoxes);
        // Set The Adapter
        animalList.setAdapter(arrayAdapter);

        //register onClickListener to handle click events on each item
        animalList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {

                String selected=listTypesBoxes.get(position);

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
}
