package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.Model;

import java.util.ArrayList;
import java.util.List;

public enum UserRepository implements Storage {
    INSTANCE;
    private List<Model> devs = new ArrayList<>();
    @Override
    public List<Model> getAll() {
        return devs;
    }

    @Override
    public int Size() {
        return devs.size();
    }
    @Override
    public void delete(int id) {
        devs.remove(id);
    }

    @Override
    public Model getByCriteria(Criteria criteria) throws IllegalAccessException {
        return Search.search(criteria, this);
    }

    @Override
    public void setAll(List<Model> projects) {
        this.devs = projects;
    }

    @Override
    public void add(Model pm) {
        devs.add(pm);
    }

    @Override
    public Model get(int id) {
        return devs.get(id);
    }
}
