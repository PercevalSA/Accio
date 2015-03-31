import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpURLConnectionExample {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        int i = 0;
        while(i == 0){

            HttpURLConnectionExample http = new HttpURLConnectionExample();

            // Getting barcode of product
            Scanner inb = new Scanner(System.in);
            System.out.println("Enter a barcode");
            String barcode = inb.nextLine();

            // base URL, with API key and barcode
            String url = "https://www.outpan.com/api/get-product.php?apikey=e6092ddde3f7d36258ea7bfa801017fd&barcode=";
            url = url + barcode;

            // Getting name of product
            String nom = http.getName(url);

            // Getting manufacturer of product
            String manufacturer = http.getManufacturer(url);

            // Adding the product to our local DB
            addBDD(nom, manufacturer, barcode);

        }
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

        changeFlag("01");

        while(getLatestFilefromDir("product") == null){
            Thread.sleep(1000);
        }
        File p = getLatestFilefromDir("product");
        String s = p.getName();
        System.out.println(s);

        //Scanner in = new Scanner(System.in);
        //System.out.println("Enter a product name");
        //String s = in.nextLine();

        // Name of product asked and added to request, with correct HTML form
        //String namef = s.replace(" ","+");
        urlname = urlname+s;

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

        p.delete();
        changeFlag("00");
        return s.replace("+"," ");

    }

    private String setManufacturer(String url) throws Exception {

        // URL changes for setting manufacturer name
        String urlman = url.replace("get-product", "edit-attr");
        urlman = urlman+"&attr_name=Manufacturer&attr_val=";

        changeFlag("10");

        while(getLatestFilefromDir("manufacturer") == null){
            Thread.sleep(1000);
        }
        File p = getLatestFilefromDir("manufacturer");
        String s = p.getName();

        //Scanner in = new Scanner(System.in);
        //System.out.println("Enter a manufacturer");
        //String s = in.nextLine();

        // Manufacturer of product asked and added to request, with correct HTML form
        //String manf = s.replace(" ","+");
        urlman = urlman+s;

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

        p.delete();
        changeFlag("00");
        return s.replace("+"," ");
    }

    //3103220035214
    //3242272001553

    private static void addBDD(String nom, String manufacturer, String barcode) throws Exception {

        String url = "http://localhost/pact/connection-check-manufacturer.php?manufacturer="+manufacturer;

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

        String marqueid = response.toString();

        // System.out.println(marqueid);

        // If manufacturer doesn't exist in our DB, add it

        if (marqueid.equals("no")){
            String urlbis="http://localhost/pact/connection-add-manufacturer.php?manufacturer="+manufacturer;

            URL objsbis = new URL(urlbis);
            HttpURLConnection consbis = (HttpURLConnection) objsbis.openConnection();

            // GET request
            consbis.setRequestMethod("GET");

            // add request header
            consbis.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader inbis = new BufferedReader(
                    new InputStreamReader(consbis.getInputStream()));
            String inputLinebis;
            StringBuffer responsebis = new StringBuffer();

            while ((inputLinebis = inbis.readLine()) != null) {
                responsebis.append(inputLinebis);
            }
            inbis.close();

            marqueid = responsebis.toString();

            //System.out.println(responsebis.toString());
        }

        // Add the product to our DB, with its corresponding manufacturer

        int boite = 7;
        String urladd = "http://localhost/pact/connection-add-product.php?";
        String urlParameters = "nom="+nom+"&codebarre="+barcode+"&boite="+boite+"&marque="+marqueid;
        String urladdbis = urladd+urlParameters;

        URL objadd = new URL(urladdbis);
        HttpURLConnection conadd = (HttpURLConnection) objadd.openConnection();

        // GET request
        conadd.setRequestMethod("GET");

        // add request header
        conadd.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader inadd = new BufferedReader(
                new InputStreamReader(conadd.getInputStream()));
        String inputLineadd;
        StringBuffer responseadd = new StringBuffer();

        while ((inputLineadd = inadd.readLine()) != null) {
            responseadd.append(inputLineadd);
        }
        inadd.close();

        String out = responseadd.toString();

        System.out.println(out);

    }


private static void changeFlag(String flag) throws Exception {

    String url = "http://localhost/pact/connection-change-flag.php?flag="+flag;

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // GET request
    con.setRequestMethod("GET");

    // add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();
}

    private File getLatestFilefromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

}