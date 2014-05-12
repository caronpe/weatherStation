import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Version2{

    public static void main(String args[]) throws SQLException {
        Connection mysqlCon = null;
        try {
           Class.forName("com.mysql.jdbc.Driver");
            mysqlCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
        } catch (Exception e) {
            System.out.println(e);
        }
 
    }
}
