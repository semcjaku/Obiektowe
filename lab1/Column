package lab1;


public class Column
{
    String columnName;
    String columnType;
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

        List<class_def> col = new ArrayList<>();
    }
}