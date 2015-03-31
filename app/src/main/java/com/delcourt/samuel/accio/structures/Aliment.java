package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

public class Aliment {

    private String name;
    private String marque;
    private boolean favori;

    private String historique;
    private String boxName;

    private String alimID;
    private String type; //Correspond au type de la boîte

    public Aliment(String name,String marque,  boolean favori,String historique, String box, String alimID,String type){
        this.name = name;
        this.marque = marque;
        this.favori = favori;
        this.historique = historique;
        this.boxName=box;
        this.alimID=alimID;
        this.type=type;
    }

    public String getAlimentName(){return name;}

    public String getAlimentMarque(){return marque;}

    public boolean isAlimentFavori(){return favori;}

    public void setFavori(boolean b){favori = b;}

    public String getAlimentHistorique(){return historique;}

    public String getBoxName(){return boxName;}

    public String getalimID(){return alimID;}

    public String getType(){return type;}
}
