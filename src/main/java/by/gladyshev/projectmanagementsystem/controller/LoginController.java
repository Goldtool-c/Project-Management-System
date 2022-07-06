package by.gladyshev.projectmanagementsystem.controller;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import by.gladyshev.projectmanagementsystem.util.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    private UserDAO userDAO;
    private ProjectDAO projectDAO;

    public LoginController(@Autowired UserDAO userDAO, @Autowired ProjectDAO projectDAO) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
    }

    @PostMapping
    public String login(@ModelAttribute("login") LoginForm lm) {
        UserModel activeUser;
        activeUser = (UserModel) userDAO.searchByName(lm.getLogin());
        if (activeUser != null && (lm.getPassword().hashCode() == activeUser.getPassword())) {
            System.out.println(activeUser + " logged in");//log
            ActiveUser.setActiveUser(activeUser);
            return "redirect:/users/" + activeUser.getId();
        } else {
            System.out.println("failed attempt to log in as " + lm.getLogin());//log
            return "login/failedLogin";
        }
    }

    @GetMapping
    public String login(Model model) {
        model.addAttribute("login", new LoginForm());
        ActiveUser.setActiveUser(new UserModel());
        return "login/login";
    }
}
