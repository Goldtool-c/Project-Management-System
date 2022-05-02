package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.user.User;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import by.gladyshev.ProjectManagementSystem.util.ActiveUser;
import by.gladyshev.ProjectManagementSystem.util.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    private UserDAO userDAO;
    private ProjectDAO projectDAO;

    public LoginController(@Autowired UserDAO userDAO,@Autowired ProjectDAO projectDAO) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
    }

    @PostMapping
    public String login(@ModelAttribute("login")LoginForm lm)
    {
        UserModel activeUser = null;
        try {
            activeUser = (UserModel)
                    Search.search(new Criteria("name", lm.getLogin()), UserRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(activeUser!=null&&(lm.getPassword().hashCode()==activeUser.getPassword())) {
            System.out.println(activeUser+" logged in");//log
            ActiveUser.setActiveUser(activeUser);
            return "redirect:/users/" + activeUser.getId();
        } else {
            System.out.println("failed attempt to log in as "+lm.getLogin());//log
            return "login/failedLogin";
        }
    }
    @GetMapping
    public String login(Model model)
    {
        model.addAttribute("login", new LoginForm());
        ActiveUser.setActiveUser(new UserModel());
        return "login/login";
    }
}
