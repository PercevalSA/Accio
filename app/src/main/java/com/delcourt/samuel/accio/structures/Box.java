package com.delcourt.samuel.accio.structures;

/* Définit la structure des boîtes.
Les attributs de la boîte seront données par la base de données
 */

import java.util.ArrayList;

public class Box {

    private final String referenceBdd; //référence vers la base de données : permet d'avoir accès aux informations sur la boîte
    private String name;
    private String type;
    private ArrayList<Aliment> listeAliments;
    private boolean connectedBDD;

    public Box(String referenceBdd, String name, String type){
        this.name=name;
        this.referenceBdd=referenceBdd;
        this.type = type;
        listeAliments = new ArrayList<>();
        connectedBDD = false;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public String getType(){return type;}

    public String getReferenceBdd(){return referenceBdd;}

    public ArrayList<Aliment> getListeAliments(){return listeAliments;}

    public void reinitialiseListeAliments(){
        listeAliments = new ArrayList<>();
    }

    public boolean getConnectedBdd(){return connectedBDD;}

    public void setConnectedBDD(boolean b){connectedBDD=b;}


}
