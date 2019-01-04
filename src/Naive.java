public class Naive {
    public static String[] predict(Example[] data, String[] labels, Example[] test) {
        String[] results = new String[test.length];

        //the total number of labels
        int[] total = new int[2];
        //go over the data and count how many "yes" and "no" we have
        for (Example example : data) {
            if (example.getLabel().equals(labels[0])) {
                total[0]++;
            } else {
                total[1]++;
            }
        }

        //go over the test
        for (int i = 0; i < test.length; i++) {
            results[i] = calcLabel(data, labels, test[i], total);
        }

        return results;
    }

    //calculate the label of the example
    private static String calcLabel(Example[] data, String[] labels, Example example, int[] total) {
        //create counter matrix. rows for "yes/no" and cols for fields
        int[][] fieldCounter = new int[2][example.getFields().length];

        String[] exampleFields = example.getFields();

        //for each train example
        for (Example train : data) {
            String[] dataFields = train.getFields();
            String trainLabel = train.getLabel();

            //for each field
            for (int i = 0; i < dataFields.length; i++) {
                //check if the field is equal to the example field
                if (exampleFields[i].equals(dataFields[i])) {
                    //check the train example label
                    if (trainLabel.equals(labels[0])) {
                        fieldCounter[0][i]++;
                    } else {
                        fieldCounter[1][i]++;
                    }
                }
            }
        }

        //result for label 1
        double result1 = 1;
        for (int field : fieldCounter[0]) {
            result1 *= (double) ((double) field / (double) total[0]);
        }
        result1 *= (double) ((double) total[0] / (double) ((double) total[0] + (double) total[1]));

        //result for label 2
        double result2 = 1;
        for (int field : fieldCounter[1]) {
            result2 *= (double) ((double) field / (double) total[1]);
        }
        result2 *= (double) ((double) total[1] / ((double) ((double) total[0] + (double) total[1])));

        if (result1 > result2) {
            return labels[0];
        }
        return labels[1];
    }
}