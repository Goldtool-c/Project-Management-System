package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDeleteService {
    private final TaskDAO DAO;
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    public int delete(int id) {
        TaskModel task = (TaskModel) DAO.show(id);
        ProjectModel pm = (ProjectModel) projectDAO.show(task.getPm().getId());
        UserModel um = null;
        if (task.getResponsible() != null) {
            um = (UserModel) userDAO.show(task.getResponsible().getId());
        }
        for (int i = 0; i < pm.getTasks().size(); i++) {
            if (pm.getTasks().get(i).getId() == id) {
                pm.getTasks().remove(i);
                break;
            }
        }
        if (um != null) {
            for (int i = 0; i < um.getTasks().size(); i++) {
                if (um.getTask(i).getId() == id) {
                    um.getTasks().remove(i);
                    break;
                }
            }
        }
        projectDAO.update(pm);
        for (int i = 0; i <= DAO.getID(); i++) {
            if(DAO.show(i)!=null) {
                if (DAO.show(i).getId() == id) {
                    DAO.delete(i);
                    break;
                }
            }
        }
        //DAO.delete(id);
        return pm.getId();
    }
}
