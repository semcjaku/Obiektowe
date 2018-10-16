package lab1;
import java.util.ArrayList;

public class COOLColumn extends Column {

    ArrayList<COOLValue> col;

    public COOLColumn(String name, String type)
    {
        super(name, type);
        col = new ArrayList<COOLValue>();
    }

}
