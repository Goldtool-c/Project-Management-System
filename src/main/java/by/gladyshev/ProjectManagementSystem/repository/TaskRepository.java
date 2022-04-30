package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.MyModel;

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

    }
    @Override
    public MyModel getByCriteria(Criteria criteria) throws IllegalAccessException {
       return Search.search(criteria, this);
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
