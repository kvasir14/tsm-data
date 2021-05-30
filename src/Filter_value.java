import java.util.ArrayList;
import java.util.List;

/**
 * Impements filtering functionality for GUI
 */
public class Filter_value {
	//#region properties
	private List<String> array = new ArrayList<String>();
	private String filter="";
	private ArrayList<String> value= new ArrayList<String>();
	private int index=0;
	//#endregion

	//#region constructors
	public Filter_value(String str) {
		filter=str;
	}
	//#endregion
	
	//#region methods
	/** 
	 * @param inc_index
	 */
	public void setIndex(int inc_index) {
		index=inc_index;
	}

	/** 
	 * @return int
	 */
	public int getIndex() {
		return index;
	}
	
	/** 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getValue() {
		return value;
	}
	
	/** 
	 * @param str
	 */
	public void setValue(ArrayList<String> str) {
		//System.out.println(filter+" "+str);
		value=str;
	}

	/** 
	 * @return String
	 */
	public String getFilter() {
		return filter;
	}

	/** 
	 * @param values
	 */
	public void setArray(List<String> values) {
		array=values;
	}

	/** 
	 * @return List<String>
	 */
	public List<String> getArray() {
		return array;
	}

	/** 
	 * @return boolean
	 */
	public boolean active() {
		if(value.size()==1) {
			//return !value.get(0).equals("<None>");
		}
		return !value.equals(null) && value.size() > 0;
	}
	//#endregion
}
