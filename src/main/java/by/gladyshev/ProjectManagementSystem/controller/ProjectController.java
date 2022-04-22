package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
}
