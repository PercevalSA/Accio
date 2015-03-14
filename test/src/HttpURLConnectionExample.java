import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.DataOutputStream;

public class HttpURLConnectionExample {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        // Getting barcode of product
        Scanner inb = new Scanner(System.in);
        System.out.println("Enter a barcode");
        String barcode = inb.nextLine();

        // base URL, with API key and barcode
        String url = "https://www.outpan.com/api/get-product.php?apikey=e6092ddde3f7d36258ea7bfa801017fd&barcode=";
        url = url+barcode;

        // Getting name of product
        String nom = http.getName(url);

        // Getting manufacturer of product
        String manufacturer = http.getManufacturer(url);

        // Adding the product to our local DB
        // addBDD(nom, manufacturer);

    }

    // Get Name of product
    private String getName(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        /*int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);*/

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // System.out.println(response.toString());

        String phrase = response.toString();
        String delims = "\",";
        String delimsname = "\"name\": \"";
        String[] tokens = phrase.split(delims);
        String[] name = tokens[2].split(delimsname);


        /*for (int j = 0; j<manufacturer.length; j++){
            System.out.println(manufacturer[1]);
        }*/

        /*for (int i = 0; i<tokens.length; i++){
            System.out.println(tokens[i]);
        }*/

        // If name exists, then show it
        if (name.length > 1) {
            String nom = name[1];
            System.out.println("Le nom du produit est : " + nom);
            String nomf = nom.replace(" ","+");
            return nomf;
        }

        // If it doesn't, then set it
        else {
            String nom = setName(url);
            System.out.println("Le nom du produit est : " + nom);
            String nomf = nom.replace(" ","+");
            return nomf;
        }

    }

    private String getManufacturer(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        /*int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);*/

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String phrase = response.toString();
        String delims = "\",";
        String delimsbis = "\"";
        String delimsman = "\"Manufacturer\": \"";
        String[] manufacturer = phrase.split(delimsman);

        // If manufacturer exists, then show it
        if (manufacturer.length == 2){
            String[] man = manufacturer[1].split(delims);
            String[] manbis = man[0].split(delimsbis);
            String manuf = manbis[0];
            System.out.println("La marque du produit est : " + manuf);
            String manufb = manuf.replace(" ","+");
            return manufb;
        }

        // If it doesn't, then set it
        else {
            String manuf = setManufacturer(url);
            System.out.println("La marque du produit est : " + manuf);
            String manufb = manuf.replace(" ","+");
            return manufb;
        }
    }

    // Set Name of product
    private String setName(String url) throws Exception {

        // URL changes for setting name
        String urlname = url.replace("get-product","edit-name");
        urlname = urlname+"&name=";

        Scanner in = new Scanner(System.in);
        System.out.println("Enter a product name");
        String s = in.nextLine();

        // Name of product asked and added to request, with correct HTML form
        String namef = s.replace(" ","+");
        urlname = urlname+namef;

        // System.out.println(urlname);

        URL objname = new URL(urlname);
        HttpURLConnection conname = (HttpURLConnection) objname.openConnection();

        // optional default is GET
        conname.setRequestMethod("GET");

        //add request header
        conname.setRequestProperty("User-Agent", USER_AGENT);

        /*int responseCode = conb.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);*/

        BufferedReader inname = new BufferedReader(
                new InputStreamReader(conname.getInputStream()));
        String inputLineb;
        StringBuffer responseb = new StringBuffer();

        while ((inputLineb = inname.readLine()) != null) {
            responseb.append(inputLineb);
        }
        inname.close();

        return s;

    }

    private String setManufacturer(String url) throws Exception {

        // URL changes for setting manufacturer name
        String urlman = url.replace("get-product","edit-attr");
        urlman = urlman+"&attr_name=Manufacturer&attr_val=";

        Scanner in = new Scanner(System.in);
        System.out.println("Enter a manufacturer");
        String s = in.nextLine();

        // Manufacturer of product asked and added to request, with correct HTML form
        String manf = s.replace(" ","+");
        urlman = urlman+manf;

        URL objman = new URL(urlman);
        HttpURLConnection conman = (HttpURLConnection) objman.openConnection();

        // optional default is GET
        conman.setRequestMethod("GET");

        //add request header
        conman.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader inman = new BufferedReader(
                new InputStreamReader(conman.getInputStream()));
        String inputLineb;
        StringBuffer responseman = new StringBuffer();

        while ((inputLineb = inman.readLine()) != null) {
            responseman.append(inputLineb);
        }
        inman.close();

        return s;
    }

    //3103220035214
    //3242272001553

    private static void addBDD(String nom, String manufacturer, String barcode) throws Exception {

        int alim = 6;
        int boite = 7;
        String urls = "http://localhost/connection3.php?";
        String urlParameters = "nom="+nom+"&codebarre="+barcode+"&aliment="+alim+"&boite="+boite;
        String url = urls+urlParameters;

        URL objs = new URL(url);
        HttpURLConnection cons = (HttpURLConnection) objs.openConnection();

        // GET request
        cons.setRequestMethod("GET");

        // add request header
        cons.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(cons.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }
}