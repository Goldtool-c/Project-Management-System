package by.gladyshev.ProjectManagementSystem.repository;

public class Criteria {
    private String property;
    private String value;
    public Criteria(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}
