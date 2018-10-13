package lab1;
import java.util.ArrayList;

public class Column implements Cloneable
{
    String columnName;
    String columnType;
    ArrayList<Class<?>> col;
    public Column(String name, String type)
    {
        //Class<?> class_def = null;

        columnName = name;
        columnType = type;
        /*try{
            class_def = Class.forName(type);
        } catch (Exception e) {
            System.out.println("Invalid type.");
        }*/

        col = new ArrayList<>();
    }

    public void ColumnToCOOLColumn(Object hide)
    {
        ArrayList COOLcol = new ArrayList<COOLValue>();
        int j=0, originalSize = col.size(), current;
        while(j<originalSize)
        {
            current = col.indexOf(hide);
            if(current != 0)
                COOLcol.add(new COOLValue(j,col.get(0)));
            col.remove(0);
            j++;
        }
        col = (ArrayList<Class<?>>) COOLcol.clone();
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