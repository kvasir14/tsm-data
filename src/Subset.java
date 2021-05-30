import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Subset object represents a subset of all possible items and events.
 * 
 * This is represented by a List<Item>, Item containing a List<Event>. Both lists can be filtered to form the resulting Subset Object
 */
public class Subset {
	//#region properties
	static int count = 0; //total number of subsets; can't remember purpose off the top of my head
	List<Item> items = new ArrayList<Item>();
	//#endregion

	//#region constructors
	/**
	 * default constructor
	 * copies master list of all items
	 */
	public Subset() {
		count++;
		this.items = Master.allItems;
	}
	
	/**
	 * 
	 * @param list
	 */
	public Subset(List<Item> items) {
		count++;
		this.items = items;
	}
	
	/**
	 * copy constructor
	 * @param s
	 */
	public Subset(Subset s) {
		count++;
		this.items=s.items;
	}
	//#endregion

	//#region methods
	/** 
	 * @param id
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset id(int id) throws ParseException, IOException {
		List<Item> new_items = Filter.byID(items, id);
		return new Subset(new_items);
	}

	/** 
	 * @param fullID
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset id(String fullID) throws ParseException, IOException {
		List<Item> new_items = Filter.byID(items, fullID);
		return new Subset(new_items);
	}

	/** 
	 * @param ids
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset id(List<String> ids) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : ids) {
			s = new Subset(Filter.combine(s.items,Filter.byID(items, Integer.parseInt(str))));
		}
		return s;
	}

	/** 
	 * @param character
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset character(String character) throws ParseException, IOException {
		List<Item> new_items = items;
		if(character.contains("-")) {
			String toon = character.substring(0, character.indexOf("-"));
			String realm = character.substring(character.indexOf("-")+1,character.length());
			new_items = Filter.byCharacter(new_items, toon);
			new_items = Filter.byRealm(new_items, realm);
		}
		else {
			new_items = Filter.byCharacter(new_items, character);
		}
		return new Subset(new_items);
	}

	/** 
	 * @param characters
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset character(ArrayList<String> characters) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		//System.out.println(characters);
		for(String str : characters) {
			s = new Subset(Filter.combine(s.items,Filter.byCharacter(items, str)));
		}
		//System.out.println(s.items.size());
		return s;
	}

	/** 
	 * @param realm
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset realm(String realm) throws ParseException, IOException {
		List<Item> new_items = Filter.byRealm(items, realm);
		return new Subset(new_items);
	}

	/** 
	 * @param realms
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset realm(List<String> realms) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : realms) {
			s = new Subset(Filter.combine(s.items,Filter.byRealm(items, str)));
		}
		return s;
	}

	/** 
	 * @param otherPlayer
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset otherPlayer(String otherPlayer) throws ParseException, IOException {
		List<Item> new_items = Filter.byOtherPlayer(items, otherPlayer);
		return new Subset(new_items);
	}

	/** 
	 * @param otherPlayers
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset otherPlayer(List<String> otherPlayers) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : otherPlayers) {
			s = new Subset(Filter.combine(s.items,Filter.byOtherPlayer(items, str)));
		}
		return s;
	}

	/** 
	 * @param account
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset account(String account) throws ParseException, IOException {
		List<Item> new_items = Filter.byAccount(items, account);
		return new Subset(new_items);
	}

	/** 
	 * @param accounts
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset account(List<String> accounts) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : accounts) {
			s = new Subset(Filter.combine(s.items,Filter.byAccount(items, str)));
		}
		return s;
	}

	/** 
	 * @param name
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset name(String name) throws ParseException, IOException {
		List<Item> new_items = Filter.byName(items, name);
		return new Subset(new_items);
	}

	/** 
	 * @param name
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset nameExact(String name) throws ParseException, IOException {
		List<Item> new_items = Filter.byNameExact(items, name);
		return new Subset(new_items);
	}

	/** 
	 * @param names
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset name(List<String> names) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : names) {
			s = new Subset(Filter.combine(s.items,Filter.byName(items, str)));
		}
		return s;
	}

	/** 
	 * @param names
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset nameExact(List<String> names) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : names) {
			s = new Subset(Filter.combine(s.items,Filter.byNameExact(items, str)));
		}
		return s;
	}

	/** 
	 * @param loc
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset loc(String loc) throws ParseException, IOException {
		List<Item> new_items = Filter.byLoc(items, loc);
		return new Subset(new_items);
	}

	/** 
	 * @param locs
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset loc(ArrayList<String> locs) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : locs) {
			s = new Subset(Filter.combine(s.items,Filter.byLoc(items, str)));
		}
		return s;
	}

	/** 
	 * @param start
	 * @param end
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset date(Long start, Long end) throws ParseException, IOException {
		List<Item> new_items = Filter.byDate(items, start, end);
		return new Subset(new_items);
	}

	/** 
	 * @param start
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset date(Long start) throws ParseException, IOException {
		List<Item> new_items = Filter.byDate(items, start, start+60*60*24*1000);
		return new Subset(new_items);
	}

	/** 
	 * @return Subset
	 */
	//public Subset date(double start, double end) throws ParseException, IOException {
	//	ArrayList<Item> new_items = Filter.byDate(items, (long) start*1000l, (long)end*1000l);
	//	return new Subset(new_items);
	//}
	
	public Subset type(String type) throws ParseException, IOException {
		List<Item> new_items = Filter.byType(items, type);
		return new Subset(new_items);
	}

	/** 
	 * @param types
	 * @return Subset
	 * @throws ParseException
	 * @throws IOException
	 */
	public Subset type(List<String> types) throws ParseException, IOException {
		Subset s = new Subset(new ArrayList<Item>());
		for(String str : types) {
			s = new Subset(Filter.combine(s.items,Filter.byType(items, str)));
		}
		return s;
	}

	/** 
	 * @param b
	 * @return Subset
	 */
	public Subset exclude(List<Item> b){
		List<Item> new_items = new ArrayList<Item>();
		for(Item item_b : b) {
			boolean containsItem = false;
			for(Item item_a : items) {
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
		//System.out.println(len+" "+b.size()+" "+new_items.size());
		return this;
	}
	//#endregion
}
