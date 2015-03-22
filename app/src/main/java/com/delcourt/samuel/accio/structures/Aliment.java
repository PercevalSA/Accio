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
    private String box;
    //private int alimentID;

    public Aliment(String name,String marque,  boolean favori,ArrayList<String> historique, String box){
        this.name = name;
        this.marque = marque;
        this.favori = favori;
        this.historique = historique;
        this.box=box;
    }

    public String getAlimentName(){return name;}

    public String getAlimentMarque(){return marque;}

    public boolean isAlimentFavori(){return favori;}

    public void setFavori(boolean b){favori = b;}

    public ArrayList<String> getAlimentHistorique(){return historique;}

    public String getBoxName(){return box;}
}
