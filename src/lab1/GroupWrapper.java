package lab1;

import java.util.HashMap;
import java.util.Map;

public class GroupWrapper implements Groupby
{
    public HashMap<Value,DataFrame> group;
    public int indexOfGroupingColumn, omittedColumnsIndexModifier;

    public GroupWrapper(HashMap<Value,DataFrame> groupbyResult, int idx)
    {
        group = new HashMap<Value,DataFrame>();
        group = groupbyResult;
        indexOfGroupingColumn=idx;
        omittedColumnsIndexModifier=0;
    }

    public DataFrame DfCreatorForComparisons()
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

    public DataFrame DfCreatorForOperations()
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
            if(!(i!=indexOfGroupingColumn && (nameAndTypeGetter.getValue().columns.get(i).columnType==StringValue.class || nameAndTypeGetter.getValue().columns.get(i).columnType==DateTimeValue.class)))
                result.columns.add(new Column(nameAndTypeGetter.getValue().columns.get(i).columnName,nameAndTypeGetter.getValue().columns.get(i).columnType));
            else if(i<indexOfGroupingColumn)
                omittedColumnsIndexModifier++;
        }

        return result;
    }

    @Override public DataFrame max()
    {
        DataFrame result = this.DfCreatorForComparisons();
        Value maximum=null;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn).col.add(entry.getValue().columns.get(indexOfGroupingColumn).col.get(0));
            for(int i=0;i<entry.getValue().columns.size();i++)
            {
                if(i==indexOfGroupingColumn)
                    continue;
                maximum = entry.getValue().columns.get(i).col.get(0);
                for(int j=1;j<entry.getValue().Size();j++)
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
        DataFrame result = this.DfCreatorForComparisons();
        Value minimum=null;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn).col.add(entry.getValue().columns.get(indexOfGroupingColumn).col.get(0));
            for(int i=0;i<entry.getValue().columns.size();i++)
            {
                if(i==indexOfGroupingColumn)
                    continue;
                minimum = entry.getValue().columns.get(i).col.get(0);
                for(int j=1;j<entry.getValue().Size();j++)
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
        DataFrame result = this.DfCreatorForOperations();
        Value sum=null;
        int resultIndex;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.add(entry.getValue().columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.get(0));
            resultIndex=0;
            for(int i=0;i<entry.getValue().columns.size();i++)
            {
                if(i==indexOfGroupingColumn)
                {
                    resultIndex++;
                    continue;
                }
                sum = entry.getValue().columns.get(i).col.get(0);
                if(sum instanceof DateTimeValue || sum instanceof StringValue)
                    continue;
                for(int j=1;j<entry.getValue().Size();j++)
                {
                    sum = sum.add(entry.getValue().columns.get(i).col.get(j));
                }
                result.columns.get(resultIndex).col.add(sum.div(new IntegerValue(entry.getValue().Size())));
                resultIndex++;
            }
        }
        return result;
    }

    @Override public DataFrame std()
    {
        DataFrame result = this.var();

        for(int i=0;i<result.columns.size();i++)
        {
            if(i==indexOfGroupingColumn)
                continue;
            for(int j=0;j<result.Size();j++)
            {
                result.columns.get(i).col.set(j,result.columns.get(i).col.get(j).pow(new DoubleValue(0.5)));
            }
        }
        return result;
    }

    @Override public DataFrame sum()
    {
        DataFrame result = this.DfCreatorForOperations();
        Value sum=null;
        int resultIndex;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.add(entry.getValue().columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.get(0));
            resultIndex=0;
            for(int i=0;i<entry.getValue().columns.size();i++)
            {
                if(i==indexOfGroupingColumn)
                {
                    resultIndex++;
                    continue;
                }
                sum = entry.getValue().columns.get(i).col.get(0);
                if(sum instanceof DateTimeValue || sum instanceof StringValue)
                    continue;
                for(int j=1;j<entry.getValue().Size();j++)
                {
                    sum = sum.add(entry.getValue().columns.get(i).col.get(j));
                }
                result.columns.get(resultIndex).col.add(sum);
                resultIndex++;
            }
        }
        return result;
    }

    @Override public DataFrame var()
    {
        DataFrame means = this.mean();
        DataFrame result = this.DfCreatorForOperations();
        Value sum=null;
        int mRowIdx=0, resultIndex;

        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            result.columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.add(entry.getValue().columns.get(indexOfGroupingColumn-omittedColumnsIndexModifier).col.get(0));
            resultIndex=0;
            for(int i=0;i<entry.getValue().columns.size();i++)
            {
                if(i==indexOfGroupingColumn)
                {
                    resultIndex++;
                    continue;
                }
                sum = entry.getValue().columns.get(i).col.get(0);
                if(sum instanceof DateTimeValue || sum instanceof StringValue)
                    continue;
                for(int j=1;j<entry.getValue().Size();j++)
                {
                    sum = sum.add(((entry.getValue().columns.get(i).col.get(j)).sub(means.columns.get(resultIndex).col.get(mRowIdx))).pow(new IntegerValue(2)));
                }
                result.columns.get(resultIndex).col.add(sum.div(new IntegerValue(entry.getValue().Size()-1)));
                resultIndex++;
            }
            mRowIdx++;
        }
        return result;
    }

    @Override public DataFrame apply(Applyable a)
    {
        DataFrame result = this.DfCreatorForOperations();
        DataFrame tmp = null;
        for (HashMap.Entry<Value, DataFrame> entry : group.entrySet())
        {
            tmp = a.apply(entry.getValue());
            for(int i=0;i<tmp.Size();i++)
            {
                result.addRow(tmp.Iloc(i));
            }
        }
        return result;
    }
}
