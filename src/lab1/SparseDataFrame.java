package lab1;

import java.util.ArrayList;

public class SparseDataFrame extends DataFrame {
	public Object hide;
	ArrayList<COOLColumn> columns;
	
	public SparseDataFrame(String[] colnames, String coltype, Object hidden)
	{
		for(int i=0; i<colnames.length;i++)
            columns.add(new COOLColumn(colnames[i],coltype));
		hide = hidden;
	}
	
	public SparseDataFrame(DataFrame df)
	{
		for(int i=0;i<df.columns.size(); i++)
		{
			columns.add(new COOLColumn(df.columns.get(i).columnName,df.columns.get(0).columnType));
			//columns.set(i,df.columns.get(i));
			//columns.get(i).ColumnToCOOLColumn(hide);
            Column temp = df.columns.get(i);
            columns.set(i,temp.ColumnToCOOLColumn(hide));
		}
	}

	public DataFrame ToDense()
    {
        String[] colnames = new String[columns.size()], coltypes = new String[columns.size()];

        for(int i=0; i<colnames.length; i++)
        {
            colnames[i] = columns.get(0).columnName;
            coltypes[i] = columns.get(0).columnType;
        }

        DataFrame densedf = new DataFrame(colnames, coltypes);
        int currentIndex;

        for(int i=0; i<columns.size();i++)
        {
            currentIndex = 0;
            for(int j=0; j<columns.get(i).col.size(); j++)
            {
                while(currentIndex < columns.get(i).col.get(j).GetIndex())
                {
                    densedf.columns.get(i).col.add(hide);
                    currentIndex++;
                }
                densedf.columns.get(i).col.add(columns.get(i).col.get(j).GetValue());
            }
        }

        int longest = 0, x;
        for(int i=0; i<densedf.columns.size();i++)
        {
            if(densedf.columns.get(i).col.size() > longest)
                longest = densedf.columns.get(i).col.size();
        }

        for(int i=0; i<densedf.columns.size();i++)
        {
            x = densedf.columns.get(i).col.size();
            if(x < longest)
                for(int j=1; j<=x; j++)
                    densedf.columns.get(i).col.add(hide);
        }

        return densedf;
    }

    public int Size()
    {
        int longest = 0;
        for(int i=0; i<columns.size();i++)
        {
            if(columns.get(i).col.size() > longest)
                longest = columns.get(i).col.size();
        }
        return longest;
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
        DataFrame rowOfIndex = new DataFrame(colnames, coltypes);
        for(int c=0; c<columns.size(); c++)
        {
            if(columns.get(c).col.size() >= i)
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
                if(columns.get(c).col.size() >= i)
                    rowsOfIndex.columns.get(c).col.add(columns.get(c).col.get(i));
            }
        }
        return rowsOfIndex;
    }

}
