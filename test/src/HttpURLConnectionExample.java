import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        // Getting name of product (and setting it if necessary)
        http.getName();

    }

    // Get Name of product
    private void getName() throws Exception {
        // base URL, with API key
        String url = "http://www.outpan.com/api/get-product.php?apikey=e6092ddde3f7d36258ea7bfa801017fd&barcode=";

        Scanner inb = new Scanner(System.in);
        System.out.println("Enter a barcode");
        String b = inb.nextLine();

        // URL with barcode
        url = url+b;

        // System.out.println(url);

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
        String delimsb = "\"name\": \"";
        String[] tokens = phrase.split(delims);
        String[] name = tokens[2].split(delimsb);

        // If name exists, then show it
        if (name.length > 1) {
            String nom = name[1];
            System.out.println(nom);
        }

        // If it doesn't, then set it
        else {
            setName(url);
        }

    }

    // Set Name of product
    private void setName(String url) throws Exception {

        // URL changes for setting name
        String urlb = url.replace("get-product","edit-name");
        urlb = urlb+"&name=";

        Scanner in = new Scanner(System.in);
        System.out.println("Enter a name");
        String s = in.nextLine();

        // Name of product asked and added to request, with correct HTML form
        String namef = s.replace(" ","+");
        urlb = urlb+namef;

        System.out.println(urlb);

        URL objb = new URL(urlb);
        HttpURLConnection conb = (HttpURLConnection) objb.openConnection();

        // optional default is GET
        conb.setRequestMethod("GET");

        //add request header
        conb.setRequestProperty("User-Agent", USER_AGENT);

        /*int responseCode = conb.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);*/

        BufferedReader inb = new BufferedReader(
                new InputStreamReader(conb.getInputStream()));
        String inputLineb;
        StringBuffer responseb = new StringBuffer();

        while ((inputLineb = inb.readLine()) != null) {
            responseb.append(inputLineb);
        }
        inb.close();

        URL objc = new URL(url);
        HttpURLConnection conc = (HttpURLConnection) objc.openConnection();

        conc.setRequestMethod("GET");
        conc.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader inc = new BufferedReader(
                new InputStreamReader(conc.getInputStream()));
        String inputLinec;
        StringBuffer responsec = new StringBuffer();

        while ((inputLinec = inc.readLine()) != null) {
            responsec.append(inputLinec);
        }
        inc.close();

        String phrasec = responsec.toString();
        String delims = "\",";
        String delimsb = "\"name\": \"";
        String[] tokensc = phrasec.split(delims);
        String[] namec = tokensc[2].split(delimsb);

        // Name is shown at the end
        String nomc = namec[1];
        System.out.println(nomc);

    }

}