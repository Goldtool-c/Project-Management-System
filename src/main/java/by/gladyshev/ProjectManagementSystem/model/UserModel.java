package by.gladyshev.ProjectManagementSystem.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserModel implements MyModel {
    private int id;
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 1, max = 150, message = "Name length should be more than 1 and less than 150 symbols")
    @Email(message = "name should be email format")
    private String name;
    private int password;
    private String role;
    private List<TaskModel> tasks = new ArrayList<>();
    public UserModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserModel(String name, int password) {
        this.name = name;
        this.password = password;
    }

    public UserModel(int id, String name, int password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public int getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskModel> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return id == userModel.id &&
                Objects.equals(name, userModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
