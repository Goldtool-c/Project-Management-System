package by.gladyshev.projectmanagementsystem.repository;

import by.gladyshev.projectmanagementsystem.model.MyModel;

import java.util.List;

public interface Storage {
    MyModel get(int id);
    void add(MyModel model);
    void setAll(List<MyModel> models);
    List<MyModel> getAll();
    int Size();
    MyModel getByCriteria(Criteria criteria);
    void delete(int id);
}
