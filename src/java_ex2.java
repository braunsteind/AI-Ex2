public class java_ex2 {
    private static final String PATH = "C:\\Users\\DANIEL\\IdeaProjects\\AI-Ex2\\src\\train.txt";

    public static void main(String[] args) {
        String[] fields = null;
        String[] labels = null;
        Example[] data = null;

        try {
            DataReader dataReader = new DataReader(PATH);
            fields = dataReader.getFields();
            labels = dataReader.getLabels();
            data = dataReader.getData();
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

        KNN.KNN(data,labels);
    }
}