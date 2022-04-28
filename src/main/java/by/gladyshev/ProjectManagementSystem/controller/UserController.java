package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.user.User;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
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
            System.out.println(ProjectRepository.INSTANCE.get(i));
            for (UserModel userModel : temp) {
                System.out.println("is "+um+" equals "+ userModel);
                if (um.equals(userModel)) {
                    projectModels.add((ProjectModel) ProjectRepository.INSTANCE.get(i));
                }
            }
        }
        model.addAttribute("userModel", um);
        System.out.println("projects for "+um+" are "+ projectModels);
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
    public String newUser(@ModelAttribute("userModel") UserModel um)
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
        return "redirect:/users";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id)
    {
        DAO.delete(id);
        return "redirect:/users";
    }
    @PostMapping()
    public String create(@ModelAttribute("userModel")@Valid UserModel um, BindingResult br)
    {
        if(br.hasErrors())
        {
            return "users/new";
        }
        DAO.save(um);
        return "redirect:/users";
    }
}
