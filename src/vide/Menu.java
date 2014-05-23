package vide;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.apache.commons.lang3.StringEscapeUtils;
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
		long  n=0;
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

		HTMLparser.header(out);
		
		
		GregorianCalendar c = new GregorianCalendar();
		int year=0, month=0, week=0,day=0;
		try{
			out.print("<div>");
			while(rs.next()){
				try{
					n = rs.getLong("time");
				}catch(SQLException e){
					e.printStackTrace();
				}
				c.setTimeInMillis(n*1000);
				if(c.get(GregorianCalendar.YEAR)!=year){
					year = c.get(GregorianCalendar.YEAR);
					out.print("</div>");
					out.print("<div id=\"content\">");
					out.print("<div id=\"content_title\"><a href=\"/vide/servlet/Stats?time="+n+"&interval=year\">Year "+year+"</a>");
					out.print("</div>");
				}
				if(c.get(GregorianCalendar.MONTH)!=month){
					month = c.get(GregorianCalendar.MONTH);
					out.print("<p><h2>&nbsp;&nbsp;<a href=\"/vide/servlet/Stats?time="+n+"&interval=month\">"+c.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, new Locale("en"))+"</a></h2></p>");
				}
				if(c.get(GregorianCalendar.WEEK_OF_YEAR)!=week){
					week = c.get(GregorianCalendar.WEEK_OF_YEAR);
					out.print("<h3>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/vide/servlet/Stats?time="+n+"&interval=week\">Week "+week+"</a></h3>");
				}
				if(c.get(GregorianCalendar.DAY_OF_MONTH)!=day){
					day = c.get(GregorianCalendar.DAY_OF_MONTH);
					out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/vide/servlet/Stats?time="+n+"&interval=day\">"+day+"</a>");
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		out.println("</div>");
		HTMLparser.footer(out);


		try{
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
