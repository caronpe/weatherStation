import java.awt.Color;
import java.io.*;
import java.util.Random;
import org.jfree.chart.*;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

 public class HistogrammeExample {
	 
       public static void main(String[] args) {
       double[] value = new double[100];
       Random generator = new Random();
       for (int i=1; i < 100; i++) {
       value[i] = generator.nextDouble();
       }
       int number = 100;
       HistogramDataset dataset = new HistogramDataset();
       dataset.setType(HistogramType.RELATIVE_FREQUENCY);
       dataset.addSeries("Histogram",value,number);
       System.out.println("series count:" + dataset.getSeriesCount() + "  geSeriesKey: "+ dataset.getSeriesKey(0) + " getitemcount:" +  dataset.getItemCount(0));
       String plotTitle = "Histogram"; 
       String xaxis = "number";
       String yaxis = "value"; 
       PlotOrientation orientation = PlotOrientation.VERTICAL; 
       boolean show = false; 
       boolean toolTips = false;
       boolean urls = false; 
       JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis, 
                dataset, orientation, show, toolTips, urls);
       
       XYPlot yourXYPlot = (XYPlot)chart.getPlot();
       /*mettre une couleur transparente aux barre de l'histogramme
       yourXYPlot.setForegroundAlpha(0.6F);
       */
       /*changer la couleur
       plot.getRenderer().setSeriesPaint(0, Color.BLUE);
       */
       int width = 1000;
       int height = 600; 
        try {
        ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
        } catch (IOException e) {}
       System.out.println("Histogram done");
   }
 }