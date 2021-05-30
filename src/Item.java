import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * represents an item. Has an associated IDName pair and associated events.
 */
public class Item {
	//#region properties
	IDName idname;  
	List<Event> events = new ArrayList<Event>();
	static int count = 0;
	//#endregion
	
	//#region constructors
	public Item(IDName idname) throws IOException{
		count++;
		this.idname = idname;
	}
	
	public Item(IDName inc_idname, List<Event> new_events) throws IOException{
		count++;
		idname = inc_idname;
		events = new_events;
	}
	
	public Item(int id, Subset s) throws IOException{
		count++;
		idname = Data_processing.idToIDName(id);
		events = s.items.get(0).events;
	}
	//#endregion

	//#region methods
	/** 
	 * @param e Event
	 */
	public void addEvent(Event e) {
		events.add(e);
	}
	//#endregion
}
