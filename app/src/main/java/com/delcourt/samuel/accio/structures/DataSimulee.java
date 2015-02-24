package com.delcourt.samuel.accio.structures;

// Contient des données qui devraient être sauvegardées. Le recours à cette classe me permet simplement de continuer l'application
// (bien que je ne sois pas encore parvenu à sauver les données), en préparant déjà la structure de la suite.

import java.util.ArrayList;

public class DataSimulee {

    //la gestion du nombre de frigos est maintenant opérationnelle (passag par le fichier texte)

    public ArrayList<String> dataFrigoNames; //sert à la gestion des frigos par MainActivity
    public ArrayList<Refrigerateur> dataListeFrigos;//Chaque élément de cette liste correspondra à DEUX fichiers texte,dont les
    // noms seront caractérisés par le nom du frigo. Le premier contiendra la liste des boîtes se trouvant dans ce frigos, le second
    // contiendra seulement le nombre de boîtes se trouvant dans le frigo.

    public DataSimulee(){


        dataFrigoNames = new ArrayList<String>();
        dataListeFrigos = new ArrayList<Refrigerateur>();

        //Ajout d'un frigo pour les essais (à supprimer par la suite, lorsque sauvegarde possible)
        dataFrigoNames.add("Réfrigérateur essai");
        dataListeFrigos.add(new Refrigerateur("Réfrigérateur essai"));

        dataListeFrigos.get(0).addBox("Fruits (exemple 1)");
        dataListeFrigos.get(0).addBox("Légumes (exemple 2)");

    }

}
