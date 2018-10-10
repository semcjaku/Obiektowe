package lab1;
import java.io.*;
import java.util.ArrayList;

public class DataFrame
{
    ArrayList<Column> columns;
    public DataFrame(String[] colnames, String[] coltypes)
    {
        columns.clear();
        for(int i=0; i<colnames.length;i++)
            columns.add(new Column(colnames[i],coltypes[i]));
    }

    public int Size()
    {
        return columns.get(0).col.size();
    }

    public Column Get(String colname)
    {
        for(Column c:columns)
            if(c.columnName.equals(colname))
                return c;
        return new Column("nie ma takiej kolumny","none");
    }

    public DataFrame Iloc(int i)
    {
        String[] colnames = new String[columns.size()];
        String[] coltypes = new String[columns.size()];
        for(int x=0; x<columns.size(); x++)
        {
            colnames[x] = columns.get(x).columnName;
            coltypes[x] = columns.get(x).columnType;
        }
        DataFrame columnOfIndex = new DataFrame(colnames, coltypes);
        for(int c=0; c<columns.size(); c++)
        {
            columnOfIndex.columns.get(c).col.add(columns.get(c).col.get(i));
        }
        return columnOfIndex;
    }

    public DataFrame Iloc(int from, int to)
    {
        String[] colnames = new String[columns.size()];
        String[] coltypes = new String[columns.size()];
        for(int x=0; x<columns.size(); x++)
        {
            colnames[x] = columns.get(x).columnName;
            coltypes[x] = columns.get(x).columnType;
        }
        DataFrame columnOfIndex = new DataFrame(colnames, coltypes);
        for(int i=from; i<to+1; i++)
        {
            for(int c=0; c<columns.size(); c++)
            {
                columnOfIndex.columns.get(c).col.add(columns.get(c).col.get(i));
            }
        }
        return columnOfIndex;
    }

    public static void main(String[] argv)
    {

    }
}