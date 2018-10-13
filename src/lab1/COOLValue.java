package lab1;

public class COOLValue {
	private int index;
	private Object value;
	
	public COOLValue(int i, Object v)
	{
		index = i;
		value = v;
	}
	
	public int GetIndex()
	{
		return index;
	}
	
	public void SetIndex(int x)
	{
		index = x;
	}
	
	public Object GetValue()
	{
		return value;
	}
	
	public void SetValue(Object x)
	{
		value = x;
	}

}
