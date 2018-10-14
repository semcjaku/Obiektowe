package lab1;

public class SparseDataFrame extends DataFrame {
	public Object hide;
	
	public SparseDataFrame(String[] colnames, String coltype, Object hidden)
	{
		for(int i=0; i<colnames.length;i++)
            columns.add(new Column(colnames[i],coltype));
		hide = hidden;
	}
	
	public SparseDataFrame(DataFrame df)
	{
		for(int i=0;i<df.columns.size(); i++)
		{
			columns.add(new Column(df.columns.get(i).columnName,df.columns.get(0).columnType));
			columns.set(i,df.columns.get(i));
			columns.get(i).ColumnToCOOLColumn(hide);
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

}
