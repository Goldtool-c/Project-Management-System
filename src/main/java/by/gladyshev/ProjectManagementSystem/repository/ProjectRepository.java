package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.entity.Task;
import by.gladyshev.ProjectManagementSystem.entity.user.User;
import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public enum ProjectRepository implements Storage {
    INSTANCE;
    private List<Model> projects = new ArrayList<>();
    @Override
    public List<Model> getAll() {
        return projects;
    }

    @Override
    public int Size() {
        return projects.size();
    }

    @Override
    public Model getByCriteria(Criteria criteria) throws IllegalAccessException {
        return Search.search(criteria, this);
    }

    @Override
    public void setAll(List<Model> projects) {
        this.projects = projects;
    }

    @Override
    public void add(Model pm) {
        projects.add(pm);
    }

    @Override
    public Model get(int id) {
        return projects.get(id);
    }
}
