package lab1;

import java.util.HashMap;

public class GroupWrapper implements Groupby
{
    public HashMap<Value,DataFrame> group;
    public int indexOfGroupingColumn;

    public GroupWrapper(HashMap<Value,DataFrame> groupbyResult, int idx)
    {
        group = new HashMap<Value,DataFrame>();
        group = groupbyResult;
        indexOfGroupingColumn=idx;
    }

    public DataFrame DfCreator()
    {
        DataFrame result = new DataFrame();
        //poniższy fragment kodu wyciąga z hashmapy jeden rekord jako Entry
        HashMap.Entry<Value, DataFrame> nameAndTypeGetter = null;
        for(HashMap.Entry<Value, DataFrame> entryGetter: group.entrySet()) {
            nameAndTypeGetter = entryGetter;
            break;
        }
        //vvv tworzy puste kolumny właściwych typów w wynikowym df vvv
        for(int i=0;i<nameAndTypeGetter.getValue().columns.size(); i++)
        {
            result.columns.add(new Column(nameAndTypeGetter.getValue().columns.get(i).columnName,nameAndTypeGetter.getValue().columns.get(i).columnType));
        }

        return result;
    }

    @Override public DataFrame max()
    {
        DataFrame result = this.DfCreator();
        Value maximum=null;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn).col.add(entry.getValue().columns.get(indexOfGroupingColumn).col.get(0));
            for(int i=0;i<entry.getValue().Size();i++)
            {
                if(i==indexOfGroupingColumn)
                    continue;
                maximum = entry.getValue().columns.get(i).col.get(0);
                for(int j=1;j<entry.getValue().columns.get(i).col.size();j++)
                {
                    if(maximum.lt(entry.getValue().columns.get(i).col.get(j)))
                        maximum = entry.getValue().columns.get(i).col.get(j);
                }
                result.columns.get(i).col.add(maximum);
            }
        }
        return result;
    }

    @Override public DataFrame min()
    {
        DataFrame result = this.DfCreator();
        Value minimum=null;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn).col.add(entry.getValue().columns.get(indexOfGroupingColumn).col.get(0));
            for(int i=0;i<entry.getValue().Size();i++)
            {
                if(i==indexOfGroupingColumn)
                    continue;
                minimum = entry.getValue().columns.get(i).col.get(0);
                for(int j=1;j<entry.getValue().columns.get(i).col.size();j++)
                {
                    if(minimum.gt(entry.getValue().columns.get(i).col.get(j)))
                        minimum = entry.getValue().columns.get(i).col.get(j);
                }
                result.columns.get(i).col.add(minimum);
            }
        }
        return result;
    }

    @Override public DataFrame mean()
    {

    }

    @Override public DataFrame std(){}
    @Override public DataFrame sum(){}
    @Override public DataFrame var(){}
    @Override public DataFrame apply(Applyable a){}
}
