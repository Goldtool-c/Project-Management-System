package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;

public class DAO {
    protected final JdbcTemplate jdbcTemplate;
    protected static int ID;
    protected String table;
    protected RowMapper<Model> rm;
    @Autowired
    public DAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Model show(int id)
    {
        return jdbcTemplate.query("SELECT * FROM "+table+" WHERE id =?", new Object[]{id}, rm)
                .stream().findAny().orElse(null);
    }
    public void delete(int id)
    {
        jdbcTemplate.update("DELETE FROM "+table+" WHERE id =?", id);
    }
    public void save(ProjectModel pm)
    {
        ID++;
        Field[] fields = pm.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        try {
            jdbcTemplate.update("INSERT INTO "+table+" values(?, ?)", ID, fields[1].get(pm));//[1] bcs name is 2nd field
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //todo fix this crutch
    }
    public void update(ProjectModel pm)
    {
        jdbcTemplate.update("UPDATE "+table+" SET name=? WHERE id=?", pm.getName(), pm.getId());
    }
    public List index() {
        return jdbcTemplate.query("SELECT * FROM "+table, rm);
    }
}
