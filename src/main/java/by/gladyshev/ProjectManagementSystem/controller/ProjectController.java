package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,Model model)
    {
        model.addAttribute("projectModel", DAO.show(id));
        return "projects/edit";
    }
    @GetMapping("/new")
    public String newProject(@ModelAttribute("projectModel") ProjectModel pm)
    {
        return "projects/new";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("projectModel")@Valid ProjectModel pm, BindingResult br,
                         @PathVariable("id")int id)
    {
        if (br.hasErrors()) {
            return "projects/edit";
        }
        DAO.update(pm);
        return "redirect:/projects";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id)
    {
        DAO.delete(id);
        return "redirect:/projects";
    }
    @PostMapping()
    public String create(@ModelAttribute("projectModel")@Valid ProjectModel pm, BindingResult br)
    {
        if(br.hasErrors())
        {
            return "projects/new";
        }
        DAO.save(pm);
        return "redirect:/projects";
    }

}
