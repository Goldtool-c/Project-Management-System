package by.gladyshev.projectmanagementsystem.repository;

import by.gladyshev.projectmanagementsystem.model.MyModel;

import java.util.ArrayList;
import java.util.List;

public enum UserRepository implements Storage {
    INSTANCE;
    private List<MyModel> devs = new ArrayList<>();
    @Override
    public List<MyModel> getAll() {
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
        this.devs = projects;
    }

    @Override
    public void add(MyModel pm) {
        devs.add(pm);
    }

    @Override
    public MyModel get(int id) {
        return devs.get(id);
    }
}
