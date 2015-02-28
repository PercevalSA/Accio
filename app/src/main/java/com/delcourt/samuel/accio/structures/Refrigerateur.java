package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

public class Refrigerateur {

    public String name;
    public ArrayList<Box> boxes;
    public ArrayList<String> listeBoitesNames;

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

    public void addBox(String refBdd, String boxName, String type) {
        Box box = new Box(refBdd,boxName,type);
        boxes.add(box);
        listeBoitesNames.add(boxName);
    }

}
