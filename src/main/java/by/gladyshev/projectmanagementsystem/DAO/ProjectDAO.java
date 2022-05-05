package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
        ProjectRepository.INSTANCE.setAll((List<MyModel>) this.index("id"));
    }
    @Override
    public void update(MyModel pm)
    {
        if (((ProjectModel) pm).getDevelopers().size()!=0) {
            for (int i = 0; i < repository.Size(); i++) {
                if(repository.get(i).getId()==pm.getId())
                {
                    repository.get(i).setName(pm.getName());
                }
            }
            String users = parseName(((ProjectModel) pm).getUserNames());
            if(((ProjectModel) pm).getTasks().size()!=0) {
                String tasks = parseName(((ProjectModel) pm).getTaskNames());
                jdbcTemplate.update("UPDATE " + table + " SET name=?, developers=?, tasks=? WHERE id=?",
                        pm.getName(), users, tasks, pm.getId());
            } else
            {
                jdbcTemplate.update("UPDATE " + table + " SET name=?, developers=?, tasks=null WHERE id=?",
                        pm.getName(), users, pm.getId());
            }
        } else
        {
            for (int i = 0; i < repository.Size(); i++) {
                if(repository.get(i).getId()==pm.getId())
                {
                    repository.get(i).setName(pm.getName());
                }
            }
            if(((ProjectModel) pm).getTasks().size()!=0) {
                String tasks = parseName(((ProjectModel) pm).getTaskNames());
                jdbcTemplate.update("UPDATE " + table + " SET name=?, tasks=? WHERE id=?",
                        pm.getName(), tasks, pm.getId());
            } else
            {
                jdbcTemplate.update("UPDATE " + table + " SET name=?, tasks=null WHERE id=?",
                        pm.getName(), pm.getId());
            }
        }
    }
    private String parseName(String[] names)//format "name1,name2,name3,...,nameN"
    {
        StringBuilder sb = new StringBuilder();
        if(names.length!=0) {
            sb.append(names[0]);
            for (int i = 1; i < names.length; i++) {
                sb.append(",");
                sb.append(names[i]);
            }
        }
        return sb.toString();
    }

}
