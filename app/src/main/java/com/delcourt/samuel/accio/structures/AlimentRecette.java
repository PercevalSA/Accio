package com.delcourt.samuel.accio.structures;


public class AlimentRecette {
    private String name;
    private boolean selected;
    private String type;

    public AlimentRecette(String name,String type){
        this.name=name;
        selected=false;
        this.type=type;
    }

    public String getName(){return name;}
    public String getType() {return type;}
    public boolean isSelected(){return selected;}
    public void setSelected(boolean b){selected=b;}
}
