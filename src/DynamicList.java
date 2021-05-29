import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DynamicList {
	String filter_type="";
	DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> jList; 
	private Filter_value value;
	private JTextField textfield;
	public static boolean lock = false;
	public DynamicList(Filter_value inc_value) {
		//System.out.println(inc_value.getArray());
		value = inc_value;
		//System.out.println("&"+value.getArray());
		jList=createJList();
		createTextField();
		//filterModel((DefaultListModel<String>)jList.getModel(), "");
		
	 }
	 
	 public JList<String> getList() {
		 return jList;
	 }
	 
	 public JTextField getTextField() {
		 return textfield;
	 }
	 
	 public Filter_value getFilter_value() {
		 return value;
	 }
	 
	 public void createTextField() {
		 final JTextField field = new JTextField(15);
		 field.getDocument().addDocumentListener(new DocumentListener(){
			 @Override public void insertUpdate(DocumentEvent e) { filter(); }
			 @Override public void removeUpdate(DocumentEvent e) { filter(); }
			 @Override public void changedUpdate(DocumentEvent e) {}
			 private void filter() {
				 String filter = field.getText();
				 //System.out.println("*"+model);
				 //System.out.println(jList.getSelectedValuesList());
				 Data_processing.filterModel(model, value, filter);
				 //System.out.println(jList.getSelectedValuesList());
				// System.out.println("&"+model);
			 }
		 });
		 textfield= field;
	}

	    
	 private JList<String> createJList() {
		 JList<String> list = new JList<String>(createDefaultListModel());
		 list.setVisibleRowCount(8);
		 //list.setSize(new java.awt.Dimension( 150 , 150 ));
		 list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && lock == false) {
					lock = true;
					//System.out.println("DOING STUFF TO LISTS");
					ArrayList<String> selected = new ArrayList<String>(list.getSelectedValuesList());
					//if(selected.contains("<None>")) {
					//	list.clearSelection();
						//list.setSelectedIndex(0);
					//}
					
					value.setValue(selected);
					//System.out.println(selected);
					try {
						
						//System.out.println("list update");
						GUI.filterModelBasedOnOthers();
						Graph_wrapper.updateGraph();
						
						
					} catch (ParseException | IOException e1) {
						e1.printStackTrace();
					}
					lock=false;
				}
			}
		 });
		 
		 return list;
	 }

	private ListModel<String> createDefaultListModel() {
			for (String s : value.getArray()) {
				model.addElement(s);
			}
		return model;
	}
}