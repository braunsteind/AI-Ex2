import java.util.*;

public class DT {
    private List<Integer> attributes;

    public String[] predict(Example[] data, String[] labels) {
        String[] results = new String[data.length];

        Map<Integer, List<String>> attributeMap = createAttributeMap(data);
        Tree tree = DTL(data, null, labels);
        List<Integer> attributes = new LinkedList<>();

        for (int i = 0; i < data.length; i++) {
            results[i] = getLabel(data[i], tree, attributes);
        }

        return results;
    }


    public Tree DTL(Example[] data, Map<Integer, List<String>> attributes, String[] labels) {
//        if (data.length == 0) {
//            return ;
//        }

        //if no more attributes
        if (attributes.size() == 0) {
            //select the majority
            int[] total = new int[2];
            for (Example example : data) {
                if (example.getLabel().equals(labels[0])) {
                    total[0]++;
                } else {
                    total[1]++;
                }
            }
            if (total[0] > total[1]) {
                return new Tree(labels[0]);
            }
            return new Tree(labels[1]);
        }

        //check if all examples have the same label
        boolean flag = true;
        Example first = data[0];
        for (Example example : data) {
            if (!example.getLabel().equals(first.getLabel())) {
                flag = false;
            }
        }
        if (flag) {
            return new Tree(first.getLabel());
        }

        //get the best attribute and its options
        int best = chooseAttribute(attributes, data, labels);

        List<String> options = attributes.get(best);

        //create new sub-tree
        Tree subTree = new Tree();
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
            Tree branch = DTL(newExamplesArray, newAttributes, labels);
            subTree.addSon(branch);
        }

        return subTree;
    }

    /**
     * Get the label of a given example
     *
     * @param example    The given example
     * @param root       The root of the tree
     * @param attributes Sorted attributes list of the tree
     * @return The predicted label of the example
     */
    public String getLabel(Example example, Tree root, List<Integer> attributes) {
        Tree current = root;
        //get the example fields (e.g. "adult", "male")
        String[] fields = example.getFields();

        //travel the tree by attributes (e.g. "sex")
        for (Integer attribute : attributes) {
            //if no sons return the label (e.g. "yes")
            if (current.getSons().size() == 0) {
                return current.getValue();
            }
            //for each son of the current node (e.g. "male", "female")
            for (Tree son : root.getSons()) {
                //if the right attribute (e.g. "male")
                if (fields[attribute].equals(son.getValue())) {
                    //change the current node to the son
                    current = son;
                }
            }
        }
        return "";
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
        for (int i = 0; i < attributes.size(); i++) {
            results[i] = s;
            int[] counter = new int[2];
            List<String> options = attributes.get(i);
            //for every option in the attribute
            for (int j = 0; j < options.size(); j++) {
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
                    results[i] -= ((counter[0] + counter[1]) / sumTotal) * entropy(counter[0], counter[1]);
                }
            }
        }

        //return the max index
        int maxIndex = 0;
        double max = results[0];
        for (int i = 0; i < results.length; i++) {
            if (results[i] > max) {
                max = results[i];
                maxIndex = i;
            }
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
                if (options.contains(fields[i])) {
                    options.add(fields[i]);
                }
            }
            Collections.sort(options);
            attributeMap.put(i, options);
        }
    }

    private double entropy(int a, int b) {
        return -a * (Math.log(a) / Math.log(2)) - b * (Math.log(b) / Math.log(2));
    }

    private class Tree {
        private String value;
        private List<Tree> sons;

        public Tree() {
            sons = new LinkedList<>();
        }

        public Tree(String value) {
            this.value = value;
            sons = new LinkedList<>();
        }

        public void addSon(Tree son) {
            sons.add(son);
        }

        public List<Tree> getSons() {
            return sons;
        }

        public String getValue() {
            return value;
        }
    }
}