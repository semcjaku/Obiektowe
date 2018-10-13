package lab1;
import java.util.ArrayList;
import java.util.List;

public class Column implements Cloneable
{
    String columnName;
    String columnType;
    ArrayList<Class<?>> col;
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

    @Override
    protected Column clone() {
    try {
        return (Column) super.clone();
    } catch (CloneNotSupportedException e) {
        System.out.println(this.getClass().getName() + " nie implementuje Cloneable...");
        return null;
    }
}
}