package test;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Version {

    public static void main(String[] args) throws ClassNotFoundException {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Class.forName("com.mysql.jdbc.Driver");
        
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "plockyn";
        String password = "theo";

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("show tables;");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}