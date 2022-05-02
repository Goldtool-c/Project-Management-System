package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.TaskDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.entity.Task;
import by.gladyshev.ProjectManagementSystem.entity.User;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.*;
import by.gladyshev.ProjectManagementSystem.util.ActiveUser;
import by.gladyshev.ProjectManagementSystem.validator.ShowAccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserDAO DAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private ShowAccessValidator accessValid = ShowAccessValidator.getInstance();
    public UserController(@Autowired UserDAO DAO,@Autowired ProjectDAO projectDAO,@Autowired TaskDAO taskDAO) {
        this.DAO = DAO;
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        projectDAO.index("id"); // without this devs wont be assigned due to order of creating beans
        // projectDao creates first, so in order to avoid nullPointerException it was decided not to assign developers in
        // projectDao constructor
        taskDAO.index("id"); //same reason

    }
    @GetMapping
    public String index(Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
            model.addAttribute("users", DAO.index("id"));//что-то нехорошее в маппере
            taskDAO.index("id");
            return "users/index";
        } else
        {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        UserModel um = null;
        try {
            um = (UserModel) Search.search(new Criteria("id", id), UserRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(accessValid.showValid(um)) {
            List<ProjectModel> projectModels = new ArrayList<>();
            List<UserModel> temp;
            for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
                temp = ((ProjectModel) ProjectRepository.INSTANCE.get(i)).getDevelopers();
                for (UserModel userModel : temp) {
                    if (um.equals(userModel)) {
                        projectModels.add((ProjectModel) ProjectRepository.INSTANCE.get(i));
                    }
                }
            }
            System.out.println(um.getTasks());
            model.addAttribute("userModel", um);
            model.addAttribute("projects", projectModels);
            return "users/show";
        } else
        {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        UserModel um = null;
        try {
        um = (UserModel) Search.search(new Criteria("id", id), UserRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(accessValid.showValid(um)){
            model.addAttribute("userModel", um);
            return "users/edit";
        } else
        {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/new")
    public String newUser(@ModelAttribute("userModel") User um)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            return "users/new";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("userModel")@Valid UserModel um, BindingResult br,
                         @PathVariable("id")int id)
    {
        if (br.hasErrors()) {
            return "users/edit";
        }
        DAO.update(um);
        repositoryUpdate(um);
        return "redirect:/users";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id)
    {
        DAO.delete(id);
        return "redirect:/users";
    }
    @PostMapping()
    public String create(@ModelAttribute("userModel")@Valid User user, BindingResult br)
    {
        if(br.hasErrors())
        {
            return "users/new";
        }
        UserModel um = new UserModel(user.getName(), user.getPassword().hashCode());
        DAO.save(um);
        return "redirect:/users";
    }
    private void repositoryUpdate(UserModel pm) {
        for (int i = 0; i < UserRepository.INSTANCE.Size(); i++) {
            System.out.println(UserRepository.INSTANCE.get(i)+" equals "+
                    pm+" is "+ UserRepository.INSTANCE.get(i).equals(pm));
            if(UserRepository.INSTANCE.get(i).getId()==pm.getId())
            {
                UserRepository.INSTANCE.get(i).setName(pm.getName());
            }
        }
        for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
            for (int j = 0; j < ((ProjectModel)ProjectRepository.INSTANCE.get(i)).getDevelopers().size(); j++) {
                if(pm.getId()==((ProjectModel)ProjectRepository.INSTANCE.get(i)).
                        getDevelopers().get(j).getId())
                {
                    ((ProjectModel)ProjectRepository.INSTANCE.get(i)).getDevelopers().set(j, pm);
                }
            }//idk why, but when we edit username, it appears that user(name=x, id=y) in project.developers and
            //user(name=x, id=y) in UserRepository are not same object, so it was decided to update users in
            //projects manually
            projectDAO.update(ProjectRepository.INSTANCE.get(i));
        }
    }
}
