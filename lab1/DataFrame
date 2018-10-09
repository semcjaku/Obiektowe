package lab1;
import javaIn.*;

public class DataFrame
{
    ArrayList<Column> columns;
    public DataFrame(String[] colnames, String[] coltypes)
    {
        for(int i=0; i<colnames.length();i++)
            columns.add(new Column(colanmes[i],coltypes[i]));
    }

    public int Size()
    {
        return columns.get(0).col.size();
    }

    public Column Get(String colname)
    {
        for(Column c:columns)
            if(c.columnName==colname)
                return c;
        return new Column("nie ma takiej kolumny","none")
    }

    public static void main(String[] argv)
    {

    }
}