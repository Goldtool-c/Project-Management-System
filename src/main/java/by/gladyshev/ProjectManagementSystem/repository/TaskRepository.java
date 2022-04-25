package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.Model;

import java.util.ArrayList;
import java.util.List;

public enum TaskRepository implements Storage {
    INSTANCE;
    private List<Model> tasks = new ArrayList<>();
    @Override
    public List<Model> getAll() {
        return tasks;
    }

    @Override
    public int Size() {
        return tasks.size();
    }

    @Override
    public Model getByCriteria(Criteria criteria) throws IllegalAccessException {
       return Search.search(criteria, this);
    }

    @Override
    public void setAll(List<Model> projects) {
        this.tasks = projects;
    }

    @Override
    public void add(Model pm) {
        tasks.add(pm);
    }

    @Override
    public Model get(int id) {
        return tasks.get(id);
    }
}
