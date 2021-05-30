import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Item {
	IDName idname;  
	List<Event> events = new ArrayList<Event>();
	static int count = 0;
	
	public Item(IDName inc_idname) throws IOException{
		count++;
		idname = inc_idname;
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
	
	public void addEvent(Event e) {
		events.add(e);
	}
}
