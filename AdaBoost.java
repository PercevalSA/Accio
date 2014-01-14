/**
 * @author Jonathan Lack and Eric Denovitzer
 * COS 402 P6
 * AdaBoost.java
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;

/**
 * This class implements the AdaBoost algorithm, with decision stumps serving as
 * the weak learning algorithm.
 */
public class AdaBoost implements Classifier {

    private static final int NUM_ROUNDS = 100;
    
    private double weight[];
    private double alpha[] = new double[NUM_ROUNDS];
    private DecisionStump hypotheses[] = new DecisionStump[NUM_ROUNDS];
    
    private String author = "Jonathan Lack and Eric Denovitzer";
    private String description = "The AdaBoost algorithm with decision stumps " +
    		"as the weak learning algorithm.";

    /**
     * 
     */
    public AdaBoost(BinaryDataSet d) {
        double negError = 0, sameError = 0;
        double minError = 1;
        DecisionStump best = null;
        
        weight = new double[d.numTrainExs];
        for (int i = 0; i < d.numTrainExs; i++)
            weight[i] = (1 / d.numTrainExs);
        
        for (int t = 0; t < NUM_ROUNDS; t++) {
            // get best hypothesis
            for (int i = 0; i < d.numAttrs; i++) {
                DecisionStump stump = new DecisionStump(i, true);
                DecisionStump negStump = new DecisionStump(i, false);
                sameError = stumpError(stump, d);
                negError = stumpError(negStump, d);
                if (sameError < minError) {
                    minError = sameError;
                    best = stump;
                }
                if (negError < minError) {
                    minError = negError;
                    best = negStump;
                }
            }
            // store hypothesis and its weight
            hypotheses[t] = best;
            alpha[t] = 0.5 * Math.log((1 - minError) / minError);
            // update weights
            for (int i = 0; i < d.numTrainExs; i++) {
                if (best.predict(d.trainEx[i]) != d.trainLabel[i])
                    weight[i] *= Math.pow(Math.E, alpha[t]);
                else 
                    weight[i] *= Math.pow(Math.E, -alpha[t]);
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
            if (hypotheses[i].predict(ex) == 0)
                weightFalse += alpha[i];
            else
                weightTrue += alpha[i];
        }
        if (weightTrue > weightFalse)
            return 1;
        return 0;
    }

    /** This method returns a description of the learning algorithm. */
    public String algorithmDescription() {
        return description;
    }

    /** This method returns the author of this program. */
    public String author() {
        return author;
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
            if (stump.predict(d.trainEx[i]) != d.trainLabel[i])
                error += weight[i];
        }
        return error;
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

        String filestem = argv[0];
        BinaryDataSet d = new BinaryDataSet(filestem);
        Classifier c = new AdaBoost(d);
        d.printTestPredictions(c, filestem);
    }
}
