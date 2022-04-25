package by.gladyshev.ProjectManagementSystem.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserModel implements Model {
    private int id;
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 1, max = 150, message = "Name length should be more than 1 and less than 150 symbols")
    @Email
    private String name;

    public UserModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
