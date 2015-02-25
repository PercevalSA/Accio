package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

public class Refrigerateur {

    public String name;
    public ArrayList<String> listBoxes;
    public int numberBoxes;

    public Refrigerateur(String name){

        this.name=name;
        listBoxes = new ArrayList<String>();
        numberBoxes=0;
        }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void addBox(String boxName) {
        listBoxes.add(boxName);
    }

}
