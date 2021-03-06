package com.delcourt.samuel.accio.structures;

/* Définit la structure des boîtes.
Les attributs de la boîte seront données par la base de données
 */

import java.util.ArrayList;

public class Box {

    private final String referenceBdd; //référence vers la base de données : permet d'avoir accès aux informations sur la boîte
    private String name;
    private String type;
    private ArrayList<String> listeAliments;
    private ArrayList<String> listeFavoris;

    public Box(String referenceBdd, String name, String type){
        this.name=name;
        this.referenceBdd=referenceBdd;
        this.type = type;
        listeAliments = new ArrayList<>();
        listeFavoris = new ArrayList<>();
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public String getType(){return type;}

    public String getReferenceBdd(){return referenceBdd;}

    public ArrayList<String> getListeAliments(){return listeAliments;}

    public ArrayList<String> getListeFavoris(){return listeFavoris;}

}
