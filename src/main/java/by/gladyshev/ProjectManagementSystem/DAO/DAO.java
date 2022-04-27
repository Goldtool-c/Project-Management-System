package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.repository.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;

public class DAO {
    protected final JdbcTemplate jdbcTemplate;
    protected int ID;
    protected String table;
    protected RowMapper<Model> rm;
    protected Storage repository;
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
    public void save(Model pm)
    {
        ID++;
        Field[] fields = pm.getClass().getDeclaredFields();
        Object[] values = new Object[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                values[i] = fields[i].get(pm);
            }
            jdbcTemplate.update("INSERT INTO "+table+" values(?, ?)", ID, values[1]);//[1] -- name
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("log: saving "+pm+" to repository");
        pm.setId(ID);
        repository.add(pm);
        System.out.println("success");
    }
    public void update(Model pm)
    {
        jdbcTemplate.update("UPDATE "+table+" SET name=? WHERE id=?", pm.getName(), pm.getId());
    }
    public List index() {
        return jdbcTemplate.query("SELECT * FROM "+table, rm);
    }
    public int getID() {
        return ID;
    }
}
