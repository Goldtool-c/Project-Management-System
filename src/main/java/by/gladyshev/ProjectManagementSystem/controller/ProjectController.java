package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import by.gladyshev.ProjectManagementSystem.util.ActiveUser;
import by.gladyshev.ProjectManagementSystem.validator.ProjectAccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private ProjectDAO DAO;
    private UserDAO userDAO;
    private List<String> sort = new ArrayList<>();
    private String currentSort = "id";
    private ProjectAccessValidator accessValid = ProjectAccessValidator.getInstance();
    public ProjectController(@Autowired ProjectDAO dao,@Autowired UserDAO userDAO)
    {
        this.userDAO = userDAO;
        this.DAO = dao;
        sort.add("id");
        sort.add("reverse id");
        sort.add("name");
        sort.add("reverse name");
    }
    @GetMapping
    public String index(Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            model.addAttribute("projects", DAO.index(currentSort));
            model.addAttribute("sort", sort);
            return "projects/index";
        }
        return "redirect:/error/notEnoughRights";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        if(accessValid.showValid((ProjectModel) DAO.show(id))) {
            model.addAttribute("projectModel", DAO.show(id));
            return "projects/show";
        } else
        {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            model.addAttribute("projectModel", DAO.show(id));
            return "projects/edit";
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/new")
    public String newProject(@ModelAttribute("projectModel") ProjectModel pm)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            return "projects/new";
        } else {
            return "redirect:/error/notEnoughRights";
        }

    }
    @GetMapping("/{id}/assign")
    public String assign(@PathVariable("id")int id,Model pm, Model um)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            pm.addAttribute("projectModel", DAO.show(id));
            um.addAttribute("userModel", userDAO.index(currentSort));
            return "projects/assign";
        } else {
            return "redirect:/error/notEnoughRights";
        }

    }
    @GetMapping("/assign/{uId}/{pId}")
    public String assign(@PathVariable("uId")int uId, @PathVariable("pId")int pId)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            UserModel um;
            ProjectModel pm = null;
            try {
                pm = (ProjectModel) Search.search(new Criteria("id", pId), ProjectRepository.INSTANCE);
                um = (UserModel) Search.search(new Criteria("id", uId), UserRepository.INSTANCE);
                pm.assignDeveloper(um);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            DAO.update(pm);
            return "redirect:/projects/" + pm.getId();
        } else {
            return "redirect:/error/notEnoughRights";
        }
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
    @PostMapping("/sort")
    public String sort(@ModelAttribute("sort") String sortType)
    {
        Collections.swap(sort, 0, sort.indexOf(sortType));
        currentSort = sortType;
        return "redirect:/projects";
    }

}
