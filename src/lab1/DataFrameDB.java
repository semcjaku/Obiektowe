package lab1;

public class DataFrameDB extends DataFrame
{
    public DataFrameDB(){super();}

    public static DataFrame SelectToDF(String selectQuery) //UNFINISHED!!!
    {
        DataFrame result = new DataFrame();
        DBClient dbc = new DBClient();
        dbc.query = selectQuery;
        String queryResult = dbc.Select();

        return result;
    }

    public DBClient DFtoDB()
    {
        DBClient dbc = new DBClient();
        String[] values = new String[this.Size()];
        for(int i=0;i<this.Size();i++)
            values[i]=this.RowAsString(i,"sql");
        dbc.Insert(values);
        return dbc;
    }
}
