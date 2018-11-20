package lab1;
import java.io.*;
import java.util.*;

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
                for(int i=0; i<coltypes.length;i++)
                {
                    columns.add(new Column("Col"+(i+1),coltypes[i]));
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

    boolean CustomContainsKey(HashMap<Value[], DataFrame> container, Value[] key)
    {
        int equalityRate;
        for (HashMap.Entry<Value[], DataFrame> entry : container.entrySet())
        {
            equalityRate=0;
            for(int i=0;i<key.length;i++)
            {
                if(entry.getKey()[i].eq(key[i]))
                    equalityRate++;
            }
            if(equalityRate==key.length)
                return true;
        }
        return false;
    }

    public GroupWrapper groupby(String[] colnames)
    {
        HashMap<Value[], DataFrame> groupingResult = new HashMap<>();
        int[] indicesOfBy = new int[colnames.length];
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
        {
            if(id==-1)
                throw new RuntimeException("No such column");
        }

        for(int i=0; i<this.Size();i++)
        {
            Value currentTuple[] = new Value[colnames.length];
            for(int j=0;j<currentTuple.length;j++)
            {
                currentTuple[j] = columns.get(indicesOfBy[j]).col.get(i);
            }
            if (!this.CustomContainsKey(groupingResult,currentTuple))
            {
                groupingResult.put(currentTuple, this.Iloc(i));
            }
            else
            {
                int equalityRate;
                Value[] tmp = currentTuple;
                for (HashMap.Entry<Value[], DataFrame> entry : groupingResult.entrySet())
                {
                    equalityRate=0;
                    for(int x=0;x<currentTuple.length;x++)
                    {
                        if(entry.getKey()[x].eq(currentTuple[x]))
                            equalityRate++;
                    }
                    if(equalityRate==currentTuple.length)
                        tmp = entry.getKey();
                }
                groupingResult.get(tmp).addRow(this.Iloc(i));
            }
        }
        return new GroupWrapper(groupingResult,indicesOfBy);
    }

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

    public String asString()
    {
        StringBuilder output = new StringBuilder();
        for(Column c:columns)
            output.append(c.columnName+' ');
        output.append("\n\r");
        for(int i=0;i<this.Size();i++)
        {
            for(int j=0;j<columns.size();j++)
            {
                output.append(columns.get(j).col.get(i).toString()+' ');
            }
            output.append("\n\r");
        }
        return output.toString();
    }

    /*public static void main(String[] argv)
    {
        Class<? extends Value>[] types = (Class<? extends Value>[]) new Class<?>[4];
        types[0] = StringValue.class;
        types[1] = DateTimeValue.class;
        types[2] = DoubleValue.class;
        types[3] = DoubleValue.class;
        //DataFrame test = new DataFrame("/C:/Temp/groupby.csv", types);
        //test.groupby(new String[]{"id"}).max().Display();
        //test.groupby(new String[]{"id"}).min().Display();
        //test.groupby(new String[]{"id"}).mean().Display();
        //test.groupby(new String[]{"id"}).std().Display();
        //test.groupby(new String[]{"id"}).var().Display();
        //test.groupby(new String[]{"id"}).sum().Display();

        DataFrame test2 = new DataFrame("/C:/Temp/groubymulti.csv", types);
        //test2.groupby(new String[]{"id","date"}).max().Display();
        //test2.groupby(new String[]{"id","date"}).min().Display();
        //test2.groupby(new String[]{"id","date"}).mean().Display();
        //test2.groupby(new String[]{"id","date"}).std().Display();
        //test2.groupby(new String[]{"id","date"}).var().Display();
        //test2.groupby(new String[]{"id","date"}).sum().Display();
    }*/
}