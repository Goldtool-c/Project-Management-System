package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.TaskDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.Task;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

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
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("task", DAO.show(id));
        return "tasks/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("task")@Valid TaskModel task,
                       BindingResult br)
    {
        if(task.getName().contains("|"))
        {
            FieldError error = new FieldError("task", "name", "symbol \"|\" is forbidden");
            br.addError(error);
        }
        if (br.hasErrors()) {
            return "tasks/edit";
        }
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
        return "redirect:/tasks/show/"+id;
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id)
    {
        ProjectModel pm = null;
        TaskModel task = null;
        try {
            task = (TaskModel) Search.search(new Criteria("id", id), TaskRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("ekjfpv");
        System.out.println(task);
        System.out.println(task.getPm());
        try {
            pm = (ProjectModel) Search.search(new Criteria("id", task.getPm().getId()), ProjectRepository.INSTANCE);
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
        projectDAO.update(pm);
        System.out.println("size = "+pm.getTasks().size());
        for (int i = 0; i < TaskRepository.INSTANCE.Size(); i++) {
            if(TaskRepository.INSTANCE.get(i).getId()==id)
            {
                TaskRepository.INSTANCE.delete(i);
                break;
            }
        }
        DAO.delete(id);
        return "redirect:/projects/"+pm.getId();
    }
}
