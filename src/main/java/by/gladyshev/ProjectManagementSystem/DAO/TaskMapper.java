package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.Project;
import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.TaskRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper implements RowMapper<MyModel> {

    @Override
    public MyModel mapRow(ResultSet resultSet, int i) throws SQLException {
        TaskModel tm = new TaskModel();
        tm.setId(resultSet.getInt("id"));
        tm.setName(resultSet.getString("name"));
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < tm.getName().length(); j++) {
            if(tm.getName().charAt(j)!='|')
            {
                sb.append(tm.getName().charAt(j));
            }
            else
            {
                break;
            }
        }
        try {
            ProjectModel pm = (ProjectModel) Search.search(new Criteria("name", sb.toString()),
                    ProjectRepository.INSTANCE);
            System.out.println("из таск маппера");
            System.out.println(sb.toString());
            System.out.println(pm.getId());
            tm.setPm(pm);
            System.out.println(tm.getPm());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tm;
    }
}
