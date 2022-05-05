package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskDAO extends DAO {
    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "tasks";
        rm = new TaskMapper();
        if(jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class)!=null)
        {
            ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        } else
        {
            ID=0;
        }
        repository = TaskRepository.INSTANCE;
        TaskRepository.INSTANCE.setAll(index("id"));
    }
    @Override
    public void update(MyModel tm)
    {
        if(((TaskModel)tm).getResponsible()!=null) {
            jdbcTemplate.update("UPDATE " + table + " SET name=?, developer=? WHERE id=?",
                    tm.getName(), ((TaskModel) tm).getResponsible().getId(), tm.getId());
        } else
        {
            jdbcTemplate.update("UPDATE " + table + " SET name=? WHERE id=?",
                    tm.getName(), tm.getId());
        }
    }
    public int getID() {
        return this.ID;
    }
}
