package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.MyModel;

import java.util.ArrayList;
import java.util.List;

public enum ProjectRepository implements Storage {
    INSTANCE;
    private List<MyModel> projects = new ArrayList<>();
    @Override
    public List<MyModel> getAll() {
        return projects;
    }

    @Override
    public int Size() {
        return projects.size();
    }

    @Override
    public MyModel getByCriteria(Criteria criteria) throws IllegalAccessException {
        return Search.search(criteria, this);
    }

    @Override
    public void delete(int id) {
        projects.remove(id);
    }

    @Override
    public void setAll(List<MyModel> projects) {
        this.projects = projects;
    }

    @Override
    public void add(MyModel pm) {
        projects.add(pm);
    }

    @Override
    public MyModel get(int id) {
        return projects.get(id);
    }

}
