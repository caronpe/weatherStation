package histogram;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;

public class Histogramme {
	
	public static void createHisto(String variable) throws ClassNotFoundException, SQLException{
		String query = "SELECT date, "+ variable +  " from weather where (select DATE(date)) = CURDATE() ORDER BY date ASC;";
		JDBCCategoryDataset dataset = new JDBCCategoryDataset(
				"jdbc:mysql://localhost:3306/mydb", "com.mysql.jdbc.Driver",
				"plockyn", "theo");

		dataset.executeQuery(query);
		JFreeChart chart = ChartFactory.createLineChart("Test", "date", variable,
				dataset, PlotOrientation.VERTICAL, true, true, false);
		
		try {
			 int width = 1000;
		     int height = 600;
	        ChartUtilities.saveChartAsJPEG(new File("~/weatherStation/apache-tomcat-7.0.54/webapps/vide/snapshots/" + variable+".jpeg"), chart, width, height);
	        } catch (IOException e) {}
       
		System.out.println("Histogram "+ variable + " done");
		dataset.clear();
		}

    public static void main(String[] args) throws Exception {
    	createHisto("temperature");
    }
}