package lab1;

import java.util.HashMap;
import java.util.Map;

public class GroupWrapper implements Groupby
{
    public HashMap<Value,DataFrame> group;

    public GroupWrapper(HashMap<Value,DataFrame> groupbyResult)
    {
        group = new HashMap<Value,DataFrame>();
        group = groupbyResult;
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
            for(int i=0;i<entry.getValue().Size();i++)
            {
                maximum = entry.getValue().columns.get(i).col.get(0);
                for(int j=1;j<entry.getValue().columns.get(i).col.size();j++)
                {
                    if(maximum.lt(entry.getValue().columns.get(i).col.get(j)))
                        maximum = entry.getValue().columns.get(i).col.get(j);
                }
            }
            //result dodaj wiersz w któym jest maximum;
        }
    }
    @Override public DataFrame min(){}
    @Override public DataFrame mean(){}
    @Override public DataFrame std(){}
    @Override public DataFrame sum(){}
    @Override public DataFrame var(){}
    @Override public DataFrame apply(Applyable a){}
}
