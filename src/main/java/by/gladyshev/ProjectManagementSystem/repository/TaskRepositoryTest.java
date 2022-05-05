package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class TaskRepositoryTest {
    /*@Mock
    MyModel taskModelI;
    @InjectMocks
    TaskModel taskModel = new TaskModel();
    @Mock
    Storage taskRepositoryI;
    @InjectMocks
    Storage repository = TaskRepository.INSTANCE;*/
    @Test
    void add() {
        /*when(repository.get(1)).thenReturn(taskModel);
        when(taskModel.getResponsible()).thenReturn(new UserModel(0, "test"));
        Assert.assertEquals(taskModel.getResponsible().getId(), 0);*/
    }
}