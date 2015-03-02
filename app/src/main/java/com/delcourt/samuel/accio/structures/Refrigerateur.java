package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

public class Refrigerateur {

    private String name;
    private ArrayList<Box> boxes;
    private ArrayList<String> listeBoitesNames;

    public Refrigerateur(String name){
        this.name=name;
        boxes = new ArrayList<>();
        listeBoitesNames = new ArrayList<>();
        }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Box> getBoxes(){return boxes;}

}
