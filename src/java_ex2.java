import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class java_ex2 {
    //TODO change path before submitting
    private static final String TRAIN_PATH = "C:\\Users\\DANIEL\\IdeaProjects\\AI-Ex2\\src\\train.txt";
    private static final String TEST_PATH = "C:\\Users\\DANIEL\\IdeaProjects\\AI-Ex2\\src\\test.txt";


    public static void main(String[] args) {
        String[] fields = null;
        String[] labels = null;
        Example[] data = null;
        Example[] test = null;
        try {
            DataReader dataReader = new DataReader(TRAIN_PATH);

            //get fields and labels
            fields = dataReader.getFields();
            labels = dataReader.getLabels();

            //get train data
            data = dataReader.getData();

            //get test
            dataReader = new DataReader(TEST_PATH);
            test = dataReader.getData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        String[] resultsDT = DT.predict(data, labels);
        String[] resultsKNN = KNN.predict(data, labels, test);
        String[] resultsNaive = Naive.predict(data, labels, test);

        //calculate accuracy
        double accuracy1 = 0;
        double accuracy2 = calcAccuracy(test, resultsKNN);
        double accuracy3 = calcAccuracy(test, resultsNaive);

        //writing the results
        List<String> lines = new LinkedList<>();
        lines.add("Num\tDT\tKNN>\tnaiveBase");
        for (int i = 0; i < test.length; i++) {
            lines.add((i + 1) + "\t" + resultsDT[i] + "\t" + resultsKNN[i] + "\t" + resultsNaive[i]);
        }
//        lines.add(accuracy1);
        lines.add("\t" + accuracy1 + "\t" + accuracy2 + "\t" + accuracy3);

        Path file = Paths.get("output.txt");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //calculate the accuracy of the prediction
    private static double calcAccuracy(Example[] data, String[] labels) {
        int accuracy = 0;

        //compare prediction to the true label
        for (int i = 0; i < data.length; i++) {
            if (data[i].getLabel().equals(labels[i])) {
                accuracy++;
            }
        }

        //calculate the result and round it
        double result = (double) accuracy / (double) data.length;
        return round(result, 2);
    }

    //round double
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}