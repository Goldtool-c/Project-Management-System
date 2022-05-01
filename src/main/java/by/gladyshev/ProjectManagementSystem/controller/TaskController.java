package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.TaskDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.Task;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private TaskDAO DAO;
    private ProjectDAO projectDAO;
    public TaskController(@Autowired TaskDAO DAO, @Autowired ProjectDAO projectDAO) {
        this.DAO = DAO;
        this.projectDAO = projectDAO;
        projectDAO.index("id"); // without this devs wont be assigned due to order of creating beans
        // projectDao creates first, so in order to avoid nullPointerException was decided not to assign developers in
        // projectDao constructor
    }
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id")int id, Model model)
    {
        TaskModel task = (TaskModel) DAO.show(id);
        System.out.println(task.getPm());
        model.addAttribute("task", task);
        return "tasks/show";
    }
}
