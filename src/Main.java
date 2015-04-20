import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by any on 16/04/15.
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        accioServer.main(args); // Launch image recognition
        
        // Checking if image recognition has been done
        while(getLatestFilefromDir("C:\\Users\\user\\Documents\\workspace\\Camera\\data").toString().equals("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".test") == false) {

        	Thread.sleep(1000);
        
        }
            try {
                AdaBoost.main(args); // Launch Adaboost algorithm
                System.out.println("Adaboost");
                
                try {
                    BufferedReader inputReader = new BufferedReader(new FileReader("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".testout")); // Get the name of the fruit or vegetable which has been recognised
                    String nextLine;

                    while((nextLine = inputReader.readLine()) != null) {
                        envoiBDD(nextLine, args[1], args[2]); // Add or delete it to/from the MySQL DB

                    }
                    
                    inputReader.close();
                    File todelete = new File("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".test");
                    todelete.delete();
                    File newfile = new File("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".test.new");
                    if (newfile.exists()) {
                    	newfile.renameTo(new File("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".test"));
                    	AdaBoost.main(args); // Launch Adaboost algorithm
                    	
                    	inputReader = new BufferedReader(new FileReader("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".testout")); // Get the name of the other fruit or vegetable which has been recognised

                        while((nextLine = inputReader.readLine()) != null) {
                            envoiBDD(nextLine, args[1], args[2]); // Add or delete it to/from the MySQL DB
                        }
                        
                        inputReader.close();
                        todelete = new File("C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+args[0]+".test");
                        todelete.delete();
                    	
                    }

                } catch (FileNotFoundException exc1) {
                    exc1.printStackTrace();

                } catch (IOException exc2) {
                    exc2.printStackTrace();

                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private static File getLatestFilefromDir(String dirPath){ // Method designed to check for the latest edited file in the directory
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

    private final static String USER_AGENT = "Mozilla/5.0";

    private static void envoiBDD(String nom, String addordelete, String boite) { // Method designed for MySQL requests through PHP scripts

        String urladd = null;
        if (addordelete.equals("add")) {
            urladd = "http://perceval.tk/pact/connection-add-product-bis.php?";
        } else if (addordelete.equals("delete")) {
            urladd = "http://perceval.tk/pact/connection-delete-product-bis.php?";
        }
        String urlParameters = "nom="+nom+"&boite="+boite;
        String urladdbis = urladd+urlParameters;

        URL objadd = null;
        try {
            objadd = new URL(urladdbis);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conadd = null;
        try {
            conadd = (HttpURLConnection) objadd.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // GET request
        try {
            conadd.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // add request header
        conadd.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader inadd = null;
        try {
            inadd = new BufferedReader(
                    new InputStreamReader(conadd.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLineadd;
        StringBuffer responseadd = new StringBuffer();

        try {
            while ((inputLineadd = inadd.readLine()) != null) {
                responseadd.append(inputLineadd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inadd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String out = responseadd.toString();

        System.out.println(out);
        
        

    }

}