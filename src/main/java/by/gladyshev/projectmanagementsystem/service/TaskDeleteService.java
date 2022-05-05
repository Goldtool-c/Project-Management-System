package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.*;

public class TaskDeleteService {
    public static int delete(int id, TaskDAO DAO, ProjectDAO projectDAO)
    {
        ProjectModel pm = null;
        TaskModel task = null;
        UserModel um = null;
        try {
            task = (TaskModel) Search.search(new Criteria("id", id), TaskRepository.INSTANCE);
            pm = (ProjectModel) Search.search(new Criteria("id", task.getPm().getId()), ProjectRepository.INSTANCE);
            if(task.getResponsible()!=null) {
                um = (UserModel) Search.search(new Criteria("id", task.getResponsible().getId()), UserRepository.INSTANCE);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pm.getTasks().size() ; i++) {
            if(pm.getTasks().get(i).getId()==id)
            {
                pm.getTasks().remove(i);
                break;
            }
        }
        if(um!=null) {
            for (int i = 0; i < um.getTasks().size(); i++) {
                if (um.getTask(i).getId() == id) {
                    um.getTasks().remove(i);
                    break;
                }
            }
        }
        projectDAO.update(pm);
        for (int i = 0; i < TaskRepository.INSTANCE.Size(); i++) {
            if(TaskRepository.INSTANCE.get(i).getId()==id)
            {
                TaskRepository.INSTANCE.delete(i);
                break;
            }
        }
        DAO.delete(id);
        return pm.getId();
    }
}
