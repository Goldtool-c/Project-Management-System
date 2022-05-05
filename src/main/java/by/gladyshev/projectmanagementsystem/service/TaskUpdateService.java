package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.repository.Criteria;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import by.gladyshev.projectmanagementsystem.repository.Search;
import by.gladyshev.projectmanagementsystem.repository.TaskRepository;

public class TaskUpdateService {
    public static void update(int id, TaskModel task, TaskDAO DAO, ProjectDAO projectDAO)
    {
        TaskModel res = null;
        try {
            res = (TaskModel) Search.search(new Criteria("id", task.getId()), TaskRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assert res != null;
        res.setByShortName(task.getName());
        DAO.update(res);
        ProjectModel pm = null;
        try {
            pm = (ProjectModel) Search.search(new Criteria("id", res.getPm().getId()),
                    ProjectRepository.INSTANCE);
            for (int i = 0; i < pm.getTasks().size(); i++) {
                if(pm.getTasks().get(i).getId()==res.getId())
                {
                    pm.getTasks().set(i, res);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        projectDAO.update(pm);
    }
}
