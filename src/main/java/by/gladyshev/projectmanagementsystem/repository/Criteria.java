package by.gladyshev.projectmanagementsystem.repository;

public class Criteria {
    private String property;
    private Object value;
    public Criteria(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }
}
