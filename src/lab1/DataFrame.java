package lab1;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class DataFrame
{

    ArrayList<Column> columns;
    public DataFrame()
    {
    	columns.clear();
    	columns.add(new Column("kol1","COOLValue"));
    }
    
    public DataFrame(String[] colnames, String[] coltypes)
    {
        columns.clear();
        for(int i=0; i<colnames.length;i++)
            columns.add(new Column(colnames[i],coltypes[i]));
    }

    public DataFrame(String filename, String[] coltypes)
    {
        this(filename, coltypes, true);
    }

    public DataFrame(String filename, String[] coltypes, boolean header)
    {
        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String line, name;
        String[] colnames, row;

        if(line = br.readLine() != null && header)
        {
            colnames = line.split(",");
            for(int i=0; i<colnames.length;i++)
                columns.add(new Column(colnames[i],coltypes[i]));
        }
        else if(!header)
        {
            Scanner keyboard = new Scanner(System.in);
            for(int i=0; i<coltypes.length;i++)
            {
                System.out.println("Enter the name of column number" + (i+1));
                name = keyboard.nextLine();
                columns.add(new Column(name,coltypes[i]));
            }
        }

        while(line = br.readLine() != null)
        {
            row = line.split(",");
            for(int i=0; i<row.length;i++)
            {
                Class clazz = Class.forName(coltypes[i]);
                Object x = clazz.newInstance();
                //tu trzeba zrzutować stringa row[i] na ten obiekt x sprawdzając typ (tylko prymitywne) switchem albo po prostu wciepać same stringi(tylko jaki to ma wtedy sens)
                columns.get(i).col.add(x);
            }
        }

        br.close();
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

    /*public DataFrame Get(String[] cols, boolean deepCopy)
    {
        DataFrame partialFrame = null;
        for(int i=0;i<cols.length;i++)
        {
            for(int j=0;j<columns.size();j++)
            {
                if(cols[i].equals(columns.get(j).columnName))
                {
                    if(!deepCopy)
                    {
                        partialFrame.columns.add(columns.get(j).clone());
                    }
                    else
                    {
                        partialFrame.columns.add(new Column(columns.get(j).columnName,columns.get(j).columnType));
                        partialFrame.columns.get(partialFrame.columns.size()-1).col = columns.get(j).col.clone();
                    }
                }
            }
        }
        return partialFrame;
    }*/

    public DataFrame Iloc(int i)
    {
        String[] colnames = new String[columns.size()];
        String[] coltypes = new String[columns.size()];
        for(int x=0; x<columns.size(); x++)
        {
            colnames[x] = columns.get(x).columnName;
            coltypes[x] = columns.get(x).columnType;
        }
        DataFrame rowOfIndex = new DataFrame(colnames, coltypes);
        for(int c=0; c<columns.size(); c++)
        {
            rowOfIndex.columns.get(c).col.add(columns.get(c).col.get(i));
        }
        return rowOfIndex;
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
        DataFrame rowsOfIndex = new DataFrame(colnames, coltypes);
        for(int i=from; i<to+1; i++)
        {
            for(int c=0; c<columns.size(); c++)
            {
                rowsOfIndex.columns.get(c).col.add(columns.get(c).col.get(i));
            }
        }
        return rowsOfIndex;
    }

    public static void main(String[] argv)
    {

    }
}