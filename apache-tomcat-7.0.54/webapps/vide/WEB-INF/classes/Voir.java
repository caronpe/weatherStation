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
import java.io.File;

@WebServlet("/servlet/Voir")
public class Voir extends HttpServlet{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{

		Connection con=null;
		Statement stmt=null;
		String n="";
		PrintWriter out = null;
		out = res.getWriter();
		res.setContentType( "text/html" );
		// Autorefresh par 5 minutes
		res.addHeader("Refresh", ""+5*60);

		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		// connexion a la base
		// A CHANGER POUR CHAQUE PORTAGE
		String url = "jdbc:mysql://localhost/mydb";
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
		query = "select * from "+table+" where date >= all(select date from "+table+")";
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

		long time = 0;
		
		// Header
		HTMLparser.header(out);
		out.println("	<div id=\"content\">");
		try{
			time = rs.getInt("date");
			out.println("<div id=\"content_title\">"+PosixTime.convertPosix(time)+"</div>" );
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		// Content
		out.println("<TABLE width=100% border=0>");
		out.println("<TR>");
		try{	
			// Titres de colonnes
			for(int i = 1; i<=taille; i++){
				if(!((rsmd.getColumnName(i).equals("date"))))
					out.print("<TD><b>"+rsmd.getColumnName(i)+"</b></TD>");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		out.println("</TR>");
		out.println("<TR>");

		for(int i = 1; i<=taille; i++){
			try{
				// Contenu de colonnes
				if(!((rsmd.getColumnName(i).equals("date")))){
					n = rs.getString(rsmd.getColumnName(i));
					out.print("<TD>"+n.trim()+"</TD>");
				}
			}catch(SQLException e){
					e.printStackTrace();
			}
		}

		// Recuperation de l'image la plus proche de la date
		time = Voir.getImage(time);
		out.println("</TR>");
		out.println("</TABLE>");
		// Affichage de l'image
		out.println("<center><a href=\"/vide/servlet/Images?time="+time+"\"><img src=\"/vide/snapshots/timestamp-"+time+".jpeg\" alt=\"Image not found\" height=\"600\" width=\"800\"></img></a></center>");
		out.println("</div>");
		
		// Footer
		HTMLparser.footer(out);
		


		// Deconnexion de la bdd
		try{
			stmt.close();
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}


	// Renvoie le temps unix de l'image la plus proche du temps entre en parametre
	public static long getImage(long time){
		String path = "/home/pi/Document/weatherStation/apache-tomcat-7.0.54/webapps/vide/snapshots/";
		//"/home/thor/WeatherStation/apache-tomcat-8.0.5/webapps/vide/snapshots/";
	
		File img = new File(path+"img-"+time+".jpg");
		if(img.exists()){
			return time;
		}
		int cpt = 0;
		long old = time;
		while(!img.exists() && cpt < 200){
			time = time+1;
			img = new File(path+"img-"+time+".jpg");
			cpt++;
		}
		if(cpt==200){
			time = old;
			cpt = 0;
			while(!img.exists() && cpt < 200){
				time = time-1;
				img = new File(path+"img-"+time+".jpg");
				cpt++;
			}
		}
		return time;
	}
	
}
