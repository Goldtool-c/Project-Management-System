package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import by.gladyshev.projectmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class UserDAO extends DAO {
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "userDB";
        rm = new UserMapper();
        ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        repository = UserRepository.INSTANCE;
        UserRepository.INSTANCE.setAll((List<MyModel>) this.index("id"));
    }
    @Override
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
            jdbcTemplate.update("INSERT INTO "+table+" values(?, ?, ?, ?)", ID, values[1], values[2], "developer");//[1] -- name
            jdbcTemplate.update("UPDATE "+table+" set flag='true' where id=?", ID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pm.setId(ID);
        repository.add(pm);
    }
    @Override
    public void delete(int id)
    {
        MyModel temp = null;
        for (int i = 0; i < repository.Size(); i++) {
            temp = repository.get(i);
            if(temp.getId()==id)
            {
                repository.delete(i);
                break;
            }
        }
        ProjectModel tempProject;
        UserModel tempUser;
        for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
            tempProject = (ProjectModel) ProjectRepository.INSTANCE.get(i);
            for (int j = 0; j < tempProject.getDevelopers().size(); j++) {
                tempUser = tempProject.getDevelopers().get(j);
                if(temp.equals(tempUser))
                {
                    tempProject.getDevelopers().remove(j);
                    System.out.println("User "+temp+" deleted from "+tempProject);
                    updateProjectDB(i);
                    break;
                }
            }
        }
        jdbcTemplate.update("UPDATE "+table+" set flag = 'false' WHERE id =?", id);
    }
    private void updateProjectDB(int i)
    {
        ProjectModel pm = (ProjectModel) ProjectRepository.INSTANCE.get(i);
        String users = parseName(pm.getUserNames());
        if(!users.equals("")) {
            jdbcTemplate.update("UPDATE project SET name=?, developers=? WHERE id=?", pm.getName(), users, pm.getId());
        } else {
            jdbcTemplate.update("UPDATE project SET name=?, developers=null WHERE id=?", pm.getName(), pm.getId());
        }

    }
    private String parseName(String[] names)
    {
        StringBuilder sb = new StringBuilder();
        if(names.length!=0) {
            sb.append(names[0]);
            for (int i = 1; i < names.length; i++) {
                sb.append(",");
                sb.append(names[i]);
            }
            return sb.toString();
        }
        return "";
    }
}
