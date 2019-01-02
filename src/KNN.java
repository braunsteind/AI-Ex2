import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KNN {
    private static final int K = 5;

    public static String[] KNN(String[][] data, String[] lables) {
        //go over the data
        for (int i = 0; i < data.length; i++) {
            calcLabel(data, lables, i);
        }
    }

    private static String calcLabel(String[][] data, String[] labels, int myIndex) {
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
        distances.sort((o1, o2) -> o2.distance - o1.distance);

        int labelIndex = data[0].length - 1;
        int[] label = new int[2];
        //check the label with KNN
        //go over the KNN examples and count their labels
        for (i = 0; i < K; i++) {
            int index = distances.get(i).index;
            if (data[index][labelIndex].equals(labels[0])) {
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
    private static int calcDistance(String[] a, String[] b) {
        int distance = 0;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
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