import java.util.*;

public class DT {
    private Map<Integer, String> attributeInt2String;
    private Map<String, Integer> attributeString2Int;

    public String[] predict(Example[] data, String[] labels, String[] fields, Example[] test) {
        String[] results = new String[data.length];

        //create maps
        attributeString2Int = new HashMap<>();
        attributeInt2String = new HashMap<>();

        for (int i = 0; i < fields.length; i++) {
            attributeString2Int.put(fields[i], i);
            attributeInt2String.put(i, fields[i]);
        }

        Map<Integer, List<String>> attributesInt2Options = createAttributeMap(data);

        Tree tree = DTL(data, attributesInt2Options, labels, getMajority(data, labels));

        for (int i = 0; i < test.length; i++) {
            results[i] = getLabel(test[i], tree);
        }

        return results;
    }


    public Tree DTL(Example[] data, Map<Integer, List<String>> attributes, String[] labels, String def) {
        //if no more examples return default
        if (data.length == 0) {
            return new Tree(def);
        }

        //if no more attributes
        if (attributes.isEmpty()) {
            //select the majority
            String majority = getMajority(data, labels);
            return new Tree(majority);
        }

        //check if all examples have the same label
        boolean flag = true;
        Example first = data[0];
        for (Example example : data) {
            if (!example.getLabel().equals(first.getLabel())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            //select the label
            return new Tree(first.getLabel());
        }

        //get the best attribute and its options
        int best = chooseAttribute(attributes, data, labels);

        List<String> options = attributes.get(best);

        //create new sub-tree
        Tree subTree = new Tree(attributeInt2String.get(best));
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            List<Example> newExamples = new LinkedList<>();

            //add examples with the option
            for (Example example : data) {
                String[] fields = example.getFields();
                if (fields[best].equals(option)) {
                    newExamples.add(example);
                }
            }
            //create a branch for the option
            Example[] newExamplesArray = newExamples.toArray(new Example[newExamples.size()]);
            Map<Integer, List<String>> newAttributes = new HashMap<>(attributes);
            newAttributes.remove(best);
            Tree branch = DTL(newExamplesArray, newAttributes, labels, getMajority(newExamplesArray, labels));
            subTree.addSon(option, branch);
        }

        return subTree;
    }

    private String getMajority(Example[] data, String[] labels) {
        int[] total = new int[2];
        for (Example example : data) {
            if (example.getLabel().equals(labels[0])) {
                total[0]++;
            } else {
                total[1]++;
            }
        }
        if (total[0] > total[1]) {
            return labels[0];
        }
        return labels[1];
    }

    /**
     * Get the label of a given example
     *
     * @param example The given example
     * @param root    The root of the tree
     * @return The predicted label of the example
     */
    public String getLabel(Example example, Tree root) {
        Tree current = root;
        //get the example fields (e.g. "adult", "male")
        String[] fields = example.getFields();

        //while can go down in the tree
        while (!current.sons.isEmpty()) {
            //attribute name (e.g. "sex")
            String attributeName = current.value;
            int attributeIndex = attributeString2Int.get(attributeName);
            //son (e.g. "male")
            String son = fields[attributeIndex];
            //make the son the current node
            current = current.getSons().get(son);
        }
        return current.getValue();
    }

    private int chooseAttribute(Map<Integer, List<String>> attributes, Example[] examples, String[] labels) {
        int[] total = new int[2];

        //count total labels from examples (e.g. total "yes" and "no")
        for (Example example : examples) {
            if (example.getLabel().equals(labels[0])) {
                total[0]++;
            } else {
                total[1]++;
            }
        }
        int sumTotal = total[0] + total[1];

        //calculate the total entropy
        double s = entropy(total[0], total[1]);

        double[] results = new double[attributes.size()];

        //count the labels for each options at each attribute
        int i = 0;
        for (Integer attribute : attributes.keySet()) {
            results[i] = s;
            List<String> options = attributes.get(attribute);
            //for every option in the attribute
            for (int j = 0; j < options.size(); j++) {
                int[] counter = new int[2];
                String option = options.get(j);
                //go over the examples
                for (Example example : examples) {
                    String[] fields = example.getFields();
                    //if the field is relevant
                    if (fields[i].equals(option)) {
                        //check its label and count
                        if (example.getLabel().equals(labels[0])) {
                            counter[0]++;
                        } else {
                            counter[1]++;
                        }
                    }
                }
                double p = ((double) (counter[0] + counter[1])) / (double) sumTotal;
                double entropy = entropy(counter[0], counter[1]);
                results[i] -= p * entropy;
            }
            i++;
        }

        //return the max index
        i = 0;
        int maxIndex = 0;
        double max = Double.NEGATIVE_INFINITY;
        for (Integer attribute : attributes.keySet()) {
            if (results[i] > max) {
                max = results[i];
                maxIndex = attribute;
            }
            i++;
        }

        return maxIndex;
    }

    //create attribute map from each attribute to the possible options
    //(e.g. "sex" --> "male", "female". notice that we use "sex" as index and not as String)
    private Map<Integer, List<String>> createAttributeMap(Example[] data) {
        Map<Integer, List<String>> attributeMap = new HashMap<>();

        //for every field
        for (int i = 0; i < data[0].getFields().length; i++) {
            List<String> options = new LinkedList<>();
            //for every example
            for (Example example : data) {

                String[] fields = example.getFields();
                if (!options.contains(fields[i])) {
                    options.add(fields[i]);
                }
            }
            Collections.sort(options);
            attributeMap.put(i, options);
        }

        return attributeMap;
    }

    private double entropy(double a, double b) {
        double sum = a + b;
        double result = (-a / sum) * (Math.log(a / sum) / Math.log(2)) + (-b / sum) * (Math.log(b / sum) / Math.log(2));
        if (Double.isNaN(result)) {
            return 0;
        }
        return result;
    }

    private class Tree {
        private String value;
        Map<String, Tree> sons;

        public Tree() {
            sons = new HashMap<>();
        }

        public Tree(String value) {
            this.value = value;
            sons = new HashMap<>();
        }

        public void addSon(String key, Tree son) {
            sons.put(key, son);
        }

        public Map<String, Tree> getSons() {
            return sons;
        }

        public String getValue() {
            return value;
        }
    }
}