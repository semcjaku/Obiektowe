package lab1;
import java.sql.*;
 
public class DBClient{
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String query = null;

    public void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Temp\\db.db");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }catch(Exception e){e.printStackTrace();}
    }

    public void Select()
    {
        try {
            connect();
            stmt = conn.createStatement();

            // Wyciagamy wszystkie pola z kolumny name
            // znajdujące się w tabeli users
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while(rs.next()){
                for(int i=1;i<columnsNumber+1;i++)
                {
                    String book = rs.getString(i);
                    System.out.print(book+" ");
                }
                System.out.println();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            // zwalniamy zasoby, które nie będą potrzebne
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
    }

    public void SelectAll()
    {
        query="SELECT * FROM books";
        Select();
    }

    public void SelectByISBN(String isbn)
    {
        query="SELECT * FROM books WHERE isbn="+isbn;
        Select();
    }

    public void SelectByAuthor(String author)
    {
        query="SELECT * FROM books WHERE author LIKE '%"+author+"'";
        Select();
    }

    public static void main(String[] args)
    {
        DBClient dbc = new DBClient();
        //dbc.SelectAll();
        //dbc.SelectByISBN("1234567891234");
        dbc.SelectByAuthor("Huxley");
    }
} 


