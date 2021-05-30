import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;

public class Graph_wrapper {
	//#region properties
	static Graph chart;
	static long interval;
	static Subset subset;
	//#endregion

	//#region methods
	/** 
	 * @param intv
	 * @param s
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void init(long intv, Subset s) throws ParseException, IOException {
		
		//Time_series.init(s);

		System.out.println("graph");
		subset=s;
		interval=intv;

      createGraph(interval,s);
	}

	/** 
	 * @param interval
	 * @param s
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void createGraph(long interval, Subset s) throws ParseException, IOException {
		chart = new Graph("Gold Per Day" , "", "date", "gold", getData(s),s);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen( chart );
		chart.setVisible( true );	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	GUI.createAndShowGUI(s);
            }
        });

	}

	/** 
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void updateGraph() throws ParseException, IOException {
		
		//System.out.println("updateGraph");
		Subset s = Data_processing.updateGraphbyFilters(subset);
		PrintWriter out = new PrintWriter(new File("graph.txt"));
		for(Item item : s.items) {
      		for(Event e: item.events) {
      			try {
					out.println(e.printData());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
      		}
      	}
      	out.close();
		//GUI.frame.dispose();
		//chart.dispose();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              //  GUI.createAndShowGUI();
            }
        });
		//moving this chart= into the invokeLater makes sales negative gold values and purchases posisitve. wut
		//or maybe it's just switching the types of events, from sales to purchases and vice versa
		//chart = new Graph("Gold Per Day" , "", "date", "gold", getData(s),s);
		Graph.lineChart.getXYPlot().setDataset(getData(s));
		
		//chart.update();
		//chart.pack( );
		//RefineryUtilities.centerFrameOnScreen( chart );
		//chart.setVisible( true );	
		
	}

	/** 
	 * @param interval
	 * @param subset
	 * @return DefaultCategoryDataset
	 * @throws ParseException
	 * @throws IOException
	 */
	private static DefaultCategoryDataset G_QperInterval(long interval, Subset subset) throws ParseException, IOException {
		int count =0;
		//interval = interval /24;
		System.out.println("sum of graph: "+String.format("%.2f",Data_processing.sum(subset.items,"goldspent")));
		
		//System.out.println(subset.items.size());
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		long startExact = (long) Data_processing.min(subset.items,"time")*1000l;
		long startDay = Master.mdy.parse(Master.mdy.format(new Date(startExact))).getTime();
		long endExact = (long) Data_processing.max(subset.items,"time")*1000l;
		long endDay = Master.mdy.parse(Master.mdy.format(new Date(endExact))).getTime()+interval;
		//System.out.println(endDay+" "+startDay+" "+Master.mdy.format(new Date(startDay))+ " // "+Master.mdy.format(new Date(endDay)));
		if(endDay-startDay<86400001*3) {
			endDay=endDay+86400000;
			startDay=startDay-86400000;
		}
		for(long i=startDay; i<endDay; i+=interval) {
			//System.out.println(Master.mdy.format(new Date(i))+ " // "+Master.mdy.format(new Date(i+dayLength)));
			Subset s =  new Subset(subset).date(i, i+interval);
			//System.out.println(s.items);
			long goldperday = (long) Data_processing.sum(s.items,"gold");
			//System.out.println(s.items.get(0).events.get(0).priceGold);
			//System.out.println(goldperday+" "+(i-startDay)/dayLength);
			//if(goldperday != 0) {
				dataset.addValue(goldperday, "gold", Master.mdy.format(i));//Integer.valueOf(count));//Long.toString((i-startDay)/dayLength));
			//}
			count++;
		}
		System.out.println(String.format("%.2f", Data_processing.sum(subset.items,"goldspent")));
		System.out.println("avg value: "+String.format("%.2f", Data_processing.sum(subset.items,"goldspent")/(((endDay-startDay)/interval)-1)));
		return dataset;
	}

	/** 
	 * @param subset
	 * @return XYDataset
	 * @throws ParseException
	 * @throws IOException
	 */
	private static XYDataset getData(Subset subset) throws ParseException, IOException {
		List<Long> xcoords = new ArrayList<Long>();
		List<Long> ycoords = new ArrayList<Long>();
		int count =0;
		//interval = interval /24;
		//System.out.println("sum of graph: "+String.format("%.2f",Data_processing.sum(subset.items,"goldspent")));
		long interval = 86400000;
		//System.out.println(subset.items.size());
		TimeSeries dataset = new TimeSeries("Gold");
		long startExact = (long) Data_processing.min(subset.items,"time")*1000l;
		long startDay = Master.mdy.parse(Master.mdy.format(new Date(startExact))).getTime()+7200000;
		long endExact = (long) Data_processing.max(subset.items,"time")*1000l;
		long endDay = Master.mdy.parse(Master.mdy.format(new Date(endExact))).getTime()+interval;
		//System.out.println(endDay+" "+startDay+" "+Master.mdy.format(new Date(startDay))+ " // "+Master.mdy.format(new Date(endDay)));
		if(endDay-startDay<86400001*3) {
			endDay=endDay+86400000*3;
			startDay=startDay-86400000*3;
		}
		
		//System.out.println(Master.mdy.format(new Date(startDay)));
		//System.out.println(startDay);
		//System.out.println(Master.mdy.format(new Date(endDay)));
		//System.out.println(endDay);
		for(long x=startDay; x<endDay; x+=interval) {
			long i=Master.mdy.parse(Master.mdy.format(new Date(x))).getTime();
			long iplus= Master.mdy.parse(Master.mdy.format(new Date(i+interval))).getTime();
			//System.out.println(Master.mdy.format(new Date(i))+ " // "+Master.mdy.format(new Date(i+interval)));
			//System.out.println(i+"\t // \t"+iplus);
			Subset s =  new Subset(subset).date(i, iplus);
			//System.out.println(s.items);
			long goldperday = (long) Data_processing.sum(s.items,"gold");
			//System.out.println(s.items.get(0).events.get(0).priceGold);
			//System.out.println(goldperday+" "+(i-startDay)/dayLength);
			//if(goldperday != 0) {
			try {
				dataset.add(new Day(new Date(i)), goldperday);//Integer.valueOf(count));//Long.toString((i-startDay)/dayLength));
				xcoords.add(i);
				ycoords.add(goldperday);
			}
			catch(org.jfree.data.general.SeriesException e) {
				//System.out.println("FAILED TO ADD DATA POINT: "+ i+", "+goldperday);
			}
			count++;
		}
		//System.out.println(String.format("%.2f", Data_processing.sum(subset.items,"goldspent")));
		//System.out.println("avg value: "+String.format("%.2f", Data_processing.sum(subset.items,"goldspent")/(((endDay-startDay)/interval)-1)));
		List<Long> pastTen = new ArrayList<Long>();
		int movingAverageSize = 14;
		for(int i=0; i<xcoords.size(); i++) {
			
			pastTen.add(ycoords.get(i));
			
			if(pastTen.size() > movingAverageSize) {
				pastTen.remove(0);
				long sum = 0;
				for(long value : pastTen) {
					sum+=value;
				}
				sum/=pastTen.size();
				//dataset.add(new Day(new Date(xcoords.get(i))), sum);
			}
		}
		return new TimeSeriesCollection(dataset);
	}
	//#endregion
}
