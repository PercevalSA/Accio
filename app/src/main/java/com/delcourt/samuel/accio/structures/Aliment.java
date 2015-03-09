package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

/**
 * Created by Coline on 09/03/2015.
 */
public class Aliment {

    private String name;
    private boolean favori;
    private ArrayList<String> historique;
    //private int alimentID;

    public Aliment(String name){
        this.name = name;
        favori = false;
        historique = new ArrayList();
    }

    public String getAlimentName(){return name;}

    public boolean isAlimentFavori(){return favori;}

    public ArrayList<String> getAlimentHistorique(){return historique;}
}
