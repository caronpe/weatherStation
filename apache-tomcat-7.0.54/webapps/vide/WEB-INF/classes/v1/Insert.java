import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Insert{
	public static void main(String args[]){

		Connection con=null;
		Statement stmt=null;


		try{
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("java.sql.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		// connexion a la base
		String url = "jdbc:mysql://localhost/WeatherStation";
		String nom = "root";
		String mdp = "toto";
		String table = "data";
		String query = "";
		try{
			con = DriverManager.getConnection(url,nom,mdp);
			stmt = con.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
		long time;
		double temp, hydr;
		int lum, pres, sound;
		
		for(int i=0; i<20; i++){
			time = 1399460729+(100000*i);
			temp = Math.random()*25;
			hydr = Math.random()*100;
			lum = (int)Math.random()*500;
			pres = (int)Math.random()*1050;
			sound = (int)Math.random()*100;
			query = "Insert into data values("+time+","+temp+","+hydr+","+lum+","+pres+","+sound+")";
			try{
				stmt.executeUpdate(query);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		try{
			stmt.close();
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
