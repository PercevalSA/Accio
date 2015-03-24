package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

public class Refrigerateur {

    private String name;
    private ArrayList<Box> boxes;
    private boolean connectionBdd;
    private boolean refrigerateurCreated;

    public Refrigerateur(String name){
        this.name=name;
        boxes = new ArrayList<>();
        //listeBoitesNames = new ArrayList<>();
        connectionBdd = false;
        refrigerateurCreated = false;
        }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Box> getBoxes(){return boxes;}

    public boolean getConnectionBdd(){return connectionBdd;}

    public void setConnectionBdd(boolean b){connectionBdd = b;}

    public boolean isRefrigerateurCreated(){return refrigerateurCreated;}

    public void setRefrigerateurCreated(){refrigerateurCreated=true;}

}
