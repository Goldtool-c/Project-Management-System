package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.entity.User;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
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
    public UserController(@Autowired UserDAO DAO,@Autowired ProjectDAO projectDAO) {
        this.DAO = DAO;
        this.projectDAO = projectDAO;
        projectDAO.index("id"); // without this devs wont be assigned due to order of creating beans
        // projectDao creates first, so in order to avoid nullPointerException was decided not to assign developers in
        // projectDao constructor
    }
    @GetMapping
    public String index(Model model)
    {

        model.addAttribute("users", DAO.index("id"));
        return "users/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        UserModel um = (UserModel) DAO.show(id);
        List<ProjectModel> projectModels = new ArrayList<>();
        List<UserModel> temp;
        for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
            temp = ((ProjectModel)ProjectRepository.INSTANCE.get(i)).getDevelopers();
            for (UserModel userModel : temp) {
                if (um.equals(userModel)) {
                    projectModels.add((ProjectModel) ProjectRepository.INSTANCE.get(i));
                }
            }
        }
        model.addAttribute("userModel", um);
        model.addAttribute("projects", projectModels);
        return "users/show";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,Model model)
    {
        model.addAttribute("userModel", DAO.show(id));
        return "users/edit";
    }
    @GetMapping("/new")
    public String newUser(@ModelAttribute("userModel") User um)
    {
        return "users/new";
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
