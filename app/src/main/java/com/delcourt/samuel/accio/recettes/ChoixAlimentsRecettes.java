package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.recettes.OptionsRecettesActivity;
import com.delcourt.samuel.accio.structures.AlimentRecette;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import java.util.ArrayList;
import java.util.HashMap;


public class ChoixAlimentsRecettes extends ActionBarActivity {

    private ArrayList<AlimentRecette> listeAlimentsProposes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_aliments_recettes);

        //Juste pour l'exemple :
        AlimentRecette exemple = new AlimentRecette("banane (exemple)","Fruits");
        listeAlimentsProposes.add(exemple);
        createListeAlimentsProposes();

        //On affiche cette liste :
        afficheListeAlimentsProposes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_aliments_recettes, menu);
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

    public void alimentSelectionne(int position){
        /*String name = listeAlimentsProposes.get(position);
        OptionsRecettesActivity.aliments=name;

        //Envoie sur la page d'options
        Intent intent = new Intent(this,OptionsRecettesActivity.class);
        startActivity(intent);*/

    }

    public void afficheListeAlimentsProposes(){

        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        final ListView listViewBoxes=(ListView)findViewById(R.id.listeAlimentsProposes);

        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;


        for(int i=0;i<listeAlimentsProposes.size();i++){

            //on insère la référence aux éléments à afficher
            map = new HashMap<String, String>();
            map.put("nom", listeAlimentsProposes.get(i).getName());

            String type = listeAlimentsProposes.get(i).getType();
            if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
            else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
            else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
            else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
            else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
            else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
            else {//Sinon (type non reconnu, ne devrait jamais arriver) : on affiche l'image du frigo
                map.put("img", String.valueOf(R.drawable.ic_launcher));
                //On affiche un toast
                Toast.makeText(getApplicationContext(), "Le type de la boîte n'a pas été reconnu",
                        Toast.LENGTH_SHORT).show();
            }

            if(listeAlimentsProposes.get(i).isSelected()==true) {
                map.put("selected", String.valueOf(R.drawable.fav));
            } else {map.put("selected", String.valueOf(R.drawable.favn));}

            //enfin on ajoute cette hashMap dans la arrayList
            listItem.add(map);
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.liste_aliments_recette,
                new String[] {"nom", "img", "selected"}, new int[] {R.id.nom_aliment_recette, R.id.img_aliment_recette, R.id.aliment_recette_selected});

        //On attribue à notre listView l'adapter que l'on vient de créer
        listViewBoxes.setAdapter(mSchedule);


        //register onClickListener to handle click events on each item
        listViewBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                int indexBox = position;
                sendMessageBoxSelected(indexBox);
            }
        });
    }


    public void createListeAlimentsProposes(){
        for(int i=0;i< RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
            for(int j=0;j<RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().size();j++){
                AlimentRecette aliment = new AlimentRecette(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().get(j).getAlimentName(),
                        RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
                listeAlimentsProposes.add(aliment);
            }
        }
    }

    public void sendMessageBoxSelected(int index){}
}
