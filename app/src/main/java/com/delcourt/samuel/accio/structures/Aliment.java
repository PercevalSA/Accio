package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

/**
 * Created by Coline on 09/03/2015.
 */
public class Aliment {

    private String name;
    private String marque;
    private boolean favori;
    private ArrayList<String> historique;
    //private int alimentID;

    public Aliment(String name,String marque,  boolean favori,ArrayList<String> historique){
        this.name = name;
        this.marque = marque;
        this.favori = favori;
        this.historique = historique;
    }

    public String getAlimentName(){return name;}

    public String getAlimentMarque(){return marque;}

    public boolean isAlimentFavori(){return favori;}

    public ArrayList<String> getAlimentHistorique(){return historique;}
}
