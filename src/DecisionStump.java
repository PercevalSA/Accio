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
}
