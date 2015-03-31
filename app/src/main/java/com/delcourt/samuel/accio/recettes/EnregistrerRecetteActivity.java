package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class EnregistrerRecetteActivity extends ActionBarActivity {

    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enregistrer_recette);
        } catch (Exception e){
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enregistrer_recette, menu);
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

    public static void setUrl(String adresse){url=adresse;}

    public void newRecette(View view) {
        EditText editText = (EditText) findViewById(R.id.nameRecette);
        String recetteName = editText.getText().toString();


        if (recetteName.length() == 0) { //Si le nom est vide, envoie un message
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            try {
                OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("recettes_file.txt",MODE_APPEND));
                BufferedWriter bw = new BufferedWriter(outStream);
                PrintWriter out2 = new PrintWriter(bw);
                out2.println(recetteName);
                out2.println(url);
                out2.close();
            } catch (FileNotFoundException e1) {
                Toast.makeText(getApplicationContext(), "liste recettes not found", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this,MenuRecettesActivity.class);
            startActivity(intent);
        }
    }
}
