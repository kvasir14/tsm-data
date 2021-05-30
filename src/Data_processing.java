import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;

public class Data_processing {
	public static String parseFixes(String str) {
		str = str.replace("\",", "");
		str = str.replace("\"", "");
		//System.out.println(str);
		if(str.indexOf(",")>0&&str.indexOf(".")>0) {
			if(str.substring(str.lastIndexOf(",")-10,str.lastIndexOf(",")).contains(".")){
				str=str.substring(0,str.lastIndexOf("."))+str.substring(str.lastIndexOf(","),str.length());
			}
		}
		return str;
	}
	
	public static void removeUnknownItems(ArrayList<Event> events) {
		Iterator<Event> i = events.iterator();
		while (i.hasNext()) {
			Event e = i.next(); // must be called before you can call i.remove()
			if(e.iid==0) {
				i.remove();
			}
		}
	}
	
	public String addSpaces(String str) {
		String spaces ="";
		for(int i=str.length(); i<50; i++) {
			spaces+=" ";
			
		}
		return spaces+"\t";
	}
	
	public static String addSpaces(int num) {
		String spaces ="";
		for(int i=num; i<50; i++) {
			spaces+=" ";
			
		}
		return spaces+"\t";
	}
	
	public static IDName getIDName(int id) {
		for(IDName element : Master.idnames) {
			if(element.id==id) {
				return element;
			}
		}
		return null;
	}
	
	public static IDName getIDNameFromName(String name) {
		for(IDName element : Master.idnames) {
			if(element.name.equals(name)) {
				return element;
			}
		}
		return null;
	}
	
	public static IDName getIDName(String id) {
		return getIDName(idToShortID(id));
	}
	
	public static IDName idToIDName(int id) throws IOException{
		IDName idname = getIDName(id);
		if(idname!=null) {
			return idname;
		}
		IDName newidname;
		switch(id) {
		case 0:
			newidname = new IDName(id,"UNKNOWN ITEM");
			Master.idnames.add(newidname);
			return newidname;
		case 1:
			newidname =  new IDName(id,"Postage");
			Master.idnames.add(newidname);
			return newidname;
		case 2:
			newidname =  new IDName(id,"Repair Bill");
			Master.idnames.add(newidname);
			return newidname;
		case 3:
			newidname =  new IDName(id,"Money Transfer");
			Master.idnames.add(newidname);
			return newidname;
		case 58:
			newidname =  new IDName(id,"p:58 UNKNOWN");
			Master.idnames.add(newidname);
			return newidname;
		}
		IDName new_idname = new IDName(0,"");
		try {
			System.out.println("API call for "+id);
			if(Master.api_token == null || Master.api_token.isEmpty() || Master.api_token.length()==0) {
				Master.api_token = api.get_token(Master.apikey);
			}
			org.json.JSONObject json = api.readJsonFromUrl("https://us.api.blizzard.com/data/wow/item/"+id+"?namespace=static-us&locale=en_US&access_token="+Master.api_token);
			
			//System.out.println(json.toString());
			//System.out.println(json.get("name"));
			String name = json.get("name").toString();
			new_idname = new IDName(id,name);
			System.out.println("*Added: "+id+", "+name);
			Master.idnames.add(new_idname);
			PrintWriter out = new PrintWriter(new FileOutputStream(new File("IDNames.txt"), true));
			out.println(id+", "+name);
			out.close();
		}
		catch (java.io.FileNotFoundException e) {
			System.out.println("ERROR ERROR ERROR "+ id);
		}
		
		return new_idname;
	}
	
