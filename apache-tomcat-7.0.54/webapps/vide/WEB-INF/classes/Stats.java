import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang3.StringEscapeUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
		// A CHANGER POUR CHAQUE PORTAGE SUR UNE AUTRE BASE
		String url = "jdbc:mysql://localhost:3306/mydb";
		String nom = "weatherStation1";
		String mdp = "weather";
		String table = "weather";
		String query = "";
		try{
			con = DriverManager.getConnection(url,nom,mdp);
			stmt = con.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		// Recuperation des parametres
		long time = Integer.valueOf(req.getParameter("time"));
		String interval = req.getParameter("interval");
		// A CHANGER POUR CHAQUE NOUVELLE BDD
		query = "select avg(date) as date, avg(temperature) as temperature, avg(humidity) as humidity, avg(light) as light, avg(pressure) as pressure, avg(noise) as noise from "+table+" where date >= "+time+" and date <= "+PosixTime.addIntervalPosix(time,interval)+"";
		int taille = 0;
		// Execution de la requete
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


		// Header
		HTMLparser.header(out);
		
		// Content
		out.println("<div id=\"content\">");
		out.println("<div id=\"content_title\">"+this.getDate(time, interval)+"</div>");     
		out.println("<TABLE width=60% border=0>");
		out.println("<TR>");
		try{
			for(int i = 2; i<=taille; i++){
				// Noms de colonnes
				out.print("<TD><b>"+rsmd.getColumnName(i)+"</b></TD>");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		out.println("</TR>");
		out.println("<TR>");
		
		// Contenu des colonnes
		for(int i = 2; i<=taille; i++){
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
		out.println("</div>");
		// Footer
		HTMLparser.footer(out);


		// Deconnexion a la bdd
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
	
	// Renvoie la date en fonction de l'intervalle choisi
	public String getDate(long time, String interval){
		String date = "Stats for ";
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(time*1000);
		date+=c.get(GregorianCalendar.YEAR)+"";
		if(!interval.equals("year")){
			date+=", "+c.getDisplayName(GregorianCalendar.MONTH, Calendar.LONG, new Locale("en"))+"";
			if(!interval.equals("month")){
				date+=", week "+c.get(GregorianCalendar.WEEK_OF_YEAR);
				if(!interval.equals("week"))
					date+=", day "+c.get(GregorianCalendar.DAY_OF_MONTH);
			}
		}
		return date;
	}

}
