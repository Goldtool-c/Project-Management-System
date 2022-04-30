package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.MyModel;

import java.util.List;

public interface Storage {
    MyModel get(int id);
    void add(MyModel model);
    void setAll(List<MyModel> models);
    List<MyModel> getAll();
    int Size();
    MyModel getByCriteria(Criteria criteria) throws IllegalAccessException;
    void delete(int id);
}
