package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.repository.Criteria;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;

public class ProjectUpdateService {
    public static void update(ProjectModel pm, TaskDAO taskDAO, ProjectDAO projectDAO, int id)
    {
        ProjectModel pm1 = (ProjectModel) ProjectRepository.INSTANCE.getByCriteria(new Criteria("id", id));
        pm1.setName(pm.getName());
        for (int i = 0; i < pm1.getTasks().size(); i++) {
            taskDAO.update(pm1.getTasks().get(i));
        }
        projectDAO.update(pm1);
    }
}
