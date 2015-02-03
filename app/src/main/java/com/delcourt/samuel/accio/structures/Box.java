package com.delcourt.samuel.accio.structures;

/* Définit la structure des boîtes.
Les attributs de la boîte seront données par la base de bonnées
 */

public class Box {

    public String name;
    public String referenceBdd; //référence vers la base de données : permet d'avoir accès aux informations sur la boîte

    public Box(String referenceBdd, String name){
        this.name=name;
        this.referenceBdd=referenceBdd;
    }

    public void setName(String name){
        this.name=name;
    }
}
