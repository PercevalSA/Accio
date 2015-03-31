package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.AlimentRecette;
import java.util.ArrayList;
import java.util.HashMap;


public class ChoixAlimentsRecettes extends ActionBarActivity {

    private ArrayList<AlimentRecette> listeAlimentsProposes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choix_aliments_recettes);

            //On affiche cette liste :
            afficheListeAlimentsProposes();

            //Toast temporaire
            Toast toast = Toast.makeText(getApplicationContext(), "Récupérer automatiquement le contenu de toutes les boîtes", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } catch (Exception e){
            Log.e("log_tag", "Error " + e.toString());
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
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

    public void afficheListeAlimentsProposes(){

        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        final ListView listViewBoxes=(ListView)findViewById(R.id.listeAlimentsProposes);

        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        if(listeAlimentsProposes.size()==0){
            //Juste pour l'exemple :
            AlimentRecette exemple = new AlimentRecette("banane","a");
            listeAlimentsProposes.add(exemple);
            createListeAlimentsProposes();
        }


        for(int i=0;i<listeAlimentsProposes.size();i++){

            //on insère la référence aux éléments à afficher
            map = new HashMap<>();
            map.put("nom", listeAlimentsProposes.get(i).getName());

            String type = listeAlimentsProposes.get(i).getType();
            if (type.compareTo("Fruits")==0){ map.put("img", String.valueOf(R.drawable.ic_fruit));}
            else if (type.compareTo("Légumes")==0){ map.put("img", String.valueOf(R.drawable.ic_legume));}
            else if (type.compareTo("Produits laitiers")==0){ map.put("img", String.valueOf(R.drawable.ic_produit_laitier));}
            else if (type.compareTo("Poisson")==0){ map.put("img", String.valueOf(R.drawable.ic_poisson));}
            else if (type.compareTo("Viande")==0){ map.put("img", String.valueOf(R.drawable.ic_viande));}
            else if (type.compareTo("Sauces et condiments")==0){ map.put("img", String.valueOf(R.drawable.ic_condiment));}
            else {//Sinon (type non reconnu, ne devrait jamais arriver) - c'est le cas de l'aliment banane mis par défaut
                map.put("img", String.valueOf(R.drawable.ic_launcher));
            }

            if(listeAlimentsProposes.get(i).isSelected()) {
                map.put("selected", String.valueOf(R.drawable.validate));
            } else {map.put("selected", String.valueOf(R.drawable.validaten));}

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
                boxSelected(position);
            }
        });
    }


    public void createListeAlimentsProposes(){
        for(int i=0;i< RefrigerateurActivity.getRefrigerateur().getBoxes().size();i++){
            for(int j=0;j<RefrigerateurActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().size();j++){
                AlimentRecette aliment = new AlimentRecette(RefrigerateurActivity.getRefrigerateur().getBoxes().get(i).getListeAliments().get(j).getAlimentName(),
                        RefrigerateurActivity.getRefrigerateur().getBoxes().get(i).getType());
                listeAlimentsProposes.add(aliment);
            }
        }
    }

    public void boxSelected(int index){
        if(listeAlimentsProposes.get(index).isSelected()==false){
            listeAlimentsProposes.get(index).setSelected(true);
            afficheListeAlimentsProposes();
        }else{
            listeAlimentsProposes.get(index).setSelected(false);
            afficheListeAlimentsProposes();
        }
    }

    public void sendMessageChercher(View view){
        String aliments=prepareRechercheAliments();
        if (aliments.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas choisi d'aliments", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else{
            Intent intent = new Intent(this, OptionsRecettesActivity.class);
            OptionsRecettesActivity.setAliments(aliments);
            startActivity(intent);
        }

    }

    public String prepareRechercheAliments(){
        String aliments;
        ArrayList<String> alimentsSelected = new ArrayList<>();

        //Récupère ce qui a été coché
        EditText editText = (EditText) findViewById(R.id.edit_text_recette);
        String recup = editText.getText().toString();

        for(int i=0;i<listeAlimentsProposes.size();i++){
            if(listeAlimentsProposes.get(i).isSelected()){
                alimentsSelected.add(listeAlimentsProposes.get(i).getName());
            }
        }

        if(alimentsSelected.size()==0){//Lorsque l'on n'a rien sélectionné, ou juste un texte supplémentaire
            aliments=recup;
            //Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_SHORT).show();
        }

        else if(recup.length()==0 && alimentsSelected.size()!=0){//Un aliment a été sélectionné, sans texte supplémentaire
            aliments = recup;
            for(int i=0;i<alimentsSelected.size()-1;i++){
                aliments=aliments+alimentsSelected.get(i)+"-";
                //Toast.makeText(getApplicationContext(), "i="+i,Toast.LENGTH_SHORT).show();
            }
            aliments=aliments+alimentsSelected.get(alimentsSelected.size()-1);//On ajoute le dernier
            //Toast.makeText(getApplicationContext(), "2",Toast.LENGTH_SHORT).show();
        }

        else{//Lorsque aliment sélectionné et texte supplémentaire
            aliments = recup;
            for(int j=0;j<alimentsSelected.size();j++){
                aliments=aliments+"-"+ alimentsSelected.get(j);
            }
            //Toast.makeText(getApplicationContext(), "3",Toast.LENGTH_SHORT).show();
        }

        return aliments;
    }
}
