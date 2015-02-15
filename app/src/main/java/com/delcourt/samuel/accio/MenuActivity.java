package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.structures.Refrigerateur;


public class MenuActivity extends ActionBarActivity {

    public static Refrigerateur refrigerateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the message from the intent (the frigo name)
        Intent intent = getIntent();
        try{String frigoName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);//Le bloc try permet d'aasurer que l'on peut revenir au menu depuis les activités précédentes (erreur sinon)

        //Récupère à partir des données sauvegardées les données du frigo
        int index=0;
        for (int i=0;i<MainActivity.dataSimulee.dataNombreFrigos;i++){
            if (frigoName.compareTo(MainActivity.dataSimulee.dataListeFrigos.get(i).name)==0) {
                index = i;
                break;
            }
        }
        //int index = MainActivity.dataSimulee.dataListeFrigos.indexOf(frigoName);
        refrigerateur = MainActivity.dataSimulee.dataListeFrigos.get(index);}
        catch (Exception E){//Affiche un Toast indiquant qu'une erreur a eu lieu
            Toast toast = Toast.makeText(getApplicationContext(), "Une erreur est survenue...", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

        setContentView(R.layout.activity_menu);

        TextView textElement = (TextView) findViewById(R.id.frigoNameMenu);
        textElement.setText("Réfrigérateur : " + refrigerateur.name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    public void sendMessageContent(View view){
        Intent intent = new Intent(this,ContentActivity.class);
        startActivity(intent);
    }

    public void sendMessageRecipes(View view){
        Intent intent = new Intent(this,RecipesActivity.class);
        startActivity(intent);
    }

    public void sendMessageFavorite(View view){
        Intent intent = new Intent(this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void sendMessageHistorique(View view){
        Intent intent = new Intent(this,HistoriqueActivity.class);
        startActivity(intent);
    }

    public void sendMessageHelp(View view){
        Uri webpage = Uri.parse("http://www.android-help.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }
}
