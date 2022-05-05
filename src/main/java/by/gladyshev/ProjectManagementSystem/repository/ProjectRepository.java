package by.gladyshev.projectmanagementsystem.repository;

import by.gladyshev.projectmanagementsystem.model.MyModel;

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
    public MyModel getByCriteria(Criteria criteria) {
        MyModel model = null;
        try {
            model = Search.search(criteria, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
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
