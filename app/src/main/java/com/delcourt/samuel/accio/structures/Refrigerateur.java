package com.delcourt.samuel.accio.structures;

import java.util.ArrayList;

/* Définit la structure des réfrigérateurs.
Le nom des frigo et les références sur la base de données des boites qu'ils contiennent
sont ainsi stockées par l'application elle même.
 */

public class Refrigerateur {

    public String name;
    public ArrayList<Box> boxes;

    public Refrigerateur(String name){

        this.name=name;
        boxes = new ArrayList<Box>();
        }

    public void setName(String name)
    {
        this.name=name;
    }

    public void addBox(Box box) {
        boxes.add(box);
    }

}
