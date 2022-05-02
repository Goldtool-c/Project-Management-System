package by.gladyshev.ProjectManagementSystem.controller;

import by.gladyshev.ProjectManagementSystem.DAO.ProjectDAO;
import by.gladyshev.ProjectManagementSystem.DAO.TaskDAO;
import by.gladyshev.ProjectManagementSystem.DAO.UserDAO;
import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import by.gladyshev.ProjectManagementSystem.util.ActiveUser;
import by.gladyshev.ProjectManagementSystem.util.ProjectFilter;
import by.gladyshev.ProjectManagementSystem.validator.ShowAccessValidator;
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
            for (int i = 0; i < pm.size()&&i<10; i++) {
                show.add(pm.get(i));
            }
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
            for (int i = ((id-1)*10); i < pm.size()&&i<(id*10); i++) {
                show.add(pm.get(i));
            }
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
    @GetMapping("/filter")
    public String filter(Model model)
    {
        activePage = 1;
        model.addAttribute("projects", filtered);
        model.addAttribute("pages", pagesNumber(filtered));
        return "projects/filtered";
    }
    @GetMapping("/filter/page/{id}")
    public String pageFilter(@PathVariable("id") int id, Model model)
    {
        if(ActiveUser.getActiveUser().getRole().equals("admin")) {
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
        model.addAttribute("projectId", id);
        model.addAttribute("task", new TaskModel());
        return "projects/createTask";
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
    @PostMapping("/filter")
    public String filter(@ModelAttribute("filter") ProjectFilter filter)
    {
        System.out.println(filter);
        filtered = ProjectFilter.filter(filter);
        return "redirect:/projects/filter";
    }
    @PostMapping("/createTask/{id}")
    public String createTask(@PathVariable("id") int id, @ModelAttribute("task")TaskModel task)
    {
        ProjectModel pm = null;
        try {
            pm = (ProjectModel) Search.search(new Criteria("id", id), ProjectRepository.INSTANCE);
            String newName = pm.getName()+"|"+task.getName();
            task.setName(newName);
            task.setPm(pm);
            taskDAO.save(task);
            task.setId(taskDAO.getID());
            System.out.println(pm.getTasks());
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
