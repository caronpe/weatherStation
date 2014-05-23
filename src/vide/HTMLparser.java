package vide;

import java.io.*;

public class HTMLparser {
	public static void header(PrintWriter out){
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
		out.println("	\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"fr\">");

		out.println("<head>");
		out.println("	<meta http-equiv=\"content-type\" content=\"text-html ; charset=UTF-8\"/>");
		out.println("	<title>Portable Weather Station</title>");
		out.println("	<link rel=\"stylesheet\" href=\"../css/style.css\" />");
		out.println("</head>");
		out.println("<body>");
		out.println("	<div id=\"header\">");
		out.println("		<div id=\"topmenu\">");
		out.println("			<a href=\"/vide/servlet/Voir\">Home</a>");
		out.println("			<a href=\"/vide/servlet/Menu\">Menu</a>");
		out.println("		</div>");
		out.println("	</div>");
	}
	
	public static void footer(PrintWriter out){
		/*out.println("		<div id=\"footer_line\">");
		out.println("			<div id=\"footer_content\">");
		out.println("				<a href=\"/vide/servlet/Voir\">Home</a>");
		out.println("				<a href=\"/vide/servlet/Menu\">Menu</a>");
		out.println("			</div>");
		out.println("		</div>");
		out.println("	</div>");*/
		out.println("</body>");
		out.println("</html>");
	}
}
