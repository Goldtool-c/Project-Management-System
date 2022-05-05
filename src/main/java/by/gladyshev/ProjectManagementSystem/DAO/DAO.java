package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.repository.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;

public class DAO {
    protected final JdbcTemplate jdbcTemplate;
    protected int ID;
    protected String table;
    protected RowMapper<MyModel> rm;
    protected Storage repository;
    @Autowired
    public DAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public MyModel show(int id)
    {
        return jdbcTemplate.query("SELECT * FROM "+table+" WHERE id =?", new Object[]{id}, rm)
                .stream().findAny().orElse(null);
    }
    public void delete(int id)
    {
        MyModel temp;
        for (int i = 0; i < repository.Size(); i++) {
            temp = repository.get(i);
            if(temp.getId()==id)
            {
                repository.delete(i);
                break;
            }
        }
        jdbcTemplate.update("UPDATE "+table+" set flag = 'false' WHERE id =?", id);
    }
    public void save(MyModel pm)
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
            jdbcTemplate.update("UPDATE "+table+" set flag='true' where id=?", ID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pm.setId(ID);
        repository.add(pm);
    }
    public void update(MyModel pm)
    {
        jdbcTemplate.update("UPDATE "+table+" SET name=? WHERE id=?", pm.getName(), pm.getId());
    }
    public List index(String sortType)
    {
        List list = jdbcTemplate.query("SELECT * FROM "+table+" order by "+parseSort(sortType), rm);
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i)==null)
            {
                list.remove(i);
                i--;
            }
        }
        return list;
    }
    public int getID() {
        return ID;
    }
    protected String parseSort(String sortType)
    {
        switch (sortType)
        {
            case "reverse id":
            {
                sortType = "id desc";
                break;
            }
            case "reverse name":
            {
                sortType = "name desc";
                break;
            }
            default:
            {
                //todo exception/log
                break;
            }
        }
        return sortType;
    }
}
