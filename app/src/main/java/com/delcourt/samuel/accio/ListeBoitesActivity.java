package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.delcourt.samuel.accio.create_new_object_activities.NewBoxActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;
import java.util.ArrayList;
import java.util.HashMap;


public class ListeBoitesActivity extends ActionBarActivity {

    private Refrigerateur refrigerateur = RefrigerateurActivity.refrigerateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_boites);

        affihceListeBoites();

        TextView textElement = (TextView) findViewById(R.id.messageBoitesduFrigo);
        textElement.setText("Boites Accio du réfrigérateur : " + com.delcourt.samuel.accio.RefrigerateurActivity.refrigerateur.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_boites, menu);
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

    public void sendMessageBoxSelected(View view, int index){//A COMPLETER !!!
        BoxActivity.boite=refrigerateur.getBoxes().get(index);
        Intent intent = new Intent(this, BoxActivity.class);
        startActivity(intent);
    }

    public void sendMessageNewBox(View view){
        Intent intent = new Intent(this, NewBoxActivity.class);
        startActivity(intent);
        }

    public void affihceListeBoites(){
        int numberBoxes = RefrigerateurActivity.refrigerateur.getBoxes().size();
        if(numberBoxes==0){//Si pas de boîte, on affiche un message
            ListView frigoList=(ListView)findViewById(R.id.listeViewBoites);
            ArrayList<String> liste = new ArrayList<>();
            liste.add("");
            liste.add("Ce réfrigérateur ne contient pas encore de boîte Accio");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, liste);
            frigoList.setAdapter(arrayAdapter);
        }
        else {//On affiche la liste des boîtes

            // Get the reference of listViewFrigos (pour l'affichage de la liste)
            final ListView listViewBoxes=(ListView)findViewById(R.id.listeViewBoites);

            //Création de la ArrayList qui nous permettra de remplir la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

            //On déclare la HashMap qui contiendra les informations pour un item
            HashMap<String, String> map;


            for(int i=0;i<numberBoxes;i++){


                //on insère la référence aux éléments à afficher
                map = new HashMap<String, String>();
                map.put("titre", RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName());
                map.put("description", RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
                //Récupère le nom de l'image à affihcer
                String type = refrigerateur.getBoxes().get(i).getType();
                //MODIFIER LES NOMS DES IMAGES A AFFICHER
                if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_launcher));}
                else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
                    map.put("img", String.valueOf(R.drawable.ic_launcher));
                    //On affiche un toast
                    Toast.makeText(getApplicationContext(), "Le type de la boîte n'a pas été reconnu",
                            Toast.LENGTH_SHORT).show();
                }

                //enfin on ajoute cette hashMap dans la arrayList
                listItem.add(map);
            }

            //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
            SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichage_liste_boites,
                    new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});

            //On attribue à notre listView l'adapter que l'on vient de créer
            listViewBoxes.setAdapter(mSchedule);


            //register onClickListener to handle click events on each item
            listViewBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                // argument position gives the index of item which is clicked

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    int indexBox = position;
                    sendMessageBoxSelected(view, indexBox);
                }
            });
        }
    }

}
