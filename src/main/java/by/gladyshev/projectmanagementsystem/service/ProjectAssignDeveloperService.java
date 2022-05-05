package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.Criteria;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import by.gladyshev.projectmanagementsystem.repository.Search;
import by.gladyshev.projectmanagementsystem.repository.UserRepository;

public class ProjectAssignDeveloperService {
    public static int assign(int uId, int pId, ProjectDAO DAO)
    {
        UserModel um;
        ProjectModel pm = null;
        try {
            pm = (ProjectModel) Search.search(new Criteria("id", pId), ProjectRepository.INSTANCE);
            um = (UserModel) Search.search(new Criteria("id", uId), UserRepository.INSTANCE);
            pm.assignDeveloper(um);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        DAO.update(pm);
        return pm.getId();
    }
}
