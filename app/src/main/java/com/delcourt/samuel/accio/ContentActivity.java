package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.delcourt.samuel.accio.content_sous_activites.ContentDessertsActivity;
import com.delcourt.samuel.accio.content_sous_activites.ContentFruitsActivity;
import com.delcourt.samuel.accio.content_sous_activites.ContentLegumesActivity;
import com.delcourt.samuel.accio.content_sous_activites.ContentViandesActivity;
import com.delcourt.samuel.accio.content_sous_activites.ContentYaourtsActivity;


public class ContentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
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

    public void sendMessageLegumes(View view){
        Intent intent = new Intent(this, ContentLegumesActivity.class);
        startActivity(intent);
    }

    public void sendMessageViandes(View view){
        Intent intent = new Intent(this, ContentViandesActivity.class);
        startActivity(intent);
    }

    public void sendMessageYaourts(View view){
        Intent intent = new Intent(this, ContentYaourtsActivity.class);
        startActivity(intent);
    }

    public void sendMessageDesserts(View view){
        Intent intent = new Intent(this, ContentDessertsActivity.class);
        startActivity(intent);
    }

    public void sendMessageFruits(View view){
        Intent intent = new Intent(this, ContentFruitsActivity.class);
        startActivity(intent);
    }
}
