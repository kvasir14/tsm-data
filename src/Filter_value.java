import java.util.ArrayList;
import java.util.List;

public class Filter_value {
	private List<String> array = new ArrayList<String>();
	private String filter="";
	private ArrayList<String> value= new ArrayList<String>();
	private int index=0;
	
	public Filter_value(String str) {
		//setArray(Data_processing.getPossibleValues(s, str));
		filter=str;
	}
	
	public void setIndex(int inc_index) {
		index=inc_index;
	}
	
	public int getIndex() {
		return index;
	}

	public ArrayList<String> getValue() {
		return value;
	}

	public void setValue(ArrayList<String> str) {
		//System.out.println(filter+" "+str);
		value=str;
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setArray(List<String> values) {
		array=values;
	}
	
	public List<String> getArray() {
		return array;
	}
	
	public boolean active() {
		//System.out.println(value+" "+(!value.equals(null) && !value.equals("") && !value.equals("<None>")));
		if(value.size()==1) {
			//return !value.get(0).equals("<None>");
		}
		return !value.equals(null) && value.size() > 0;
	}
}
