package by.gladyshev.projectmanagementsystem.controller;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.*;
import by.gladyshev.projectmanagementsystem.service.TaskDeleteService;
import by.gladyshev.projectmanagementsystem.service.TaskUpdateService;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import by.gladyshev.projectmanagementsystem.validator.ShowAccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private TaskDAO DAO;
    private ProjectDAO projectDAO;
    private ShowAccessValidator accessValid = ShowAccessValidator.getInstance();
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

        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        TaskModel task = (TaskModel) DAO.show(id);
        if(task.getResponsible()!=null) {
            if (accessValid.showValid(task.getResponsible())) {
                model.addAttribute("task", task);
                return "tasks/show";
            }
        }
        //if responsible==null, only admin should have access to params of this task
        if(ActiveUser.getActiveUser().getRole().equals("admin"))
        {
            model.addAttribute("task", task);
            return "tasks/show";
        }
        return "redirect:/error/notEnoughRights";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        model.addAttribute("task", DAO.show(id));
        return "tasks/edit";
    }
    @GetMapping("/assign/{id}")
    public String assign(@PathVariable("id")int id, Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        List<UserModel> users;
        TaskModel tm = (TaskModel) TaskRepository.INSTANCE.getByCriteria(new Criteria("id", id));
        ProjectModel pm = tm.getPm();
        pm = (ProjectModel) ProjectRepository.INSTANCE.getByCriteria(new Criteria("id", pm.getId()));
        users = pm.getDevelopers();
        model.addAttribute("taskId", id);
        model.addAttribute("users", users);
        //model.addAttribute("toAssign", new UserModel());
        return "tasks/assign";
    }
    @GetMapping("/assign/{TaskId}/{UserId}")
    public String assign(@PathVariable("TaskId")int taskId, @PathVariable("UserId")int userId, @ModelAttribute("toAssign")String um)
    {

        UserModel toAssign = null;
        TaskModel tm = null;
        try {
            toAssign = (UserModel) Search.search(new Criteria("id", userId), UserRepository.INSTANCE);
            tm = (TaskModel) Search.search(new Criteria("id", taskId), TaskRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        tm.setResponsible(toAssign);
        DAO.update(tm);
        return "redirect:/projects/"+tm.getPm().getId();
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
        TaskUpdateService.update(id, task, DAO, projectDAO);
        return "redirect:/tasks/show/"+id;
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id")int id)
    {
        int pmId = TaskDeleteService.delete(id, DAO, projectDAO);

        return "redirect:/projects/"+pmId;
    }
}
