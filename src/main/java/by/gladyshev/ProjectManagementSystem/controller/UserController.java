package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserDAO DAO;
    @Autowired
    public UserController(UserDAO DAO) {
        this.DAO = DAO;
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
        model.addAttribute("userModel", DAO.show(id));
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
