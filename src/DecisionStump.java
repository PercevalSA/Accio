/**
 * @author Jonathan Lack and Eric Denovitzer
 * COS 402 P6
 * DecisionStump.java
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;

/**
 * This class implements decision stumps, a decision tree consisting of 
 * just a single test node.
 */
public class DecisionStump implements Classifier {

    private boolean negated;
    private int attribute;
    
    private String author = "Jonathan Lack and Eric Denovitzer";
    private String description = "Implements decision stumps, a decision " +
    		"tree consisting of just a single test node. Good, truly weak " +
    		"hypotheses for AdaBoost.";

    /**
     * This constructor takes as input a dataset and computes and stores the
     * most frequent class
     */
    public DecisionStump(int attr, boolean neg) {
        attribute = attr;
        negated = neg;
    }

    public int predict(int[] ex) {
        if (!negated)
            return ex[attribute];
        return ex[attribute] ^ 1;
    }

    /** This method returns a description of the learning algorithm. */
    public String algorithmDescription() {
        return description;
    }

    /** This method returns the author of this program. */
    public String author() {
        return author;
    }
}
