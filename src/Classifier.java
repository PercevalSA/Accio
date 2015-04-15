/**
 * This is the interface for a classifier.  A classifier only needs
 * three methods, one for evaluating examples, one for returning a
 * description of the learning algorithm used, and a third for
 * returning the "author" of the program.  Generally, the actual
 * learning will go into the constructer so that the computed
 * classifier is returned.
 */
public interface Classifier {

    /** A method for predicting the label of a given example <tt>ex</tt>
     * represented, as in the rest of the code, as an array of values
     * for each of the attributes.  The method should return a
     * prediction, i.e., 0 or 1.
     */
    public int predict(int[] ex);
}
