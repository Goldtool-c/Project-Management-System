package by.gladyshev.projectmanagementsystem.repository;

import by.gladyshev.projectmanagementsystem.model.MyModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Search {
    private Search(){}
    public static MyModel search(Criteria criteria, Storage repository) throws IllegalAccessException {
        MyModel temp;
        for (int i = 0; i < repository.Size(); i++) {
            temp = repository.get(i);
            Field[] fields = temp.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals(criteria.getProperty()))
                {
                    if(criteria.getValue().equals(field.get(temp)))
                    {
                        return temp;
                    }
                }
            }
        }
        return null; //todo throw notFindException
    }
    public static List<MyModel> searchList(Criteria criteria, Storage repository) throws IllegalAccessException {
        MyModel temp;
        List<MyModel> res = new ArrayList<>();
        for (int i = 0; i < repository.Size(); i++) {
            temp = repository.get(i);
            Field[] fields = temp.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals(criteria.getProperty()))
                {
                    if(criteria.getValue().equals(field.get(temp)))
                    {
                        res.add(temp);
                    }
                }
            }
        }
        if (res.size()!=0)
        { return res;}
        else {
            return null; //todo throw notFindException
        }
    }
}
