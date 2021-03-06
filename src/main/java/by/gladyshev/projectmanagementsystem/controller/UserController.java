package by.gladyshev.projectmanagementsystem.controller;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.entity.User;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import by.gladyshev.projectmanagementsystem.validator.ShowAccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    public UserController(@Autowired UserDAO DAO, @Autowired ProjectDAO projectDAO, @Autowired TaskDAO taskDAO) {
        this.DAO = DAO;
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        projectDAO.index("id"); // without this devs wont be assigned due to order of creating beans
        // projectDao creates first, so in order to avoid nullPointerException it was decided not to assign developers in
        // projectDao constructor
        taskDAO.index("id"); //same reason

    }

    @GetMapping
    public String index(Model model) {
        if (ActiveUser.getActiveUser().getRole().equals("admin")) {
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
            model.addAttribute("users", DAO.index("id"));//что-то нехорошее в маппере
            taskDAO.index("id");
            return "users/index";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        UserModel um = (UserModel) DAO.show(id);
        if (accessValid.showValid(um)) {
            List<ProjectModel> projectModels = new ArrayList<>();
            List<UserModel> temp;
            //get all projects this user assigned to
            for (int i = 0; i <= projectDAO.getID(); i++) {
                ProjectModel tempPM = (ProjectModel) projectDAO.show(i);
                if(tempPM!=null) {
                    temp = tempPM.getDevelopers();
                    for (UserModel userModel : temp) {
                        if (um.equals(userModel)) {
                            projectModels.add(tempPM);
                        }
                    }
                }
            }
            for (int i = 0; i <= taskDAO.getID(); i++) {
                System.out.println(i);
                TaskModel tempTM = (TaskModel) taskDAO.show(i);
                if(tempTM!=null&&tempTM.getResponsible()!=null) {
                    UserModel userModel = tempTM.getResponsible();
                    if (userModel.getName().equals(um.getName())) {
                        um.getTasks().add(tempTM);
                    }
                }
            }
            model.addAttribute("userModel", um);
            model.addAttribute("projects", projectModels);
            return "users/show";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        UserModel um;
        um = (UserModel) DAO.show(id);
        if (accessValid.showValid(um)) {
            model.addAttribute("userModel", um);
            return "users/edit";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("userModel") User um) {
        if (ActiveUser.getActiveUser().getRole().equals("admin")) {
            return "users/new";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("userModel") @Valid UserModel um, BindingResult br,
                         @PathVariable("id") int id) {
        if (br.hasErrors()) {
            return "users/edit";
        }
        repositoryUpdate(um);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        DAO.delete(id);
        return "redirect:/users";
    }

    @PostMapping()
    public String create(@ModelAttribute("userModel") @Valid User user, BindingResult br) {
        //does user with this name already exists?
        UserModel dublicate = (UserModel) DAO.searchByName(user.getName());
        if (dublicate != null) {
            FieldError error = new FieldError("userModel", "name", "This user already exists");
            br.addError(error);
        }
        if (br.hasErrors()) {
            return "users/new";
        }
        UserModel um = new UserModel(user.getName(), user.getPassword().hashCode());
        DAO.save(um);
        return "redirect:/users";
    }

    private void repositoryUpdate(UserModel pm) {
        for (int i = 0; i <= projectDAO.getID(); i++) {
            ProjectModel temp = (ProjectModel) projectDAO.show(i);
            if(temp!=null) {
                System.out.println(temp.getDevelopers());
                for (int j = 0; j < temp.getDevelopers().size(); j++) {
                    if (pm.getId() == temp.getDevelopers().get(j).getId()){
                        temp.getDevelopers().set(j, pm);
                    }
                }
                projectDAO.update(temp);
            }
        }
        DAO.update(pm);
    }
}
