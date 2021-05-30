import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * 
 */
public class Graph extends ApplicationFrame {
	//#region properties
	private static final long serialVersionUID = -8630125151695369181L;
	public static Filter_value graphOptions = new Filter_value("graphOptions");
	public static Filter_value account = new Filter_value("Account");
	public static Filter_value character = new Filter_value("Character");
	public static Filter_value realm = new Filter_value("Realm");
	public static Filter_value type = new Filter_value("Type");
	public static Filter_value id = new Filter_value("ID");
	public static Filter_value name = new Filter_value("Name");
	public static Filter_value loc = new Filter_value("Loc");
	public static Filter_value otherPlayer = new Filter_value("otherPlayer");
	public static JFreeChart lineChart;
	//#endregion

	//#region constructors
	public Graph( String applicationTitle , String chartTitle, String xaxis, String yaxis, XYDataset dataset, Subset subset) throws ParseException, IOException {
		super(applicationTitle);
		List<String> values = new ArrayList<String>() {
			private static final long serialVersionUID = -6992133666776967787L;
			{
				add("Average");
				add("Quantity");
				add("Sum");
			}
		};
		Graph.graphOptions.setArray(values);;
		Graph.account.setArray(Data_processing.getPossibleValues(subset, "account"));
		Graph.character.setArray(Data_processing.getPossibleValues(subset, "character"));
		Graph.realm.setArray(Data_processing.getPossibleValues(subset, "realm"));
		Graph.type.setArray(Data_processing.getPossibleValues(subset, "type"));;
		Graph.id.setArray(Data_processing.getPossibleValues(subset, "id"));
		Graph.name.setArray(Data_processing.getPossibleValues(subset, "name"));
		Graph.otherPlayer.setArray(Data_processing.getPossibleValues(subset, "otherPlayer"));
		Graph.loc.setArray(Data_processing.getPossibleValues(subset, "loc"));
		
		lineChart = ChartFactory.createTimeSeriesChart(
			"Gold Per Day",
			"Day",
			"Gold",
			dataset,
			false,
			true,
			true
		);
		// lineChart.getXYPlot().getRangeAxis().setAutoRange(true);
		//lineChart.getXYPlot().getDomainAxis().setAutoRange(true);
		//lineChart.getXYPlot().getRangeAxis().
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) lineChart.getXYPlot().getRenderer();
		renderer.setBaseShapesVisible(true);
		Double d =1.5d;
		Shape circle = new Ellipse2D.Double(-1*d, -1*d, 2*d, 2*d);
		renderer.setSeriesShape(0, circle);
		ChartPanel chartPanel = new ChartPanel( lineChart );
		
		chartPanel.setPreferredSize( new java.awt.Dimension( 1200 , 600 ) );
		//chartPanel.setHorizontalAxisTrace(true);
		//chartPanel.setVerticalAxisTrace(true);
		
		chartPanel.setRangeZoomable(false);

		//restoreAutoBounds(chartPanel.ZOOM_RESET_RANGE_COMMAND);
		chartPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				//chartPanel.restoreAutoRangeBounds();
				
			}
		});
		chartPanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				//chartPanel.restoreAutoRangeBounds();
				//chartPanel.range
			}
			
		});
		chartPanel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if (arg0.getWheelRotation() > 0) {
					chartPanel.zoomOutDomain(0.5, 0.5);
				} else if (arg0.getWheelRotation() < 0) {
					chartPanel.zoomInDomain(1.5, 1.5);
				}
				//chartPanel.restoreAutoRangeBounds();
				///chartPanel.auto
				
			}
		});
	 	setContentPane( chartPanel );
   	}
	//#endregion
}