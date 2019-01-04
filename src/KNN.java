import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KNN {
    private static final int K = 5;

    public static String[] KNN(Example[] data, String[] labels) {
        String[] results = new String[data.length];

        //go over the data
        for (int i = 0; i < data.length; i++) {
            results[i] = calcLabel(data, labels, i);
        }

        return results;
    }

    /**
     * Calculate the label of a given example
     * @param data The data set
     * @param labels The possible labels
     * @param myIndex The given example
     * @return The predicted label
     */
    private static String calcLabel(Example[] data, String[] labels, int myIndex) {
        List<Point> distances = new LinkedList<>();
        int i;

        //go over the data (skip the given example)
        for (i = 0; i < myIndex; i++) {
            //calculate the distance
            int distance = calcDistance(data[myIndex], data[i]);
            distances.add(new Point(i, distance));
        }
        for (i++; i < data.length; i++) {
            //calculate the distance
            int distance = calcDistance(data[myIndex], data[i]);
            distances.add(new Point(i, distance));
        }

        //sort the distances
        distances.sort(Comparator.comparingInt(o -> o.distance));

        //get the index of the label
        int[] label = new int[2];
        //check the label with KNN
        //go over the KNN examples and count their labels
        for (i = 0; i < K; i++) {
            //get close example
            int index = distances.get(i).index;
            //check its label
            if (data[index].getLabel().equals(labels[0])) {
                label[0]++;
            } else {
                label[1]++;
            }
        }

        //return the label
        if (label[0] > label[1]) {
            return labels[0];
        }
        return labels[1];
    }

    //calculate the hamming distance
    private static int calcDistance(Example a, Example b) {
        int distance = 0;
        String[] aFields = a.getFields();
        String[] bFields = b.getFields();

        //go over the fields and check if equal
        for (int i = 0; i < aFields.length; i++) {
            if (!aFields[i].equals(bFields[i])) {
                distance++;
            }
        }
        return distance;
    }

    private static class Point {
        public int index;
        public int distance;

        public Point(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }
    }
}