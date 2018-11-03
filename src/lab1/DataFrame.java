package lab1;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataFrame
{

    ArrayList<Column> columns;
    public DataFrame()
    {
        columns = new ArrayList<>();
    }
    
    public DataFrame(String[] colnames, Class<? extends Value>[] coltypes)
    {
        columns = new ArrayList<>();
        for(int i=0; i<colnames.length;i++)
            columns.add(new Column(colnames[i],coltypes[i]));
    }

    public DataFrame(String filename, Class<? extends Value>[] coltypes)
    {
        this(filename, coltypes, true);
    }

    public DataFrame(String filename, Class<? extends Value>[] coltypes, boolean header)
    {
        columns = new ArrayList<>();
        BufferedReader br = null;
        try
        {
            FileInputStream fstream = new FileInputStream(filename);
            br = new BufferedReader(new InputStreamReader(fstream));
            String line, name;
            String[] colnames, row;

            if((line = br.readLine()) != null && header)
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

            while((line = br.readLine()) != null)
            {
                row = line.split(",");
                for(int i=0; i<row.length;i++)
                {
                    Class clazz = coltypes[i];
                    if( IntegerValue.class == clazz)
                    {
                        columns.get(i).col.add(new IntegerValue(Integer.parseInt( row[i] )));
                        continue;
                    }
                    if( FloatValue.class == clazz)
                    {
                        columns.get(i).col.add(new FloatValue(Float.parseFloat( row[i] )));
                        continue;
                    }
                    if( DoubleValue.class == clazz)
                    {
                        columns.get(i).col.add(new DoubleValue(Double.parseDouble( row[i] )));
                        continue;
                    }
                    if( StringValue.class == clazz)
                    {
                        columns.get(i).col.add(new StringValue(row[i]));
                        continue;
                    }
                    if( DateTimeValue.class == clazz)
                    {
                        DateTimeValue dtv = null;
                        columns.get(i).col.add(dtv.create(row[i]));
                    }

                }
            }
        } catch(FileNotFoundException e) {
            System.err.println("Caught FileNotFoundException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch(IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != br)
            {
                try{ br.close(); }
                catch (IOException e) {System.err.println("Caught IOException while closing reader: " + e.getMessage());e.printStackTrace();}
            }
        }
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
        throw (new IllegalArgumentException("No such column"));
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
        Class<? extends Value>[] coltypes = (Class<? extends Value>[]) new Class<?>[columns.size()];
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
        Class<? extends Value>[] coltypes = (Class<? extends Value>[]) new Class<?>[columns.size()];
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

    public GroupWrapper groupby(String colname)
    {
        HashMap<Value, DataFrame> groupingResult = new HashMap<Value, DataFrame>();
        int indexOfBy = -1;
        for (Column c:columns)
            if (c.columnName.equals(colname))
            {
                indexOfBy = columns.indexOf(c);
                break;
            }
        if(indexOfBy==-1)
            throw new RuntimeException("No such column");

        for(int i=0; i<this.Size();i++)
        {
            if (!groupingResult.containsKey(columns.get(indexOfBy).col.get(i)))
            {
                groupingResult.put(columns.get(indexOfBy).col.get(i), this.Iloc(i));
            }
            else
            {
                groupingResult.get(columns.get(indexOfBy).col.get(i)).columns.addAll(this.Iloc(i).columns);
            }
        }
        return new GroupWrapper(groupingResult);
    }

    public static void main(String[] argv)
    {
        Class<? extends Value>[] types = (Class<? extends Value>[]) new Class<?>[3];
        for(int i=0;i<types.length;i++)
            types[i]=DoubleValue.class;
        DataFrame test = new DataFrame("/C:/Temp/data.csv", types);
        System.out.println(test.columns.get(0).columnName);
        System.out.println(test.columns.get(1).columnType);
        System.out.println(test.columns.get(1).col.size());
        //System.out.println(test.Iloc(0,3).columns.get(2).col.get(1).Get());
        //System.out.println(test.Get("x").col.get(15).Get());
        System.out.println(test.Size());

        DataFrame test2 = new DataFrame("/C:/Temp/sparse.csv", types);
        SparseDataFrame sdf = new SparseDataFrame(test2,new DoubleValue(0.0)); //Nullptr exception
        System.out.println(sdf.columns.get(0).columnName);
        System.out.println(sdf.columns.get(1).columnType);
        System.out.println(sdf.columns.get(0).col.size());
        System.out.println(sdf.columns.get(0).col.get(0).Get());
        System.out.println(sdf.Size());

        DataFrame test3 = sdf.ToDense();
        System.out.println(test3.columns.get(0).columnName);
        System.out.println(test3.columns.get(1).columnType);
        System.out.println(test3.columns.get(1).col.size());
        System.out.println(test3.Size());

        Value testValue = new IntegerValue(5);
        System.out.println(testValue.add(new IntegerValue(1)).Get());
        System.out.println(testValue.sub(new IntegerValue(1)).Get());
        System.out.println(testValue.mul(new IntegerValue(2)).Get());
        System.out.println(testValue.div(new IntegerValue(3)).Get());
        System.out.println(testValue.pow(new IntegerValue(2)).Get());

        System.out.println(testValue.lt(new IntegerValue(1)));
        System.out.println(testValue.lte(new IntegerValue(1)));
        System.out.println(testValue.gt(new IntegerValue(1)));
        System.out.println(testValue.gte(new IntegerValue(1)));
        System.out.println(testValue.eq(new IntegerValue(1)));
        System.out.println(testValue.neq(new IntegerValue(1)));
    }

}