	public static IDName idToIDName(String id) throws IOException{
		return idToIDName(idToShortID(id));
		
	}
	
	
	//returns a List<File> of all JSON files in a given folder
	public static List<File> listFilesForFolder(final File folder) {
		List<File> files = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else if(fileEntry.getName().endsWith(".json")){
			   files.add(fileEntry);
			  // System.out.println(fileEntry.getName())
			}
		}
		return files;
	}
	
	public static List<String> sortStringArayAsIntArray(List<String> results){
		//System.out.println("INCOMING: "+results);
		boolean containedNone = false;
		List<Integer> numbers = new ArrayList<Integer>();
		boolean isStringArray = false;
		for(int i=0; i<results.size(); i++) {
			if(results.get(i).equals("<None>")) {
				containedNone = true;
			}
			else if(isInteger(results.get(i))==false){
				isStringArray = true;
				break;
			}
			else {
				numbers.add(Integer.parseInt(results.get(i)));
			}
		}
		
		if(isStringArray) {
			if(containedNone) {
				//results.remove("<None>");	
			}
			
			Collections.sort(results);
			//results.add(0, "<None>");
			//System.out.println("OUTGOING1: "+results);
			return results;
		}
		
		//for(String str : results) {
		//	if(str.equals("<None>")) {
		//		containedNone = true;
		//	}
		//	else {
		//		numbers.add(Integer.parseInt(str));
		//	}
		//}
		Collections.sort(numbers);
		results.clear();
		if(containedNone) {
			//results.add("<None>");
		}
		for(int num : numbers) {
			results.add(Integer.toString(num));
		}
		//System.out.println("OUTGOING2: "+results);
		return results;
		
	}
	
	public static boolean isInteger(String s) {
		return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}
	
	public static int idToShortID(String id) {
		String shortID = id;
		if(shortID.contains(":")) {
			shortID=shortID.substring(2,shortID.length());
		}
		if(shortID.contains(":")) {
			shortID=shortID.substring(0,shortID.indexOf(":"));
		}
		if(id.equals("Items")) {
			shortID= "0";
		}
		if(id.equals("Postage")) {
			shortID= "1";
		}
		if(id.equals("Repair Bill")) {
			shortID="2";
		}
		if(id.equals("Money Transfer")) {
			shortID="3";
		}
		
		return Integer.parseInt(shortID);
	}
	
	public static String bonusIDS(String id) {
		String bonusIDs = "";
		if(id.contains("::")) {
			bonusIDs=id.substring(id.indexOf("::")+2,id.length());
		}
		
		return bonusIDs;
	}
	
	public static List<String> getPossibleValues(Subset s, String value){
		List<String> values = new ArrayList<String>();
		List<Integer> id_values = new ArrayList<Integer>();
		for(Item item : s.items) {
			for(Event event : item.events) {
				String element = "";
				switch(value.toLowerCase()) {
				case "account":
					element = event.account;
					break;
				case "character":
					element = event.character;
					break;
				case "realm":
				case "server":
					element = event.server;
					break;
				case "type":
					element = event.type;
					break;
				case "otherplayer":
					element = event.otherPlayer;
					break;
				case "name":
					element = event.name+" ("+event.iid+")";
					break;
				case "loc":
					element = event.loc;
					break;
				}
				if(!values.contains(element)) {
					values.add(element);
				}
			}
		}
		Collections.sort(values);
		if(value.equals("id")) {
			for(Item item : s.items) {
				for(Event event : item.events) {
					if(!id_values.contains(event.iid)) {
						id_values.add(event.iid);
					}
				}
			}
			Collections.sort(id_values);
			List<String> sNumbers = id_values.stream().map(
					n -> n.toString()).collect(Collectors.toList()
				);
			//sNumbers.add(0, "<None>");
			return (ArrayList<String>) sNumbers;
		}
		
		//values.add(0, "<None>");
		
		//System.out.println(values);
		return values;
	}
	
	public static Subset updateGraphbyFilters(Subset subset) throws ParseException, IOException{
		Subset s=subset;
		if(Graph.account.active()) {
			s=s.account(Graph.account.getValue());
		}
		//System.out.println(Graph.character.active());
		if(Graph.character.active()) {
			s=s.character(Graph.character.getValue());
		}
		if(Graph.realm.active()) {
			s=s.realm(Graph.realm.getValue());
		}
		if(Graph.type.active()) {
			s=s.type(Graph.type.getValue());
		}
		if(Graph.id.active()) {
			s=s.id(Graph.id.getValue());
		}
		
		if(Graph.otherPlayer.active()) {
			s=s.otherPlayer(Graph.otherPlayer.getValue());
			
		}
		if(Graph.name.active()) {
			//s=s.name(Graph.name.getValue());
			List<String> list = new ArrayList<String>();
			for(String str : Graph.name.getValue()) {
				list.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));
				//System.out.println(str.substring(str.indexOf("(")+1,str.indexOf(")")));
			}
			s=s.id(list);
		}
		if(Graph.loc.active()) {
			s=s.loc(Graph.loc.getValue());
		}
		Date startDate = (Date) GUI.startDatePicker.getModel().getValue();
		Date endDate = (Date) GUI.endDatePicker.getModel().getValue();
		//Master.mdy.parse(Master.mdy.format(datePicker.getModel().getValue())).getTime()
		//Master.mdy.parse(Master.mdy.format(datePicker.getModel().getValue())).getTime()
		if(startDate != null &&  endDate != null) {
			s=s.date(Master.mdy.parse(Master.mdy.format(startDate)).getTime(), Master.mdy.parse(Master.mdy.format(endDate)).getTime());
		}
		else if(startDate != null) {
			s=s.date(Master.mdy.parse(Master.mdy.format(startDate)).getTime(),new Date().getTime());
		}
		else if(endDate != null) {
			s=s.date(0l,Master.mdy.parse(Master.mdy.format(endDate)).getTime());
		}
		return s;
	}
	
	public static Subset updateGraphbyFilters(Subset subset, String filter) throws ParseException, IOException{
		Subset s=subset;
		if(Graph.account.active()&&!filter.equals("Account")) {
			s=s.account(Graph.account.getValue());
		}
		//System.out.println(Graph.character.active());
		if(Graph.character.active()&&!filter.equals("Character")) {
			s=s.character(Graph.character.getValue());
		}
		if(Graph.realm.active()&&!filter.equals("realm")) {
			s=s.realm(Graph.realm.getValue());
		}
		if(Graph.type.active()&&!filter.equals("Type")) {
			s=s.type(Graph.type.getValue());
		}
		if(Graph.otherPlayer.active()&&!filter.equals("otherPlayer")) {
			s=s.otherPlayer(Graph.otherPlayer.getValue());
		}
		if(Graph.id.active()&&!filter.equals("Id")) {
			s=s.id(Graph.id.getValue());
		}
		if(Graph.name.active()&&!filter.equals("Name")) {
			//s=s.name(Graph.name.getValue());
			List<String> list = new ArrayList<String>();
			for(String str : Graph.name.getValue()) {
				if(str.indexOf("(")  > 0 && str.indexOf(")") > 0){
					list.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));
				}
				System.out.println(str.substring(str.indexOf("(")+1,str.indexOf(")")));
			}
			s=s.id(list);
		}
		if(Graph.loc.active()&&!filter.equals("Loc")) {
			s=s.loc(Graph.loc.getValue());
		}
		Date startDate = (Date) GUI.startDatePicker.getModel().getValue();
		Date endDate = (Date) GUI.endDatePicker.getModel().getValue();
		//Master.mdy.parse(Master.mdy.format(datePicker.getModel().getValue())).getTime()
		//Master.mdy.parse(Master.mdy.format(datePicker.getModel().getValue())).getTime()
		if(startDate != null &&  endDate != null) {
			s=s.date(Master.mdy.parse(Master.mdy.format(startDate)).getTime(), Master.mdy.parse(Master.mdy.format(endDate)).getTime());
		}
		else if(startDate != null) {
			s=s.date(Master.mdy.parse(Master.mdy.format(startDate)).getTime(),new Date().getTime());
		}
		else if(endDate != null) {
			s=s.date(0l,Master.mdy.parse(Master.mdy.format(endDate)).getTime());
		}
		return s;
	}
	
	public static String stripAccents(String s) 
	{
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}
	
	public static DefaultListModel<String> filterModel(DefaultListModel<String> model, Filter_value value, String filter) {
		System.out.println("filterModel");
		//System.out.println(value.getFilter()+": "+model);
		if(filter.contains(";")) {
			List<String>results = new ArrayList<String>();
			List<String> filters =  Arrays.asList(filter.split(";"));
			for (String str : value.getArray()) {
				for(String oneFilterValue : filters) {
					oneFilterValue=oneFilterValue.trim().toLowerCase();
					if (Data_processing.stripAccents(str.toLowerCase()).equals(oneFilterValue)) {
						if (!results.contains(str)) {
							results.add(str);
						}
					} 
					
					if(str.endsWith(")") ) {
						if(Data_processing.stripAccents(str.toLowerCase().substring(0,str.indexOf("(")-1)).equals(oneFilterValue) ) {
							if (!results.contains(str) ) {
								results.add(str);
							}
						}
						
					}
				}
			}

			for(String result : results) {
				if(!model.contains(result)) {
					model.addElement(result);
				}
			}
			
			Object[] modelElements = model.toArray();
			for(Object element :  modelElements) {
				if(!results.contains(element.toString())) {
					model.removeElement(element);
				}
			}
		}
		else {
			//System.out.println(value.getFilter());
			for (String s : value.getArray()) {
				//System.out.print("*"+s);
				if (!Data_processing.stripAccents(s.toLowerCase()).contains(filter.toLowerCase())) {//|| s.equals("<None>")) {
					//System.out.println(model+" "+filter);
					if (model.contains(s)) {// && !s.equals("<None>")) {
						//System.out.println("REMOVE"+ s);
						model.removeElement(s);
					}
				} 
				else {
	
					if (!model.contains(s)) {
						//System.out.println("ADD"+ s);
						model.addElement(s);
					}
				}
			}
		}
		return null;
	}
	
	static int[] toIntArray(List<Integer> list){
		  int[] ret = new int[list.size()];
		  for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		  return ret;
		}
	
	public static String whatTypeEvent(String data) {
		if(data.contains("internalData@csvSales")) {
			return "Sale";
		}
		else if(data.contains("internalData@csvExpense")) {
			//return "Expense";
		}
		else if(data.contains("internalData@csvBuys")) {
			return "Purchase";
		}
		return null;
	}
	
	public static ArrayList<String> cleanArrayListToString(ArrayList<String> dir) {
		//System.out.println(dir);
		for(int i=0; i<dir.size(); i++) {
			dir.set(i, dir.get(i).trim().replace("[", "").replace("]", ""));
		}
		//System.out.println(dir);
		return dir;
	}
	
	public static double sum(List<Item> items, String variable) throws ParseException, IOException {
		double sum=0d;
		for(Item item : items) {
			for(Event event : item.events) {
				sum+=whichMemberVariable(variable,event);
			}
		}
		return sum;
		
	}
	
	public static double avg(List<Item> list, String variable) throws ParseException, IOException {
		return sum(list,variable)/sum(list,"quanity");
	}
	
	public static double min(List<Item> list, String variable) throws ParseException, IOException {
		double min=0d;
		for(int i=0; i<list.size(); i++) {
			for(int e=0; e<list.get(i).events.size(); e++) {
				double value = whichMemberVariable(variable,list.get(i).events.get(e));
				if(i==0&&e==0||value<min) {
					min=value;
				}
			}
		}
		return min;
	}
	
	public static double max(List<Item> items, String variable) throws ParseException, IOException {
		double max=0d;
		for(int i=0; i<items.size(); i++) {
			for(int e=0; e<items.get(i).events.size(); e++) {
				double value = whichMemberVariable(variable,items.get(i).events.get(e));
				if(i==0&&e==0||value>max) {
					max=value;
					//System.out.println(list.get(i).events.get(e).dtime);
					//System.out.println(list.get(i).events.get(e).ltime);
				}
			}
		}
		return max;
	}
	
	public static double whichMemberVariable(String string, Event e) throws IOException {
		switch(string.toLowerCase()) {
		case "quantity":
			return e.quantity;
		case "average":
		case "avggold":
		case "price":
			return e.priceGold;
		case "time":
			return e.dtime;
		case "goldearned":
		case "goldspent":
		case "gold":
				return e.priceGold*(long)e.quantity;
			//}
			//return 0l;
		}
		return -666d;
	}
	
	public static String formatGold(double gold) {
		return String.format("%.2f", gold);
	}


	public static void printEventArray(List<Event> events) {
		for(Event e : events) {
			System.out.println(e.data);
		}
	}
	
	public static Event createNewEvent(List<String> data) throws IOException {
		switch(data.get(2).toLowerCase()) {
			case "sale":
				return new Sale(data);
			case "expense":
				return new Expense(data);
			case "purchase":
				return new Purchase(data);
		}
		System.out.println("Returned null");
		return null;
	}
}
