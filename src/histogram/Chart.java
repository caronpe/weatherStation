package histogram;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vide.HTMLparser;

@WebServlet("/servlet/Chart")
public class Chart extends HttpServlet {

	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
 
		PrintWriter out = res.getWriter();
		res.setContentType( "text/html" );


		String var = req.getParameter("var");
		//create chart
		try {
			Histogramme.createHisto(var);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HTMLparser.header(out);
		out.println("	<div id=\"content\">");
		out.println( "<div id=\"content_title\"> The "+ var +" chart </div>" );
		
		out.println("<center><img src=\"/vide/snapshots/"+ var +".jpeg\" alt=\"Image not found\" height=\"600\" width=\"800\"></img></center>");

		HTMLparser.footer(out);
		
	}
	

}