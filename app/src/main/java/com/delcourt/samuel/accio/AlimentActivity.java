package com.delcourt.samuel.accio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.delcourt.samuel.accio.help_activities.AideAliment;
import com.delcourt.samuel.accio.options_activities.BoxOptionsActivity;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;


public class AlimentActivity extends ActionBarActivity {

    public static String boiteName;
    public static Aliment aliment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliment);
        afficheEnTete();
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

    public void afficheEnTete(){
        TextView textElement = (TextView) findViewById(R.id.boxName_AlimentActivity);
        textElement.setText("Boîte : "+boiteName);

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
        textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        if(aliment.isAlimentFavori()==true){image.setImageResource(R.drawable.ic_launcher);
        }else{image.setImageResource(R.drawable.ic_launcher);}

        TextView textElement3 = (TextView) findViewById(R.id.nameAliment);
        textElement3.setText(aliment.getAlimentName());
    }

    public void sendMessageHelp(View view){
        Intent intent = new Intent(this,AideAliment.class);
        startActivity(intent);
    }

}
