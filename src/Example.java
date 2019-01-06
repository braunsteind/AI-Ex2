//This class is for the examples, each example fields and label (e.g. fields:"crew", "child", "male". label: "yes")
public class Example {
    private String[] fields;
    private String label;

    //constructor
    public Example(String[] fields, String label) {
        this.fields = fields;
        this.label = label;
    }

    //get the fields
    public String[] getFields() {
        return fields;
    }

    //get the label
    public String getLabel() {
        return label;
    }
}