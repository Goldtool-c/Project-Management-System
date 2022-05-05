package by.gladyshev.projectmanagementsystem.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class TaskModelTest {
    private ProjectModel pm = Mockito.mock(ProjectModel.class);
    @Test
    void getShortName() {
        TaskModel tm = new TaskModel();
        tm.setName("denis|task14");
        Assert.assertEquals("task14", tm.getShortName());
    }

    @Test
    void setByShortName() {
        TaskModel tm = new TaskModel();
        tm.setPm(pm);
        Mockito.when(pm.getName()).thenReturn("denis");
        tm.setByShortName("task1");
        Assert.assertEquals("denis|task1", tm.getName());
    }
}