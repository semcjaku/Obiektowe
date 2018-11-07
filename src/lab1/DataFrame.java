package lab1;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
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
                        DateTimeValue dtv = new DateTimeValue(new Date());
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
        for(int c=0; c<rowOfIndex.columns.size(); c++)
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

    public void addRow(DataFrame newRow)
    {
        if(newRow.columns.size()!=columns.size())
            throw new RuntimeException("Added row has different size than the data frame.");
        for(int i=0;i<columns.size();i++)
        {
            columns.get(i).col.add(newRow.columns.get(i).col.get(0));
        }
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
                groupingResult.get(columns.get(indexOfBy).col.get(i)).addRow(this.Iloc(i));
            }
        }
        return new GroupWrapper(groupingResult,indexOfBy);

        //different, unworking version
        /*int[] indicesOfBy = new int[colnames.length];
        for(int i=0;i<indicesOfBy.length;i++)
            indicesOfBy[i]=-1;

        for(int i=0;i<indicesOfBy.length;i++)
        {
            for (Column c:columns)
                if (c.columnName.equals(colnames[i]))
                {
                    indicesOfBy[i] = columns.indexOf(c);
                    break;
                }
        }
        for(int id : indicesOfBy)
            if(id==-1)
                throw new RuntimeException("No such column");*/
    }

    /*public GroupWrapper groupby(String[] colnames)
    {
        GroupWrapper result = this.groupby(colnames[0]);
        DataFrame tmpDf = null;
        for(int i=1;i<colnames.length;i++)
        {
            for (HashMap.Entry<Value, DataFrame> entry : result.group.entrySet())
            {
                entry.getValue().groupby(colnames[i]);
            }
        }
        return result;
    }*/

    public void DisplayRow(int index)
    {
        DataFrame tmp = this.Iloc(index);
        for(int i=0;i<tmp.columns.size();i++)
            System.out.print(tmp.columns.get(i).col.get(0).toString()+' ');
        System.out.println();
    }

    public void Display()
    {
        for(Column c:columns)
            System.out.print(c.columnName+' ');
        System.out.println();
        for(int i=0;i<this.Size();i++)
            this.DisplayRow(i);
    }

    public static void main(String[] argv)
    {
        Class<? extends Value>[] types = (Class<? extends Value>[]) new Class<?>[4];
        types[0] = StringValue.class;
        types[1] = DateTimeValue.class;
        types[2] = DoubleValue.class;
        types[3] = DoubleValue.class;
        DataFrame test = new DataFrame("/C:/Temp/groupby.csv", types);
        //test.groupby("id").max().Display();
        //test.groupby("id").min().Display();
        //test.groupby("id").mean().Display();
        //test.groupby("id").std().Display();
        //test.groupby("id").var().Display();
        //test.groupby("id").sum().Display();
    }

}