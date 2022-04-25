package by.gladyshev.ProjectManagementSystem.repository;

import by.gladyshev.ProjectManagementSystem.model.Model;

import java.lang.reflect.Field;

public class Search {
    private Search(){}
    public static Model search(Criteria criteria, Storage repository) throws IllegalAccessException {
        Model temp;
        for (int i = 0; i < repository.Size(); i++) {
            temp = repository.get(i);
            Field[] fields = temp.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals(criteria.getProperty()))
                {
                    if(criteria.getValue().equals(field.get(temp).toString()))
                    {
                        return temp;
                    }
                }
            }
        }
        return null; //todo throw notFindException
    }
}
