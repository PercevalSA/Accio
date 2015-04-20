import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.lang.*;

/**
 * This class implements the AdaBoost algorithm, with decision stumps serving as
 * the weak learning algorithm.
 */
public class AdaBoost implements Classifier {

    private static final int NUM_ROUNDS = 100;
    
    private double weight[];
    private double alpha[] = new double[NUM_ROUNDS];
    private DecisionStump hypotheses[] = new DecisionStump[NUM_ROUNDS];

    /**
     * 
     */
    public AdaBoost(BinaryDataSet d) {
        double negError = 0, sameError = 0;
        double minError = 1;
        DecisionStump best = null;
        
        weight = new double[d.numTrainExs];
        for (int i = 0; i < d.numTrainExs; i++) {
            weight[i] = (1 / d.numTrainExs);
            //System.out.println(d.numTrainExs);
            //System.out.println(weight[i]);
        }
        for (int t = 0; t < NUM_ROUNDS; t++) {
            // get best hypothesis
            for (int i = 0; i < d.numAttrs; i++) {
                DecisionStump stump = new DecisionStump(i, true);
                DecisionStump negStump = new DecisionStump(i, false);
                sameError = stumpError(stump, d);
                negError = stumpError(negStump, d);
                if (sameError < minError) {
                    minError = sameError;
                    //System.out.println(minError);
                    best = stump;
                }
                if (negError < minError) {
                    minError = negError;
                    //System.out.println(minError);
                    best = negStump;
                }
            }
            // store hypothesis and its weight
            hypotheses[t] = best;
            alpha[t] = 0.5 * Math.log((1 - minError) / minError);
            // update weights
            for (int i = 0; i < d.numTrainExs; i++) {
                if (best.predict(d.trainEx[i]) != d.trainLabel[i]) {
                    weight[i] *= Math.exp(alpha[t]);
                    //System.out.println(weight[i]);
                } else {
                    weight[i] *= Math.exp(-alpha[t]);
                    //System.out.println(weight[i]);
                }
            }
            weight = normalize(weight);
        }
        
    }

    /**
     * Predicts the result favored by majority of hypotheses (decision stumps)
     */
    public int predict(int[] ex) {
        double weightTrue = 0, weightFalse = 0;
        for (int i = 0; i < hypotheses.length; i++) {
            if (hypotheses[i].predict(ex) == 0) {
                weightFalse += alpha[i];
                //System.out.println(weightFalse);
            } else {
                weightTrue += alpha[i];
                //System.out.println(weightTrue);
            }
        }
        if (weightTrue > weightFalse)
            return 1;
        return 0;
    }
    
    private double[] normalize(double[] N) {
        double[] weights = new double[N.length];
        double total = 0;
        for (int i = 0; i < N.length; i++)
            total += N[i];
        for (int i = 0; i < N.length; i++)
            weights[i] = N[i] / total;
        return weights;
    }
    
    private double stumpError(DecisionStump stump, BinaryDataSet d) {
        double error = 0;
        for (int i = 0; i < d.numTrainExs; i++) {
            if (stump.predict(d.trainEx[i]) != d.trainLabel[i]) {
                error += weight[i];
                //System.out.println(error);
            }
        }
        return error;
        //System.out.println(error);
    }

    private final static String USER_AGENT = "Mozilla/5.0";

    /*private String openResult(String filestem) {

        String nom = null;
        String nombis = "no";
        try {
            open_file(filestem + ".testout");
            try {
                while (read_line() != null) {
                    nom = read_line();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!nom.equals(null)) {
            return nom;
        } else {
            return nombis;
        }
    }*/

    private void envoiBDD(String nom) {

        int boite = 7;

        String urladd = "http://localhost/pact/connection-add-product.php?";
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

    private String filename;
    private int line_count;
    private BufferedReader in;

    private void open_file(String filename) throws FileNotFoundException {
        BufferedReader in;

        this.filename = filename;
        this.line_count = 0;

        try {
            in = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.err.print("File "+filename+" not found.\n");
            throw e;
        }
        this.in = in;
    }

    private String read_line() throws IOException {
        String line;

        line_count++;

        try {
            line = in.readLine();
        }
        catch (IOException e) {
            System.err.println("Error reading line "+line_count+" in file "+filename);
            throw e;
        }
        return line;
    }


    /**
     * A simple main for testing this algorithm. This main reads a filestem from
     * the command line, runs the learning algorithm on this dataset, and prints
     * the test predictions to filestem.testout.
     */
    public static void main(String argv[]) throws FileNotFoundException,
            IOException {

        if (argv.length < 1) {
            System.err.println("argument: filestem");
            return;
        }

        String filestem = "C:\\Users\\user\\Documents\\workspace\\Camera\\data\\"+argv[0];
        BinaryDataSet d = new BinaryDataSet(filestem);
        Classifier c = new AdaBoost(d);
        d.printTestPredictions(c, filestem);
    }
}
