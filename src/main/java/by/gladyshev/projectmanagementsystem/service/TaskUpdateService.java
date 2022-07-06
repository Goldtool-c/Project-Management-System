package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUpdateService {
    private final TaskDAO DAO;
    private final ProjectDAO projectDAO;
    public void update(TaskModel task) {
        TaskModel res = (TaskModel) DAO.show(task.getId());
        assert res != null;
        ProjectModel pm = (ProjectModel) projectDAO.show(res.getPm().getId());
        System.out.println(pm.getId());
        System.out.println(pm.getTasks());
        /*System.out.println("________");
        System.out.println(res);
        System.out.println("________");*/
        for (int i = 0; i < pm.getTasks().size(); i++) {
            if(pm.getTasks().get(i).getName().equals(res.getName()))
            {
                task.setPm(pm);
                task.setByShortName(task.getName());
                pm.getTasks().set(i, task);
            }
        }
        res.setByShortName(task.getShortName());
        DAO.update(res);
        System.out.println(pm.getId());
        System.out.println(pm.getTasks());
        DAO.decentTaskInitialization(pm);
        System.out.println(pm.getId());
        System.out.println(pm.getTasks());
        for (int i = 0; i < pm.getTasks().size(); i++) {
            if (pm.getTasks().get(i).getId() == res.getId()) {
                System.out.println("______________________________");
                System.out.println(res);
                pm.getTasks().set(i, res);
                System.out.println(pm.getTasks().get(i));
                System.out.println("______________________________");
            }
        }
        projectDAO.update(pm);
    }
}
