import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class is in charge of reading the data from the files.
 * The class reads the train.txt file and phrasing it.
 */
public class DataReader {

    private String path;
    private int cols;
    private int rows;

    public DataReader(String path) throws Exception {
        this.path = path;
        BufferedReader br = new BufferedReader(new FileReader(path));

        //get the cols number
        String line = br.readLine();
        String[] split = line.split("\t");
        cols = split.length;

        //get the rows number
        rows = 0;
        line = br.readLine();
        rows++;
        while (line != null) {
            line = br.readLine();
            rows++;
        }
    }

    //Return list of the fields
    public String[] getFields() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        //read the first line and split the fields
        String[] split = br.readLine().split("\t");

        //get the fields
        String[] fields = new String[cols - 1];
        System.arraycopy(split, 0, fields, 0, split.length - 1);

        return fields;
    }

    //get the possibles labels from the file (for example: yes/no)
    public String[] getLabels() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        //skip the first line
        br.readLine();
        //read the first line and split the fields
        String[] split = br.readLine().split("\t");

        String[] labels = new String[2];
        //add the label to the array
        labels[0] = (split[split.length - 1]);

        //look for the second label
        String line = br.readLine();
        while (line != null) {
            split = line.split("\t");
            //if found the second label
            if (!labels[0].equals(split[split.length - 1])) {
                labels[1] = split[split.length - 1];
                break;
            }
            //read the next line
            line = br.readLine();
        }

        return labels;
    }

    //get the data set from the file
    public Example[] getData() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));

        //skip the first line (not a data)
        br.readLine();

        //build the data set
        Example[] data = new Example[rows - 1];
        for (int i = 0; i < rows - 1; i++) {
            //read line and split the fields
            String[] split = br.readLine().split("\t");
            //copy the fields
            String[] fields = new String[cols - 1];
            System.arraycopy(split, 0, fields, 0, cols - 1);
            data[i] = new Example(fields, split[cols - 1]);
        }
        return data;
    }
}