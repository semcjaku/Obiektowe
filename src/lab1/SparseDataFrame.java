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
			columns.get(i).ColumnToCOOLColumn(hide);
		}
	}

}
