package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDAO extends DAO{
    @Autowired
    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "project";
        rm = new ProjectMapper();
        if(jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class)!=null)
        {
            ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        } else
        {
            ID=0;
        }
        repository = ProjectRepository.INSTANCE;
        ProjectRepository.INSTANCE.setAll((List<Model>) this.index("id"));
    }
    @Override
    public void update(Model pm)
    {
        if (((ProjectModel) pm).getDevelopers().size()!=0) {
            String users = parseName(((ProjectModel) pm).getDevelopers());
            System.out.println(users);
            jdbcTemplate.update("UPDATE " + table + " SET name=?, developers=? WHERE id=?", pm.getName(), users, pm.getId());
        } else
        {
            jdbcTemplate.update("UPDATE " + table + " SET name=? WHERE id=?", pm.getName(), pm.getId());
        }
    }
    private String parseName(List<UserModel> users)
    {
        StringBuilder sb = new StringBuilder();
        System.out.println(users);
        sb.append(users.get(0).getName());
        for (int i = 1; i < users.size(); i++) {
            sb.append(",");
            sb.append(users.get(i).getName());
        }
        return sb.toString();
    }
}
