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

@WebServlet("/servlet/Images")
public class Images extends HttpServlet{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
 
		String n="";
		PrintWriter out = null;
		out = res.getWriter();
		res.setContentType( "text/html" );


		long time = Integer.valueOf(req.getParameter("time"));
		time = Voir.getImage(time);
		
		HTMLparser.header(out);
		out.println("	<div id=\"content\">");
		out.println( "<div id=\"content_title\"> Image from "+PosixTime.convertPosix(time)+"</div>" );
		out.println("<TABLE width=100% border=0>");
		out.println("<TR>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "year", -1)+"\"><< Year</a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "month", -1)+"\"><< Month</a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "week", -1)+"\"><< Week</a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "day", -1)+"\"><< Day</a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "minute", -1)+"\"><</a> <a href=\"/vide/servlet/Image?time="+PosixTime.addIntervalPosix(time, "minute", 1)+"\">></a> </TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "day", 1)+"\">Day >></a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "week", 1)+"\">Week >></a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "month", 1)+"\">Month >></a></TD>");
		out.println("	<TD><a href=\"/vide/servlet/Images?time="+PosixTime.addIntervalPosix(time, "year", 1)+"\">Year >></a></TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("<center><img src=\"/vide/snapshots/img-"+time+".jpg\" alt=\"Image not found\" height=\"600\" width=\"800\"></img></center>");
		out.println("</div>");
		HTMLparser.footer(out);
		
	}
	

}
