import java.io.IOException;
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

        //TODO remove
        for (String item : fields) {
            System.out.println(item);
        }
        System.out.println("-------");
        System.out.println(labels[0]);
        System.out.println(labels[1]);

        String[] resultsDT = DT.predict(data, labels);
        String[] resultsKNN = KNN.predict(data, labels, test);
        String[] resultsNaive = Naive.predict(data, labels, test);

        //writing the results
        List<String> lines = new LinkedList<>();
        lines.add("Num\tDT\tKNN>\tnaiveBase");
        for (int i = 0; i < test.length; i++) {
            lines.add((i + 1) + "\t" + resultsDT[i] + "\t" + resultsKNN[i] + "\t" + resultsNaive[i]);
        }

        Path file = Paths.get("output.txt");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}