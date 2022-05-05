package by.gladyshev.projectmanagementsystem.controller;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.TaskDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.*;
import by.gladyshev.projectmanagementsystem.service.ProjectAssignDeveloperService;
import by.gladyshev.projectmanagementsystem.service.ProjectUpdateService;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import by.gladyshev.projectmanagementsystem.util.ProjectFilter;
import by.gladyshev.projectmanagementsystem.validator.ShowAccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private ProjectDAO DAO;
    private UserDAO userDAO;
    private TaskDAO taskDAO;
    private List<String> sort = new ArrayList<>();
    private String currentSort = "id";
    private ShowAccessValidator accessValid = ShowAccessValidator.getInstance();
    private List<MyModel> filtered = new ArrayList<>();
    private int activePage = 1;
    public ProjectController(@Autowired ProjectDAO dao,@Autowired UserDAO userDAO, @Autowired TaskDAO taskDAO)
    {
        this.taskDAO = taskDAO;
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
            List<ProjectModel> pm = DAO.index(currentSort);
            List<ProjectModel> show = new ArrayList<>();
            for (int i = 0; i < pm.size()&&i<10; i++) {//get first 10 to display them on a page
                show.add(pm.get(i));
            }
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
            model.addAttribute("projects", show);
            model.addAttribute("sort", sort);
            model.addAttribute("pages", pagesNumber(ProjectRepository.INSTANCE.getAll()));
            model.addAttribute("activePage", activePage);
            model.addAttribute("filter", new ProjectFilter());
            model.addAttribute("users", UserRepository.INSTANCE.getAll());
            return "projects/index";
        }
        return "redirect:/error/notEnoughRights";
    }
    @GetMapping("/page/{id}")
    public String page(@PathVariable("id") int id, Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            activePage=id;
            List<ProjectModel> pm = DAO.index(currentSort);
            List<ProjectModel> show = new ArrayList<>();
            for (int i = ((id-1)*10); i < pm.size()&&i<(id*10); i++) {///get 10 to display them on a page
                show.add(pm.get(i));
            }
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
            model.addAttribute("projects", show);
            model.addAttribute("sort", sort);
            model.addAttribute("pages", pagesNumber(ProjectRepository.INSTANCE.getAll()));
            model.addAttribute("activePage", activePage);
            model.addAttribute("filter", new ProjectFilter());
            model.addAttribute("users", UserRepository.INSTANCE.getAll());
            return "projects/index";
        }
        return "redirect:/error/notEnoughRights";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model)
    {
        if(accessValid.showValid((ProjectModel) DAO.show(id))) {
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
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
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
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
            int pmId = ProjectAssignDeveloperService.assign(uId, pId, DAO);
            return "redirect:/projects/" + pmId;
        } else {
            return "redirect:/error/notEnoughRights";
        }
    }
    @GetMapping("/filter")
    public String filter(Model model)
    {
        activePage = 1;
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        model.addAttribute("projects", filtered);
        model.addAttribute("pages", pagesNumber(filtered));
        return "projects/filtered";
    }
    @GetMapping("/filter/page/{id}")
    public String pageFilter(@PathVariable("id") int id, Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
            model.addAttribute("activeUser", ActiveUser.getActiveUser());
            activePage=id;
            List<MyModel> pm = filtered;
            List<MyModel> show = new ArrayList<>();
            for (int i = ((id-1)*10); i < pm.size()&&i<(id*10); i++) {
                show.add(pm.get(i));
            }
            model.addAttribute("projects", show);
            model.addAttribute("pages", pagesNumber(pm));
            model.addAttribute("activePage", activePage);
            return "projects/filtered";
        }
        return "redirect:/error/notEnoughRights";
    }
    @GetMapping("/createTask/{id}")
    public String createTask(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("activeUser", ActiveUser.getActiveUser());
        model.addAttribute("projectId", id);
        model.addAttribute("task", new TaskModel());
        return "projects/createTask";
    }
    @PostMapping("/{id}")
    public String update(@ModelAttribute("projectModel")@Valid ProjectModel pm, BindingResult br,
                         @PathVariable("id")int id)
    {
        if (br.hasErrors()) {
            return "projects/edit";
        }
        ProjectUpdateService.update(pm, taskDAO, DAO, id);
        return "redirect:/projects";
    }
    @PostMapping("/delete/{id}")
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
        ProjectModel dublicate = (ProjectModel) ProjectRepository.INSTANCE.getByCriteria(new Criteria("name", pm.getName()));
        if(dublicate==null) {
            DAO.save(pm);
        } else
        {
            FieldError error = new FieldError("projectModel", "name", "project "+pm.getName()+
                    " already exists");
            br.addError(error);
            return "projects/new";
        }
        return "redirect:/projects";
    }
    @PostMapping("/sort")
    public String sort(@ModelAttribute("sort") String sortType)
    {
        Collections.swap(sort, 0, sort.indexOf(sortType));
        currentSort = sortType;
        return "redirect:/projects";
    }
    @PostMapping("/filter")
    public String filter(@ModelAttribute("filter") ProjectFilter filter)
    {
        filtered = ProjectFilter.filter(filter);
        return "redirect:/projects/filter";
    }
    @PostMapping("/createTask/{id}")
    public String createTask(@PathVariable("id") int id, @ModelAttribute("task")@Valid TaskModel task, BindingResult br)
    {
        ProjectModel pm;
        try {
            pm = (ProjectModel) Search.search(new Criteria("id", id), ProjectRepository.INSTANCE);
            String newName = pm.getName()+"|"+task.getName();//setname to task in format 'projectName|taskName'
            TaskModel dublicate = (TaskModel) TaskRepository.INSTANCE.getByCriteria(
                    new Criteria("name", newName)
            );
            if(dublicate!=null)
            {
                FieldError error = new FieldError("task", "name", "Task with this name already exists");
                br.addError(error);
            }
            if(task.getName().contains("|"))
            {
                FieldError error = new FieldError("task", "name", "symbol '|' is forbidden");
                br.addError(error);
            }
            if(br.hasErrors())
            {
                return "projects/createTask";
            }
            task.setName(newName);
            task.setPm(pm);
            taskDAO.save(task);
            task.setId(taskDAO.getID());
            pm.addTask(task);
            DAO.update(pm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "redirect:/projects/"+id;
    }
    private int[] pagesNumber(List<MyModel> pm)
    {
        int n = pm.size()/10+1;
        int res[];
        if(pm.size()%10==0)
        {
            res = new int[n-1];
        } else
        {
            res = new int[n];
        }
        for (int i = 0; i < res.length; i++) {
            res[i]=i+1;
        }
        return res;
    }
}
