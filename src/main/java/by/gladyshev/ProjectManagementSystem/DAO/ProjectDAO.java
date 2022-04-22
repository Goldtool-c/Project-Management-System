package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectDAO {
    private final JdbcTemplate jdbcTemplate;
    private static int ID;
    @Autowired
    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public ProjectModel show(int id)
    {
        return jdbcTemplate.query("SELECT * FROM project WHERE id =?", new Object[]{id}, new ProjectMapper())
                .stream().findAny().orElse(null);
    }
    public void delete(int id)
    {
        jdbcTemplate.update("DELETE * FROM project WHERE id =?", id);
    }
    public void save(ProjectModel pm)
    {
        ID++;
        jdbcTemplate.update("INSERT INTO person values(?, ?)", ID, pm.getName());
    }
    public void update(ProjectModel pm)
    {
        jdbcTemplate.update("UPDATE project SET id=?, name=?", pm.getId(), pm.getName());
    }
    public List<ProjectModel> index() {
        return jdbcTemplate.query("SELECT * FROM project", new ProjectMapper());
    }
}
