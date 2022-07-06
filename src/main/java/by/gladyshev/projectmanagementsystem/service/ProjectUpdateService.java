package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectUpdateService {
    public void update(ProjectModel pm, TaskDAO taskDAO, ProjectDAO projectDAO, int id) {
        ProjectModel pm1 = (ProjectModel) projectDAO.show(id);
        taskDAO.decentTaskInitialization(pm1);
        pm1.setName(pm.getName());
        for (int i = 0; i < pm1.getTasks().size(); i++) {
            taskDAO.update(pm1.getTasks().get(i));
        }
        projectDAO.update(pm1);
    }
}
