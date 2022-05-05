package by.gladyshev.projectmanagementsystem.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskModelTest {

    @Test
    void getShortName() {
        TaskModel tm = new TaskModel();
        tm.setName("denis|task14");
        Assert.assertEquals("task14", tm.getShortName());
    }
}