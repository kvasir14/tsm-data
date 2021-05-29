import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Item {
	IDName idname;  
	List<Event> events = new ArrayList<Event>();
	static int count = 0;
	
	public Item(IDName inc_idname) throws IOException{
		count++;
		//System.out.println("Item Constructor");
		idname = inc_idname;
	}
	
	public Item(IDName inc_idname, List<Event> new_events) throws IOException{
		count++;
		//System.out.println("Item Constructor");
		idname = inc_idname;
		events = new_events;
	}
	
	public Item(int id, Subset s) throws IOException{
		count++;
		//System.out.println(s.items.size());
		//System.out.println("Item Constructor");
		idname = Data_processing.idToIDName(id);
		//System.out.println(s.items);
		events = s.items.get(0).events;
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
}
