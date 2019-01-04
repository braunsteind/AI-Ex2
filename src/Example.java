public class Example {
    private int fieldsNum;
    private String[] fields;
    private String label;

    public Example(String[] fields, String label) {
        this.fieldsNum = fields.length;
        this.fields = fields;
        this.label = label;
    }

    public String[] getFields() {
        return fields;
    }

    public String getLabel() {
        return label;
    }

    public int getFieldsNum() {
        return fieldsNum;
    }
}