import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet/Stats")
public class Stats extends HttpServlet{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{

		Connection con=null;
		Statement stmt=null;
		String n="";
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
		long time = Integer.valueOf(req.getParameter("time"));
		String interval = req.getParameter("interval");
		query = "select avg(time) as time, avg(temperature) as temperature, avg(hydrometrie) as hydrometrie, avg(luminosite) as luminosite, avg(pression) as pression, avg(sound) as sound from "+table+" where time >= "+time+" and time <= "+PosixTime.addIntervalPosix(time,interval)+"";
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

		out.println(query);
		out.println( "<head><title>Weather Station</title></head><body><center>" );
		out.println( "<h1>Servlet</h1>" );
		try{
			out.println( "<h2>time : "+rs.getString("time")+"</h2>" );
		}catch(SQLException e){
			e.printStackTrace();
		}
		out.println("<TABLE width=60% border=0>");
		out.println("<TR>");
		try{
			for(int i = 1; i<=taille; i++){
				out.print("<TH>"+rsmd.getColumnName(i)+"</TH>");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		out.println("</TR>");
		out.println("<TR>");
		for(int i = 1; i<=taille; i++){
			if((i-1)%taille==0){
				out.println("</TR>");
				out.println("<TR>");	
			}

			try{
				n = rs.getString(rsmd.getColumnName(i));
			}catch(SQLException e){
				e.printStackTrace();
			}
			out.print("<TD>"+n.trim()+"</TD>");

		}


		out.println("</TR>");
		out.println("</TABLE>");
		out.println("</br></br></br><a href=\"/vide/servlet/Menu\">Retour</a>");
		out.println("</br></br></br><a href=\"/vide/servlet/Voir\">Accueil</a>");
		out.println( "</center> </body>" );



		try{
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
