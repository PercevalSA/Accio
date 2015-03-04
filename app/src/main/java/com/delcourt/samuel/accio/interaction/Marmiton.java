package com.delcourt.samuel.accio.interaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

//package pact;

//import com.sun.javafx.css.converters.URLConverter;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by perceval on 03/03/15.*/

public class Marmiton {


/*
URLConnectionReader
http://www.marmiton.org/

page 2 : &start=10
page 3 : &start=20*/


    public static class getURL {


        private String page;

        public static void main(String[] args) throws Exception {

            System.out.println("Bonjour nous allons définir quelques options pour votre recherche");

            Scanner scan = new Scanner(System.in);
            System.out.println("quel plat voulez vous ?");
            String recherche = scan.nextLine();

            System.out.println("êtes vous végétarien ? (true pour vrai, false pour faux");
            boolean vegan = scan.nextBoolean();

            System.out.println("photo ?");
            boolean photo = scan.nextBoolean();

            System.out.println("cru ?");
            boolean cru = scan.nextBoolean();

            String url = getURL(recherche, photo, vegan, cru);
            System.out.println(url);

            URL adresse = new URL(url);
            //String page = getPage();
        }

        public String getPage(URL url) throws IOException {
            URLConnection yc = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                page = page + inputLine;
                System.out.println(inputLine);
            }
            in.close();
            return page;
        }

        public static String getURL (String recherche,
                                     boolean photo,
                                     boolean vegan,
                                     boolean cru,
                                     boolean Food,
                                     short cost,
                                     short difficult) {

    /*
    adresse de recherche (uniquement pour les recettes)
    http://www.marmiton.org/recettes/recherche.aspx?aqt=$MOTS_CLEF*/

            String url="http://www.marmiton.org/recettes/recherche.aspx?aqt="+recherche;

    /*
    Option pour la recherche :
        photo : &pht=1
        vegetarien : &veg=1
        sans cuisson ; &rct=1

        uniquement dans les ingrédients : &st=1

       cout :
           bon marché : &exp=1
           moyen : &exp=2
           assez cher : &exp=3

       difficultée :
           très facie : &dif=1
           facile : &dif=2
           moyenne : &dif=3
           difficile : &dif=4
           */


            if (photo == true)
                url+="&pht=1";
            if (vegan == true)
                url+="&veg=1";
            if (cru == true)
                url+="rct=1";
            if (Food == true)
                url+="st=1";

            switch (cost) {
                case 1 : url+="&exp=1";
                    break;

                case 2 : url+="&exp=2";
                    break;

                case 3 : url+="&exp=3";
                    break;
            }

            switch (difficult) {
                case 1 : url+="&dif=1";
                break;

                case 2 : url+="&dif=2";
                    break;

                case 3 : url+="&dif=3";
                    break;

                case 4 : url+="&dif=4";
                    break;
            }
            return url;
        }

        public static String getNextPage () {
            String nextPage=null;
            return nextPage;
        }

        public static String parsePage (String page) {
            String parsedPage=null;
            return parsedPage;
        }
    }

}

