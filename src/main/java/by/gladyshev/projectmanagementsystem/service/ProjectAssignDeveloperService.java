package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAssignDeveloperService {
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    public int assign(int uId, int pId) {
        UserModel um = (UserModel) userDAO.show(uId);
        ProjectModel pm = (ProjectModel) projectDAO.show(pId);
        pm.assignDeveloper(um);
        projectDAO.update(pm);
        return pm.getId();
    }
}
