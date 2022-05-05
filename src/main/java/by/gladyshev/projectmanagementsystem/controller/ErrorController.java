package by.gladyshev.projectmanagementsystem.controller;

import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController{
    @GetMapping("/notEnoughRights")
    public String accessError(Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        return "error/notEnoughRights";
    }
}
