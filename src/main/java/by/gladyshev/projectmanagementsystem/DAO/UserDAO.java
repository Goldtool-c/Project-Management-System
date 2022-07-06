package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class UserDAO extends DAO {
    public UserDAO(@Autowired JdbcTemplate jdbcTemplate, @Autowired UserMapper userMapper) {
        super(jdbcTemplate);
        table = "userDB";
        rm = userMapper;
        ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class);
        size = jdbcTemplate.queryForObject("SELECT count (*) FROM "+ table, Integer.class);
    }

    @Override
    public void save(MyModel pm) {
        ID++;
        size++;
        Field[] fields = pm.getClass().getDeclaredFields();
        Object[] values = new Object[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                values[i] = fields[i].get(pm);
            }
            jdbcTemplate.update("INSERT INTO " + table + " values(?, ?, ?, ?)", ID, values[1], values[2], "developer");//[1] -- name
            jdbcTemplate.update("UPDATE " + table + " set flag='true' where id=?", ID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pm.setId(ID);
    }

    @Override
    public void delete(int id) {
        MyModel temp = null;
        ProjectModel tempProject;
        UserModel tempUser;
        for (int i = 0; i < getIDProject(); i++) {
            tempProject = (ProjectModel) showProject(i);
            if(tempProject!=null) {
                for (int j = 0; j < tempProject.getDevelopers().size(); j++) {
                    tempUser = tempProject.getDevelopers().get(j);
                    if (temp.equals(tempUser)) {
                        tempProject.getDevelopers().remove(j);
                        System.out.println("User " + temp + " deleted from " + tempProject);
                        updateProjectDB(tempProject);
                        break;
                    }
                }
            }
        }
        jdbcTemplate.update("UPDATE " + table + " set flag = 'false' WHERE id =?", id);
    }


    private void updateProjectDB(ProjectModel pm) {
        String users = parseName(pm.getUserNames());
        if (!users.equals("")) {
            jdbcTemplate.update("UPDATE project SET name=?, developers=? WHERE id=?", pm.getName(), users, pm.getId());
        } else {
            jdbcTemplate.update("UPDATE project SET name=?, developers=null WHERE id=?", pm.getName(), pm.getId());
        }

    }

    private String parseName(String[] names) {
        StringBuilder sb = new StringBuilder();
        if (names.length != 0) {
            sb.append(names[0]);
            for (int i = 1; i < names.length; i++) {
                sb.append(",");
                sb.append(names[i]);
            }
            return sb.toString();
        }
        return "";
    }
    private MyModel showProject(int id) {
        return jdbcTemplate.query("SELECT * FROM project WHERE id =?", new Object[]{id}, rm)
                .stream().findAny().orElse(null);
    }
    private int getIDProject()
    {
        if (jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class) != null) {
            return jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class);
        } else {
            return  0;
        }
    }
}
