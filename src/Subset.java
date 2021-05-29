import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Subset {
		static int count = 0;
		List<Item> items = new ArrayList<Item>();
		
		public Subset() {
			count++;
			items = Master.allItems;
		}
		
		public Subset(List<Item> list) {
			count++;
			items = list;
		}
		
		public Subset(Subset s) {
			count++;
			items=s.items;
		}
		
		public Subset id(int id) throws ParseException, IOException {
			List<Item> new_items = Filter.byID(items, id);
			return new Subset(new_items);
		}
		
		public Subset id(String fullID) throws ParseException, IOException {
			List<Item> new_items = Filter.byID(items, fullID);
			return new Subset(new_items);
		}
		
		public Subset id(List<String> ids) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : ids) {
				s = new Subset(Filter.combine(s.items,Filter.byID(items, Integer.parseInt(str))));
			}
			return s;
		}
		
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
		
		public Subset character(ArrayList<String> characters) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			//System.out.println(characters);
			for(String str : characters) {
				s = new Subset(Filter.combine(s.items,Filter.byCharacter(items, str)));
			}
			//System.out.println(s.items.size());
			return s;
		}
		
		public Subset realm(String realm) throws ParseException, IOException {
			List<Item> new_items = Filter.byRealm(items, realm);
			return new Subset(new_items);
		}
		
		public Subset realm(List<String> realms) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : realms) {
				s = new Subset(Filter.combine(s.items,Filter.byRealm(items, str)));
			}
			return s;
		}
		
		public Subset otherPlayer(String otherPlayer) throws ParseException, IOException {
			List<Item> new_items = Filter.byOtherPlayer(items, otherPlayer);
			return new Subset(new_items);
		}
		
		public Subset otherPlayer(List<String> otherPlayers) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : otherPlayers) {
				s = new Subset(Filter.combine(s.items,Filter.byOtherPlayer(items, str)));
			}
			return s;
		}
		
		public Subset account(String account) throws ParseException, IOException {
			List<Item> new_items = Filter.byAccount(items, account);
			return new Subset(new_items);
		}
		
		public Subset account(List<String> accounts) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : accounts) {
				s = new Subset(Filter.combine(s.items,Filter.byAccount(items, str)));
			}
			return s;
		}
		
		public Subset name(String name) throws ParseException, IOException {
			List<Item> new_items = Filter.byName(items, name);
			return new Subset(new_items);
		}
		
		public Subset nameExact(String name) throws ParseException, IOException {
			List<Item> new_items = Filter.byNameExact(items, name);
			return new Subset(new_items);
		}
		
		public Subset name(List<String> names) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : names) {
				s = new Subset(Filter.combine(s.items,Filter.byName(items, str)));
			}
			return s;
		}
		
		public Subset nameExact(List<String> names) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : names) {
				s = new Subset(Filter.combine(s.items,Filter.byNameExact(items, str)));
			}
			return s;
		}
		
		public Subset loc(String loc) throws ParseException, IOException {
			List<Item> new_items = Filter.byLoc(items, loc);
			return new Subset(new_items);
		}
		
		public Subset loc(ArrayList<String> locs) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : locs) {
				s = new Subset(Filter.combine(s.items,Filter.byLoc(items, str)));
			}
			return s;
		}
		
		public Subset date(Long start, Long end) throws ParseException, IOException {
			List<Item> new_items = Filter.byDate(items, start, end);
			return new Subset(new_items);
		}
		
		public Subset date(Long start) throws ParseException, IOException {
			List<Item> new_items = Filter.byDate(items, start, start+60*60*24*1000);
			return new Subset(new_items);
		}
		
		//public Subset date(double start, double end) throws ParseException, IOException {
		//	ArrayList<Item> new_items = Filter.byDate(items, (long) start*1000l, (long)end*1000l);
		//	return new Subset(new_items);
		//}
		
		public Subset type(String type) throws ParseException, IOException {
			List<Item> new_items = Filter.byType(items, type);
			return new Subset(new_items);
		}
		
		public Subset type(List<String> types) throws ParseException, IOException {
			Subset s = new Subset(new ArrayList<Item>());
			for(String str : types) {
				s = new Subset(Filter.combine(s.items,Filter.byType(items, str)));
			}
			return s;
		}
		
		public Subset exclude(List<Item> b){
			List<Item> new_items = new ArrayList<Item>();
			for(Item itemb : b) {
				boolean containsItem = false;
				for(Item itema : items) {
					if(itema.idname.id==itemb.idname.id) {
						containsItem=true;
						//for(Event eventa : itema.events) {
							for(Event eventb : itemb.events) {
								if(itema.events.contains(eventb)) {
									
								}
								else {
									itema.events.add(eventb);
								}
								
							//}
						}
						containsItem=true;
					}
				}
				if(!containsItem) {
					new_items.add(itemb);
				}
			}
			//System.out.println(len+" "+b.size()+" "+new_items.size());
			return this;
		}
		
		
}
