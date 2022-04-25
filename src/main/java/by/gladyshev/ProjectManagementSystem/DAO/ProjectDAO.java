package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class ProjectDAO extends DAO{
    @Autowired
    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "project";
        rm = new ProjectMapper();
        ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        ProjectRepository.INSTANCE.setAll((List<Model>) this.index());
    }
}
