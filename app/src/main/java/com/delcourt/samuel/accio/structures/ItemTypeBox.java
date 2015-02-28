package com.delcourt.samuel.accio.structures;

//Permet de choisir le type de boîte à la création d'une nouvelle boîte
public class ItemTypeBox {
    private String type;
    private boolean selected;
    private int numero;

    public ItemTypeBox(String type){
        this.type = type;
        selected = false;
    }

    public String getType(){
        return type;
    }

    public boolean isSelected(){
        return selected;
    }

    public void selected(){
        selected = true;
    }

    public void unselected(){
        selected = false;
    }
}
