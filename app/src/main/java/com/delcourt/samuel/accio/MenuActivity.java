package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
