package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.Model;

import java.util.List;

public interface Storage {
    Model get(int id);
    void add(Model model);
    void setAll(List<Model> models);
    List<Model> getAll();
    int Size();
    Model getByCriteria(Criteria criteria) throws IllegalAccessException;
    void delete(int id);
}
