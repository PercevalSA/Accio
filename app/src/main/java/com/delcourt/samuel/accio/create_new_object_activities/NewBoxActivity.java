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
import com.delcourt.samuel.accio.ListeBoitesActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.ItemTypeBox;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class NewBoxActivity extends ActionBarActivity {

    public ArrayList<String> listTypesBoxesNames;
    public ArrayList<ItemTypeBox> listTypesBoxes;
    public int numberBoxesSelected = 0;

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
        listTypesBoxesNames = new ArrayList<>();
        getTypes();
        for (int i = 0; i < listTypesBoxesNames.size(); i++) {
            map = new HashMap<String, String>();
            map.put("check", listTypesBoxesNames.get(i));
            map.put("img2",String.valueOf(R.drawable.ic_launcher));
            listItem.add(map);
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.liste_categories,
                new String[] {"check","img2"}, new int[] {R.id.check,R.id.img2});

        //On attribue à notre listView l'adapter que l'on vient de créer
        typesList.setAdapter(mSchedule);

        //register onClickListener to handle click events on each item
        typesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {}
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

        Intent intent = new Intent(this,ListeBoitesActivity.class);

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

            //ON S'ASSURE QUE LES INFOS SONT COHERENTES

        if (newBoiteName.length() == 0 || numeroBoite.length() == 0 || codeBoite.length() == 0 ){ //Si l'un des champs n'a pas été renseigné
            Toast toast = Toast.makeText(getApplicationContext(), "L'un des champs n'a pas été renseigné", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else { //On s'assure que le code de la boîte est correct. REMARQUE : pour l'instant, le code de toutes les boîtes est : accio

            //Il faut ici se connecter à la bdd afin de récupérer les infos (notamment le code attendu)
            if (codeBoite.compareTo("accio") != 0){//Le code entré est incorrect
                Toast toast = Toast.makeText(getApplicationContext(), "Le code entré est incorrect", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

            else{//On s'assure qu'un seul type de boîte a été déclaré
                if (numberBoxesSelected==0){
                    Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas choisi le type de la boîte", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else if (numberBoxesSelected>1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Vous devez choisir un seul type de boîte", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else{
                    //on récupère le type de la boîte
                    int index = 0;
                    while(listTypesBoxes.get(index).isSelected()==false){
                        index++;
                    }

                    String newBoiteType=listTypesBoxes.get(index).getType();

                    //IL FAUT COMMUNIQUER CETTE INFO A LA BDD !

                    //On s'assure qu'aucune boîte du même nom n'a encore été créée
                    int k = 0;
                    for (int i=0;i< RefrigerateurActivity.refrigerateur.boxes.size();i++){
                        if (newBoiteName.compareTo(RefrigerateurActivity.refrigerateur.boxes.get(i).getName()) == 0){
                            k++;
                        }
                    }
                    if(k > 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Une boîte possédant ce nom existe déjà dans ce réfrigérateur", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }

                    else {//Dans ce cas, c'est bon, on peut créer la nouvelle boîte
                        try {//Ajoute le nom du nouveau frigo dans frigos_file.txt (ne remplace pas le fichier mais écrit à la suite)
                            String nameFrigo = RefrigerateurActivity.refrigerateur.getName();

                            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt",MODE_APPEND));
                            BufferedWriter bw = new BufferedWriter(outStream);
                            PrintWriter out2 = new PrintWriter(bw);
                            out2.println("Ref bdd à mettre");
                            out2.println(newBoiteName);
                            out2.close();

                            //L'ensemble du réfrigérateur n'a pas encore été recréé : il faut donc ajouter cette nouvelle boîte à la liste dynamique
                            Box newBox = new Box("Référence Bdd", newBoiteName, newBoiteType);
                            RefrigerateurActivity.refrigerateur.boxes.add(newBox);

                            Toast.makeText(getApplicationContext(), "Connecter à la bdd : récupérer référence boîte, type, code",
                                    Toast.LENGTH_SHORT).show();
                        } catch (java.io.IOException e) {
                            Toast.makeText(getApplicationContext(), "erreur écriture boîte", Toast.LENGTH_SHORT).show();
                        }

                        //la boîte a été crée, on retourne sur l'activité précédente :
                        startActivity(intent);
                    }


                }

            }

        }

    }

    public void sendMessageHelp(View view){

    }

    void getTypes() {
        ItemTypeBox fruits = new ItemTypeBox("Fruits");
        listTypesBoxes.add(fruits);
        listTypesBoxesNames.add("Fruits");

        ItemTypeBox legumes = new ItemTypeBox("Légumes");
        listTypesBoxes.add(legumes);
        listTypesBoxesNames.add("Légumes");

        ItemTypeBox laitier = new ItemTypeBox("Produits laitiers");
        listTypesBoxes.add(laitier);
        listTypesBoxesNames.add("Produits laitiers");

        ItemTypeBox poisson = new ItemTypeBox("Poisson");
        listTypesBoxes.add(poisson);
        listTypesBoxesNames.add("Poisson");

        ItemTypeBox viande = new ItemTypeBox("Viande");
        listTypesBoxes.add(viande);
        listTypesBoxesNames.add("Viande");

        ItemTypeBox sauces = new ItemTypeBox("Sauces et condiments");
        listTypesBoxes.add(sauces);
        listTypesBoxesNames.add("Sauces et condiments");
    }

    public void selectedBox(View v) {
        CheckBox cb = (CheckBox) v;
        int index=listTypesBoxesNames.indexOf(cb.getText());
        if (listTypesBoxes.get(index).isSelected()==false){
            listTypesBoxes.get(index).selected();//on indique que le frigo a été sélectionné
            numberBoxesSelected++;
        } else {
            listTypesBoxes.get(index).unselected();//on indique que le frigo a été désélectionné
            numberBoxesSelected--;
        }
    }
}
