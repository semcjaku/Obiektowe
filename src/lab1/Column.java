package lab1;
import java.util.ArrayList;
import java.util.List;

public class Column
{
    String columnName;
    String columnType;
    List<Class<?>> col;
    public Column(String name, String type)
    {
        Class<?> class_def = null;

        columnName = name;
        columnType = type;
        try{
            class_def = Class.forName(type);
        } catch (Exception e) {
            System.out.println("Invalid type.");
        }

        col = new ArrayList<>();
    }
}