package com.delcourt.samuel.accio.structures;

//Permet la gestion des recettes enregistrées

public class Recette {
    private String name;
    private String adresseWeb;

    public Recette(String name, String adresseWeb){
        this.name=name;
        this.adresseWeb=adresseWeb;
    }

    public String getName(){
        return name;
    }

    public String getAdresseWeb(){
        return adresseWeb;
    }
}
