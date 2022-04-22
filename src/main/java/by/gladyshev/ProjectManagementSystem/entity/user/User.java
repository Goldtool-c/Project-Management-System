package by.gladyshev.ProjectManagementSystem.entity.user;

import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.entity.Task;

import java.util.List;

public abstract class User {
    protected String name;
    protected int password;//hash
    protected List<Task> tasks;
    protected Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    // todo only tasks from project
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }
    //todo No more than 1 project
    public void setProject(Project project) {
        this.project = project;
    }
    public void assignTask(Task task)
    {
        tasks.add(task);
    }
    public void completeTask(Task task)
    {
        //todo search and delete task;
    }
}
