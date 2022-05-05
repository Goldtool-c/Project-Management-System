package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.Criteria;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import by.gladyshev.projectmanagementsystem.repository.Search;
import by.gladyshev.projectmanagementsystem.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper implements RowMapper<MyModel> {

    @Override
    public MyModel mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet.getString("flag").equals("true")) {
            TaskModel tm = new TaskModel();
            tm.setId(resultSet.getInt("id"));
            tm.setName(resultSet.getString("name"));
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < tm.getName().length(); j++) {
                if (tm.getName().charAt(j) != '|') {
                    sb.append(tm.getName().charAt(j));
                } else {
                    break;
                }
            }
            try {
                ProjectModel pm = (ProjectModel) Search.search(new Criteria("name", sb.toString()),
                        ProjectRepository.INSTANCE);
                tm.setPm(pm);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (UserRepository.INSTANCE.Size() != 0 && resultSet.getInt("developer") != 0) {
                UserModel um = null;
                try {
                    um = (UserModel) Search.search(new Criteria("id", resultSet.getInt("developer")), UserRepository.INSTANCE);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                tm.setResponsible(um);
                assert um != null;
                if (!isAssigned(um, tm)) {
                    um.assignTask(tm);
                }
            }
            return tm;
        } else
        {
            return null;
        }
    }
    private boolean isAssigned(UserModel um, TaskModel tm)
    {
        for (int i = 0; i < um.getTasks().size(); i++) {
            if(um.getTasks().get(i).getId()==tm.getId())
            {
                return true;
            }
        }
        return false;
    }
}
