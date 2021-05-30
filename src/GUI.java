import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class GUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5970893809825175093L;
	static JFrame frame = new JFrame("Filters");
	GridLayout grid = new GridLayout(1,7);
	Button clearBtn = new Button("Clear");
	Button updateBtn = new Button("Update");
	static ArrayList<DynamicList> lists = new ArrayList<DynamicList>();
	static JDatePickerImpl startDatePicker;
	static JDatePickerImpl endDatePicker;
	public static Subset s = new Subset(new ArrayList<Item>());
	
	public GUI() {
		super ();
		//System.out.println("*"+Graph.account.getArray()+"\n\n");
		//lists.add(createDynamicList(Graph.graphOptions));
		lists.add(createDynamicList(Graph.account));
		lists.add(createDynamicList(Graph.character));
		lists.add(createDynamicList(Graph.realm));
		lists.add(createDynamicList(Graph.type));
		lists.add(createDynamicList(Graph.name));
		//lists.add(createDynamicList(Graph.otherPlayer));
		//lists.add(createDynamicList(Graph.id));
		lists.add(createDynamicList(Graph.loc));
		
		JPanel date_panel = new JPanel(new BorderLayout());
		JPanel start_date = getNewDatePanel("Start Date: ");
		JPanel end_date = getNewDatePanel("End Date: ");
		
		date_panel.add(start_date,BorderLayout.NORTH);
		date_panel.add(end_date,BorderLayout.SOUTH);
	   // frame.add(date_panel);
		JPanel panel = new JPanel(new BorderLayout());
		
		add(updateBtn);
		updateBtn.addActionListener(this);
		panel.add(updateBtn,BorderLayout.NORTH);
		
		
		//add(clearBtn);
		clearBtn.addActionListener(this);
	   // panel.add(clearBtn,BorderLayout.SOUTH);
		frame.add(panel);
		//vbox.setSpacing(8);
		frame.setLayout(new FlowLayout());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		//frame.setLocationRelativeTo(null);
		//frame.setSize(new java.awt.Dimension( 1000 , 1000 ));
		frame.setVisible(true);
		


	}
	
	public static JPanel getNewDatePanel(String str){
		Date date = new Date();
		UtilDateModel model = new UtilDateModel();
		model.setDate(Integer.parseInt(Master.yyyy.format(date)),Integer.parseInt(Master.M.format(date)),Integer.parseInt(Master.d.format(date)));
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
		if(str.equals("Start Date: ")) {
			startDatePicker = datePicker;
		}
		if(str.equals("End Date: ")) {
			endDatePicker = datePicker;
		}
		
		datePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//try {
					//Graph_wrapper.updateGraph();
				//} catch (ParseException | IOException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				
			}
			
		});
		JPanel start_date_panel = new JPanel(new BorderLayout());
		start_date_panel.add(new JLabel(str),BorderLayout.NORTH);
		start_date_panel.add(datePicker,BorderLayout.SOUTH);
		return start_date_panel;
	}
	

	public DynamicList createDynamicList(Filter_value value) {
		//value.setArray(Data_processing.getPossibleValues(s, value));
		DynamicList dropdown = new DynamicList(value);
		JPanel outer = new JPanel(new BorderLayout());
		outer.add(new JLabel(value.getFilter()),BorderLayout.NORTH);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(dropdown.getList()));
		
		JPanel panel2 = new JPanel(new FlowLayout());
		panel2.add(dropdown.getTextField(),BorderLayout.WEST);
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dropdown.getList().clearSelection();
				
			}
			
		});
		panel2.add(reset);//,BorderLayout.EAST);
		panel.add(panel2,BorderLayout.SOUTH);
		
		//add(dropdown.getList(),vbox);
		outer.add(panel,BorderLayout.SOUTH);
		frame.add(outer);
		return dropdown;
	}


	@SuppressWarnings("unused")
	static void createAndShowGUI(Subset subset) {
		s =subset;
		JComponent newContentPane = new GUI();
	}
	
	public static void filterModelBasedOnOthers() throws ParseException, IOException {
		//System.out.println("filterModelBasedOnOthers");
		//s = Data_processing.updateGraphbyFilters(s);
		//System.out.println(s.items.size());
		for(DynamicList list : GUI.lists) {
			
			String filter = list.getFilter_value().getFilter();
			
			Subset subset = Data_processing.updateGraphbyFilters(s,filter);
			DefaultListModel<String> model3 = (DefaultListModel<String>) list.getList().getModel();
			//Object[] modelElements = model3.toArray();
			//for(Object element :  modelElements) {
				//if(!values.contains(element.toString())) {
				//modelCopy.add(element.toString());
				//}
			//}
			//list.getList().setSelectedIndices(Data_processing.toIntArray(indexes));
			//System.out.println("Current items in "+filter+": "+model3);
			//System.out.println("Items based on text input"+Data_processing.getPossibleValues(subset, filter));
			List<String> results = Data_processing.getPossibleValues(subset, filter);
			for(String result : results) {
				if(!model3.contains(result)) {
					//model3.addElement(result);
				}
			}
			
			Object[] modelElements = model3.toArray();
			for(Object element :  modelElements) {
				if(!results.contains(element.toString())) {
					//model3.removeElement(element);
				}
			}
			/*DefaultListModel<String> model = new DefaultListModel<>();
			for (String s : modelCopy) {
				model.addElement(s);
			}
			ArrayList<String> selected = new ArrayList<String>(list.getList().getSelectedValuesList());
			model.removeAllElements();
			List<String> values = Data_processing.getPossibleValues(subset, filter);
			List<Integer> indexes = new ArrayList<Integer>();
			//System.out.println(filter+" "+values);
			//System.out.println(model);
			for (int i=0; i<values.size(); i++) {
				if(selected.contains(values.get(i))){
					indexes.add(i);
				}
				if(model.contains(values.get(i))) {
					
				}
				if(!model.contains(values.get(i))) {
					model.addElement(values.get(i));
				}
				
			}
			List<String> modelCopy2 = new ArrayList<String>();
			Object[] modelElements2 = model.toArray();
			for(Object element :  modelElements2) {
				//if(!values.contains(element.toString())) {
				modelCopy2.add(element.toString());
				//}
			}
			


			DefaultListModel<String> model2 = new DefaultListModel<>();
			for (String s : modelCopy2) {
				model2.addElement(s);
			}
			//System.out.println(filter+": "+model2);
			//System.out.println(Data_processing.filterModel(model2, list.getFilter_value(), list.getTextField().getText()));
			
			DefaultListModel<String> model5 = Data_processing.filterModel(model2, list.getFilter_value(), list.getTextField().getText());
			Object[] filted_values = model5.toArray();
			//list.getList().setSelectedIndices(Data_processing.toIntArray(indexes));
			
			DefaultListModel<String> model4 = (DefaultListModel<String>) list.getList().getModel();
			
			for(Object element :  filted_values) {
				if(!model4.contains(element.toString())) {
					model4.addElement(element.toString());
				}
			}
			Object[] modelElements3 = model4.toArray();
			for(Object element : modelElements3) {
				if(!model4.contains(element.toString())) {
					model4.removeElement(element.toString());
				}
			}
			*/
		}
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(clearBtn)) {
			for(DynamicList list : lists) {
				list.getList().clearSelection();
				list.getTextField().setText("");
			}
		}
	}
}