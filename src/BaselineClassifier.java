import java.io.*;

/**
 * This is the class for an extremely simple learning algorithm that
 * finds the most frequent class in the training data, and then
 * predicts that each new test example belongs to this class.
 */
public class BaselineClassifier implements Classifier {

    private int most_frequent_class;

    /**
     * This constructor takes as input a dataset and computes and
     * stores the most frequent class
     */
    public BaselineClassifier(DataSet d) {
	int count[] = new int[2];

	for (int i = 0; i < d.numTrainExs; i++)
	    count[d.trainLabel[i]]++;

	most_frequent_class = (count[1] > count[0] ? 1 : 0);
    }

    /** The prediction method ignores the given example and predicts
     * with the most frequent class seen during training.
     */
    public int predict(int[] ex) {
	return most_frequent_class;
    }

    /** A simple main for testing this algorithm.  This main reads a
     * filestem from the command line, runs the learning algorithm on
     * this dataset, and prints the test predictions to filestem.testout.
     */
    public static void main(String argv[])
	throws FileNotFoundException, IOException {

	if (argv.length < 1) {
	    System.err.println("argument: filestem");
	    return;
	}

	String filestem = argv[0];

	DataSet d = new DataSet(filestem);

	Classifier c = new BaselineClassifier(d);

	d.printTestPredictions(c, filestem);
    }

}
