import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * Implements filtering of List<Item> based on several different fields.
 */
public class Filter {
	//#region methods
	/** 
	 * @param items
	 * @param start
	 * @param end
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byDate(List<Item> items, Long start, Long end) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			ArrayList<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(end>e.ltime&&start<e.ltime) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param items2
	 * @return List<Item>
	 */
	public static List<Item> combine(List<Item> items, List<Item> items2){
		List<Item> new_items = items;
		for(Item item_b : items2) {
			boolean containsItem = false;
			for(Item item_a : new_items) {
				if(item_a.idname.id==item_b.idname.id) {
					containsItem=true;
					for(Event eventb : item_b.events) {
						if(!item_a.events.contains(eventb)) {
							item_a.events.add(eventb);
						}
					}
				}
			}
			if(!containsItem) {
				new_items.add(item_b);
			}
		}
		return new_items;
	}
	
	/** 
	 * @param a
	 * @param b
	 * @return Subset
	 */
	public static Subset combine(Subset a, Subset b) {
		Subset s = new Subset(combine(a.items,b.items));
		return s;
	}
	
	/** 
	 * @param new_items2
	 * @param character
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byCharacter(List<Item> new_items2, String character) throws ParseException, IOException {
		ArrayList<Item> new_items = new ArrayList<Item>();
		for(Item item : new_items2) {
			ArrayList<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.character.equals(character)) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param new_items2
	 * @param otherPlayer
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byOtherPlayer(List<Item> new_items2, String otherPlayer) throws ParseException, IOException {
		ArrayList<Item> new_items = new ArrayList<Item>();
		for(Item item : new_items2) {
			ArrayList<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.otherPlayer.equals(otherPlayer)) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param realm
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byRealm(List<Item> items, String realm) throws ParseException, IOException {
		ArrayList<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			ArrayList<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.server.equals(realm)) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param account
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byAccount(List<Item> items, String account) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				//System.out.println(e.account+"  "+account);
				if(e.account.equals(account)||e.account.startsWith(account)) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param id
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byID(List<Item> items, int id) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(Data_processing.idToShortID(e.id) == id) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		//System.out.println(new_items);
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param fullID
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byID(List<Item> items, String fullID) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(Data_processing.idToShortID(e.id) == Data_processing.idToShortID(fullID) && e.bonusIDs.contains(Data_processing.bonusIDS(fullID))) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param name
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byName(List<Item> items, String name) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.name.equals(name) ||e.name.startsWith(name) || e.name.indexOf(name) >= 0 || e.name.toLowerCase().equals(name.toLowerCase())) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param otherPlayer
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byOtherPlayerExact(List<Item> items, String otherPlayer) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.name.equals(otherPlayer) || e.name.toLowerCase().equals(otherPlayer.toLowerCase())) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param name
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byNameExact(List<Item> items, String name) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.name.equals(name) || e.name.toLowerCase().equals(name.toLowerCase())) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	
	/** 
	 * @param items
	 * @param type
	 * @return List<Item>
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Item> byType(List<Item> items, String type) throws ParseException, IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			List<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.type.toLowerCase().equals(type.toLowerCase())) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}

	/** 
	 * @param items
	 * @param location
	 * @return List<Item>
	 * @throws IOException
	 */
	public static List<Item> byLoc(List<Item> items, String location) throws IOException {
		List<Item> new_items = new ArrayList<Item>();
		for(Item item : items) {
			ArrayList<Event> new_events = new ArrayList<Event>();
			for(Event e : item.events) {
				if(e.loc.equals(location)) {
					new_events.add(e);
				}
			}
			if(new_events.size() != 0) {
				new_items.add(new Item(item.idname,new_events));
			}
		}
		return new_items;
	}
	//#endregion
}
