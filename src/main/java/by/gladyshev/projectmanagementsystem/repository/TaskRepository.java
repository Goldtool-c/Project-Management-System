package by.gladyshev.projectmanagementsystem.repository;

import by.gladyshev.projectmanagementsystem.model.MyModel;

import java.util.ArrayList;
import java.util.List;

public enum TaskRepository implements Storage {
    INSTANCE;
    private List<MyModel> tasks = new ArrayList<>();
    @Override
    public List<MyModel> getAll() {
        return tasks;
    }

    @Override
    public int Size() {
        return tasks.size();
    }
    @Override
    public void delete(int id) {
        tasks.remove(id);
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
    public void setAll(List<MyModel> projects) {
        this.tasks = projects;
    }

    @Override
    public void add(MyModel pm) {
        tasks.add(pm);
    }

    @Override
    public MyModel get(int id) {
        return tasks.get(id);
    }
}
