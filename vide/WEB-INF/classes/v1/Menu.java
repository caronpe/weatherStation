import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet/Menu")
public class Menu extends HttpServlet{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{

		Connection con=null;
		Statement stmt=null;
		int  n=0;
		PrintWriter out = null;
		out = res.getWriter();
		res.setContentType( "text/html" );


		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		// connexion a la base
		String url = "jdbc:mysql://localhost:3306/WeatherStation";
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
		query = "select time from "+table+" order by time ASC";
		int taille = 0;
		ResultSetMetaData rsmd =null;
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(query);
			rsmd = rs.getMetaData();
			taille = rsmd.getColumnCount();
			rs.next();
		}catch(SQLException e){
			e.printStackTrace();
		}


		out.println( "<head><title>Weather Station</title></head><body><center>" );
		out.println( "<h1>Servlet</h1>" );
		out.println( "<h2>Statistiques :</h2>" );
		out.println("<TABLE width=60% border=0>");
		out.println("<TR>");
		out.print("<TH>Annee</TH>");
		out.print("<TH>Mois</TH>");
		out.print("<TH>Semaine</TH>");
		out.print("<TH>Jour</TH>");

		out.println("</TR>");
		out.println("<TR>");
		GregorianCalendar c = new GregorianCalendar();
		int year=0, month=0, week=0,day=0;
		try{
			while(rs.next()){
				for(int i = 1; i<=taille; i++){
					if((i-1)%taille==0){
						out.println("</TR>");
						out.println("<TR>");	
					}

					try{
						n = rs.getInt("time");
					}catch(SQLException e){
						e.printStackTrace();
					}
					c.setTimeInMillis(n*1000);
					if(c.get(GregorianCalendar.YEAR)!=year){
						year = c.get(GregorianCalendar.YEAR);
						out.print("<TD><a href=\"/vide/servlet/Stats?time="+n+"&interval=year\">"+year+"</a></TD>");
						out.println("</TR><TR>");
					}
					if(c.get(GregorianCalendar.MONTH)!=month){
						month = c.get(GregorianCalendar.MONTH);
						out.println("<TD></TD>");
						out.print("<TD><a href=\"/vide/servlet/Stats?time="+n+"&interval=month\">"+month+"</a></TD>");
						out.println("</TR><TR>");
					}
					if(c.get(GregorianCalendar.WEEK_OF_YEAR)!=week){
						week = c.get(GregorianCalendar.WEEK_OF_YEAR);
						out.println("<TD></TD>");
						out.println("<TD></TD>");
						out.print("<TD><a href=\"/vide/servlet/Stats?time="+n+"&interval=week\">"+week+"</a></TD>");
						out.println("</TR><TR>");
					}
					if(c.get(GregorianCalendar.DAY_OF_MONTH)!=day){
						day = c.get(GregorianCalendar.DAY_OF_MONTH);
						out.println("<TD></TD>");
						out.println("<TD></TD>");
						out.println("<TD></TD>");
						out.print("<TD><a href=\"/vide/servlet/Stats?time="+n+"&interval=day\">"+day+"</a></TD>");
						out.println("</TR><TR>");
					}

				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}


		out.println("</TR>");
		out.println("</TABLE>");
		out.println("</br></br></br><a href=\"/vide/servlet/Voir\">Accueil</a>");
		out.println( "</center> </body>" );



		try{
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
