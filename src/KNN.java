import java.util.LinkedList;
import java.util.List;

//Implementation of the KNN algorithm as we learned in class
//The class calculates the label of examples by the K nearest neighbors
public class KNN {
    private static final int K = 5;

    //predict the labels
    public static String[] predict(Example[] data, String[] labels, Example[] test) {
        String[] results = new String[test.length];

        //go over the data
        for (int i = 0; i < test.length; i++) {
            results[i] = calcLabel(data, labels, test[i]);
        }

        return results;
    }

    /**
     * Calculate the label of a given example
     *
     * @param data    The data set
     * @param labels  The possible labels
     * @param example The given example
     * @return The predicted label
     */
    private static String calcLabel(Example[] data, String[] labels, Example example) {
        List<Point> distances = new LinkedList<>();
        int i;

        //go over the data
        for (i = 0; i < data.length; i++) {
            //calculate the distance of the example from data[i]
            int distance = calcDistance(example, data[i]);
            //add the distance to list
            distances.add(new Point(i, distance));
        }

        //sort the distances first by distance, second by index (smaller first)
        distances.sort((o1, o2) -> {
            int result = o1.distance - o2.distance;
            if (result != 0) {
                return result;
            }
            return o1.index - o2.index;
        });

        //get the index of the label
        int[] label = new int[2];
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

    //Point class
    //Each point have index of an example and distance from an given example
    private static class Point {
        public int index;
        public int distance;

        public Point(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }
    }
}