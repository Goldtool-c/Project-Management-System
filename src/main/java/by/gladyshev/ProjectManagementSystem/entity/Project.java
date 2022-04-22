package by.gladyshev.ProjectManagementSystem.entity;

import by.gladyshev.ProjectManagementSystem.entity.user.User;

import java.util.List;

public class Project {
    private List<Task> tasks;
    private List<User> users;

    public Project(List<Task> tasks) {
        this.tasks = tasks;
    }
}
