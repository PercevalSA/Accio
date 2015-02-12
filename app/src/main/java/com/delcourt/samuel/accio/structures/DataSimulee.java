package com.delcourt.samuel.accio.structures;

// Contient des données qui devraient être sauvegardées. Le recours à cette classe me permet simplement de continuer l'application
// (bien que je ne sois pas encore parvenu à sauver les données), en préparant déjà la structure de la suite.

import java.util.ArrayList;

public class DataSimulee {

    public ArrayList<String> dataFrigoNames; //sert à la gestion des frigos par MainActivity
    public int dataNombreFrigos;
    public ArrayList<Refrigerateur> dataListeFrigos;//Chaque élément de cette liste correspondra à DEUX fichiers texte,dont les
            // noms seront caractérisés par le nom du frigo. Le premier contiendra la liste des boîtes se trouvant dans ce frigos, le second
            // contiendra seulement le nombre de boîtes se trouvant dans le frigo.

    public DataSimulee(){
        dataFrigoNames = new ArrayList<String>();
        dataNombreFrigos = 0; //initialisé à 0, car pour l'instant (abscence de sauvegarde), toutes les variables sont
        //de toute façon réinitialisées à l'ouverture de l'appli
        dataListeFrigos = new ArrayList<Refrigerateur>();

        //Ajout d'un frigo pour les essais (à supprimer par la suite, lorsque sauvegarde possible)
        dataFrigoNames.add("Réfrigérateur essai");
        dataNombreFrigos++;
        dataListeFrigos.add(new Refrigerateur("Réfrigérateur essai"));

    }

}
