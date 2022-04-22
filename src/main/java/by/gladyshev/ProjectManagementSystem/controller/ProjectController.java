package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private ProjectDAO DAO;
    @Autowired
    public ProjectController(ProjectDAO dao)
    {
        this.DAO = dao;
    }
    @GetMapping
    public String index(Model model)
    {
        model.addAttribute("projects", DAO.index());
        return "projects/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("projectModel", DAO.show(id));
        return "projects/show";
    }
}
