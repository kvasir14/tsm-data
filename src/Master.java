import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Master {
	static List<Event> allEvents = new ArrayList<Event>();
	static List<String> eventsPerDay = new ArrayList<String>();
	static List<Item> allItems = new ArrayList<Item>();
	static List<IDName> idnames = new ArrayList<IDName>();
	static List<String> directories = new ArrayList<String>();
	static List<String> usefulFiles = new ArrayList<String>();
	static List<ArrayList<Subset> > subsets = new ArrayList<ArrayList<Subset> >();
	static HashSet<String> eventHash = new HashSet<>();
	static DateFormat hm_md = new SimpleDateFormat("HH:mm M/d");
	static DateFormat h_d = new SimpleDateFormat("HH d");
	static DateFormat mdy = new SimpleDateFormat("M/d/yy");
	static DateFormat md = new SimpleDateFormat("M/d");
	static DateFormat YYYYMMDD = new SimpleDateFormat("YYYYMMd");
	static DateFormat M = new SimpleDateFormat("M");
	static DateFormat yyyy = new SimpleDateFormat("yyyy");
	static DateFormat d = new SimpleDateFormat("d");
	static List<Integer> gpd = new ArrayList<Integer>();
	static String apikey; //blizzard API key
	static List<String> Enchantingitems = new ArrayList<String>();

	
	static String api_token;
	static boolean Read_From_TSM_Files = true;
	static long Min_Date = 0;
	static boolean Read_In_New_Vendor_Events = false;
	
	static boolean Use_Events_All_Save_File = false;
	static boolean Use_Events_Vendor_Save_File = false;
	static boolean Use_Events_Auction_Save_File = true;
	static boolean Use_Events_Trade_Save_File = true;
	static boolean Use_Events_COD_Save_File = true;
	
	
	public Master() throws IOException, ParseException, URISyntaxException {
		getSettings();
		one();
	}
	
	public static void one() throws IOException, URISyntaxException, ParseException {
		//callback to two()
		api.getapikey(); 
		
	}
	
	public static void two() throws IOException, ParseException {
		//callback to three
		FileChooser.checkForSavedDirectories();
		
		//three();
	}
	
	public static void three() throws IOException, ParseException {
		//System.out.println(Master.directories);
		//arbitrage();
		
		init();
	}
	
	public static void init() throws IOException, ParseException {
		System.out.println("init");
		
		//getUsefulDirectories();
		//getFullDirectories();
		
		
		getSavedEvents(Min_Date);
		
		if(Read_From_TSM_Files) {
			readFiles(Min_Date);
		}
		//readFiles(new Date(mdy.parse(Master.mdy.format(new Date())).getTime()-(1000*60*60*24*29L)).getTime());
		Collections.sort(allEvents);

		IDNamesFileOUT();
		long interval = 1000*60*60*24;
		Subset s = new Subset()
				//.date(Min_Date)
				//.type("Sale")
				//.type("Purchase")
				//.loc("Auction")
				//.name("Nylon Thread")
				//.name("Enchant")
				.realm("Thrall")
				//.name("Darkmoon Deck")
				//.account("81892423#2")
				//.account("369914998#1")
				;
		
		Subset s_alltypes = s;
		s = Filter.combine(s.loc("Auction"), Filter.combine(s.loc("COD"), s.loc("Trade")));
		//s = Filter.combine(s, s_alltypes.name("Enchanter's Umbral Wand"));
		//System.out.println(mdy.parse("10/24/18").getTime());
		//s=s.date(mdy.parse("1/12/19").getTime());
		
		//perDayGoldSummary(s);
		
		writeSavedEvents(new Subset());	
		
		//SoHprofit(s);
		
		
		//flipProfit(s);
		//s=s.date(mdy.parse("11/7/19").getTime(),mdy.parse(mdy.format(new Date())).getTime()+1000*60*60*24);
		s=s.date(mdy.parse("1/30/20").getTime(),mdy.parse(mdy.format(new Date())).getTime()+1000*60*60*24);

		//s=s.date(mdy.parse(mdy.format(new Date())).getTime()-1000*60*60*24*3,mdy.parse(mdy.format(new Date())).getTime()+1000*60*60*24);
	
		writePerDayData(s.type("Sale"),"Sales.txt");
		writePerDayData(s.type("Purchase"),"Purchases.txt");
		
		accounting(s);
		/*Subset a = s.nameExact(Enchantingitems);
		a = Filter.combine(a, s.nameExact("Tempest Hide"));
		a = Filter.combine(a, s.nameExact("Mistscale"));
		
		a = Filter.combine(a, s.nameExact("Crimson Ink"));
		a = Filter.combine(a, s.nameExact("Crimson Pigment"));
		a = Filter.combine(a, s.nameExact("Viridescent Ink"));
		a = Filter.combine(a, s.nameExact("Viridescent Pigment"));
		a = Filter.combine(a, s.name("War-Scroll"));
		//a = Filter.combine(a, s.name("Highborne Comp"));
		Subset herbs = new Subset(Filter.combine(s.nameExact("Akunda's Bite"), Filter.combine(s.nameExact("Riverbud"), Filter.combine(s.nameExact("Sea Stalk"), Filter.combine(s.nameExact("Siren's Pollen"), Filter.combine(s.nameExact("Star Moss"), s.nameExact("Winter's Kiss")))))).items);
		a = Filter.combine(a, herbs);*/

		
		//a = Filter.combine(a.loc("Auction"), Filter.combine(a.loc("COD"), a.loc("Trade")));
		
		writeSavedEvents("Accounting-Events.txt", s);	
		//comment out the line below to disable the graph
		Graph_wrapper.init(interval, s);
		
		
		//enchantProfit(s);
		
      //  for(DynamicList list : GUI.lists) {
       // 	list.getTextField().setText("1");
       // 	list.getTextField().setText("");
       // }
        //Time_series.init(s);
		
		System.out.println("Total Auction profit: "+String.format("%.2f", Data_processing.sum(s.loc("Auction").items,"goldearned")));
	}
	
	

	private static void getUsefulDirectories() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("which-files.txt"));
		for(int i=0;scan.hasNext();i++) {
			String str = scan.nextLine();
			usefulFiles.add(str);
			directories.add(str);
		}
		
	}

	 //not for public use. This is how I calculate my expenses/profit for whatever I'm currently doing professions wise.
	private static void accounting(Subset s) throws ParseException, IOException {
		
		s=s.realm("Thrall");
		//s=s.date(mdy.parse("12/21/20").getTime(),new Date().getTime()+1000*60*60*24);
		s=s.date(mdy.parse(mdy.format(new Date())).getTime()-1000*60*60*24*2,mdy.parse(mdy.format(new Date())).getTime()+1000*60*60*24);
		Accounting blank = new Accounting("",s);
		
		String one = "%-40s";
		String two = "%14s";
		
		double cost = 0;
		double sales = 0;
		double buy = 0;
		
		double cost_total = 0;
		double sales_total = 0;
		double buy_total = 0;
		
		

		Enchantingitems =
				new ArrayList<String>(Arrays.asList(
						"Boneshatter Armguards",
						"Boneshatter Gauntlets",
						"Boneshatter Greaves",
						"Boneshatter Helm",
						"Boneshatter Pauldrons",
						"Boneshatter Treads",
						"Boneshatter Vest",
						"Boneshatter Waistguard",
						"Umbrahide Armguards",
						"Umbrahide Gauntlets",
						"Umbrahide Helm",
						"Umbrahide Leggings",
						"Umbrahide Pauldrons",
						"Umbrahide Treads",
						"Umbrahide Vest",
						"Umbrahide Waistguard"
				
				));
		printheadings();
		for(String name : Enchantingitems) {
			
			if(name.equals("")||name.startsWith("--")) {
				if(name.contentEquals("--HEADINGS")) {
					System.out.println();
					printheadings();
				}
				else {
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.println(
							String.format(one, "")+
							String.format(two, "")+
							String.format(two, "")+
							String.format(two, "")+
							String.format(two, "")+
							String.format(two, String.format("%.2f", buy_total))+
							String.format(two, "")+
							String.format(two, "")+
							String.format(two, "")+
							String.format(two, String.format("%.2f", sales_total))+
							//"  |  "+
							String.format(two, "")+
							String.format(two, String.format("%.2f", sales_total - buy_total))+
							String.format(two, String.format("%.2f", (sales_total - buy_total)/buy_total))
							);
				}
				
				//printfooter(cost, sales, buy);
				
				cost=0;
				sales=0;
				buy=0;
			}
			else {
				Accounting item = new Accounting(name,s);
				//if(name.equals("Nylon Thread")) {
						System.out.println(
							String.format(one, name)+
							String.format(two, String.format("%.2f", item.crafting()))+
							String.format(two, String.format("%.2f", item.quantitysell-item.quantitybuy))+
							String.format(two, String.format("%.2f", item.avgbuy))+
							String.format(two, String.format("%.2f", item.quantitybuy))+
							String.format(two, String.format("%.2f", item.totalbuy))+
							
							String.format(two, String.format("%.2f", (item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy))))+
							
							String.format(two, String.format("%.2f", item.avgsell))+
							String.format(two, String.format("%.2f", item.quantitysell))+
							String.format(two, String.format("%.2f", item.totalsell))+
							
							String.format(two, String.format("%.2f", (item.totalsell-(item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy)))/item.quantitysell))+
							String.format(two, String.format("%.2f", (item.totalsell-(item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy)))))+
							String.format(two, String.format("%.2f", ((item.totalsell-(item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy)))/(item.totalbuy + (item.crafting()*(item.quantitysell-item.quantitybuy))))))
						);
					cost += item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy);
					sales += item.totalsell;
					buy += item.totalbuy;
					//cost_total += item.totalbuy + item.crafting()*(item.quantitysell-item.quantitybuy);
					//sales_total += item.totalsell;
					//buy_total += item.totalbuy;
				//}
					
			}
			
			
		}
		//printfooter(cost, sales, buy);
		System.out.println();
		//printfooter(cost_total, sales_total, buy_total);
	}
	
	//not for public use
	static void printheadings() {
		String one = "%-40s";
		String two = "%14s";
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println(
				String.format(one, "Name")+
				String.format(two, "Crafting")+
				String.format(two, "Crafted Quan")+
				String.format(two, "Avg Buy")+
				String.format(two, "Quantity Buy")+
				String.format(two, "Total Buy")+
				String.format(two, "Total Cost")+
				String.format(two, "Avg Sell")+
				String.format(two, "Quantity Sell")+
				String.format(two, "Total Sell")+
				//"  |  "+
				String.format(two, "Avg Profit")+
				String.format(two, "Total Profit")+
				String.format(two, "ROI")
				);
	}
	
	//not for public use
	static void printfooter(double cost, double sales, double buy) {
		String one = "%-40s";
		String two = "%14s";
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println(
				String.format(one, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, String.format("%.2f", buy))+
				
				String.format(two, "")+
				String.format(two, String.format("%.2f", sales))+
				//"  |  "+
				String.format(two, "")+
				String.format(two, String.format("%.2f", sales-cost))+
				String.format(two, String.format("%.2f", (sales-cost)/(cost)))
				);
		/*System.out.println(
				String.format(one, "")+
				String.format(two, String.format("%.2f", cost))+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, "")+
				String.format(two, String.format("%.2f", profit))+
				//String.format(two, String.format("%.2f", profit-cost))+
				//String.format(two, String.format("%.2f", (profit-cost)/cost))+
				//"  |  "+
				String.format(two, String.format("%.2f", buy))+
				String.format(two, "")+
				String.format(two, String.format("%.2f", profit-cost-buy))+
				String.format(two, String.format("%.2f", (profit-cost-buy)/(cost+buy)))
				);*/
	}

	//not for public use
	public static void arbitrage() throws JSONException, IOException {
		PrintWriter out = new PrintWriter(new File("ah-data.txt"));
		if(Master.api_token == null || Master.api_token.isEmpty() || Master.api_token.length()==0) {
			Master.api_token = api.get_token(Master.apikey);
		}
		if(true) {
			Scanner realmScanner = new Scanner(new File("realms.txt"));
			while(realmScanner.hasNext()) {
				String realm = realmScanner.nextLine();
				
				out.println(getAHdata(realm).replaceAll("\\s{2,}", " "));
			}
			out.close();
			System.out.println("Done");
			System.exit(0);
		}
		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		//parseArbitrageData(154165);
		//parseArbitrageData(154722);
		//parseArbitrageData(152576);
		out.close();
		
	}

	//not for public use. But useful if you want to download all of the current JSON files from the wow api
	public static String getAHdata(String realm) throws JSONException, IOException{
		org.json.JSONObject json = api.readJsonFromUrl("https://us.api.blizzard.com/data/wow/connected-realm/"+"1236"+"/auctions?namespace=dynamic-us&locale=en_US&access_token="+Master.api_token);
		File jsonFile= new File("json/"+realm+"-"+((JSONObject) json.getJSONArray("files").get(0)).get("lastModified").toString()+".json");
		System.out.print(String.format("%-20s", realm)+" "+(new Date().getTime()-new Date(Long.parseLong(((JSONObject)json.getJSONArray("files").get(0)).get("lastModified").toString())).getTime())/1000/60);
		String url = ((JSONObject) json.getJSONArray("files").get(0)).get("url").toString();
		//System.out.print(" "+((JSONObject) json.getJSONArray("files").get(0)).get("lastModified").toString());
		if(!jsonFile.exists()) {
			//System.out.print(" Downloading");
			
			//api.readJsonStream(url);
			//donwloadFile(url,jsonFile);
			//org.json.JSONObject json2 = api.readJsonFromUrl("http://auction-api-us.worldofwarcraft.com/auction-data/49e985b0adca27a885b4f84d99355da6/auctions.json");
			
			//PrintWriter jsonFileout = new PrintWriter(jsonFile);
			//jsonFileout.println(json2);
		//	jsonFileout.close();
			
		}
		else {
		//	System.out.print(" Exists");
		}
		//System.out.print(" done");
		System.out.println();
		return url;
	}
	
	//not for public use
	private static void donwloadFile(String urlStr, File dstFile) {
		if (!dstFile.exists()) {
		 //  dstFile.mkdirs();
		}
		try {
		    URL url = new URL(urlStr);
		    FileUtils.copyURLToFile(url, dstFile);
		} catch (Exception e) {
		    System.err.println(e);
		    //VeBLogger.getInstance().log( e.getMessage());
		}
		
	}

	
	//////////////////////////////////////////////////////////
	
	
	
	
	  private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }

		  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
			  url="https://graph.facebook.com/19292868552";
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
		  }
	
	
	////////////////////////////////////////////////////////

	//reads settings.txt and initializes
	private void getSettings() throws ParseException, FileNotFoundException {
		File settingsFile =new File("settings.txt");
		if(settingsFile.exists()) {
			Scanner scan = new Scanner(settingsFile);
			while(scan.hasNext()) {
				String line = scan.nextLine();
				
				if(!line.startsWith("#")) {
					
					if(line.contains("Read_From_TSM_Files")) {
						if(line.toLowerCase().contains("false")) {
							Read_From_TSM_Files = false;
						}
					}
					if(line.contains("Min_Date")) {
						String datestr = "";
						int count = line.length() - line.replaceAll("\"","").length();
						int count2 = line.length() - line.replaceAll("\'","").length();
						if(count==2) {
							datestr = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
						}
						else if(count2==2) {
							datestr = line.substring(line.indexOf("\'")+1,line.lastIndexOf("\'"));
						}
						else {
							System.out.println("getting Min_Date failed - " + line);
						}
						if(!datestr.equals("0")){
							int count3 = line.length() - line.replaceAll("/","").length();
							if(count3 == 2) {
								Min_Date = mdy.parse(datestr).getTime();
							}
						}
						
					}
					if(line.contains("Use_Events_All_Save_File")) {
						if(line.toLowerCase().contains("true")) {
							Use_Events_All_Save_File = true;
						}
					}
					if(line.contains("Use_Events_Vendor_Save_File")) {
						if(line.toLowerCase().contains("true")) {
							Use_Events_Vendor_Save_File = true;
						}
					}
					if(line.contains("Use_Events_Auction_Save_File")) {
						if(line.toLowerCase().contains("false")) {
							Use_Events_Auction_Save_File = false;
						}
					}
					if(line.contains("Use_Events_Trade_Save_File")) {
						if(line.toLowerCase().contains("false")) {
							Use_Events_Trade_Save_File = false;
						}
					}
					if(line.contains("Use_Events_COD_Save_File")) {
						if(line.toLowerCase().contains("false")) {
							Use_Events_COD_Save_File = false;
						}
					}
					
					if(line.contains("Read_In_New_Vendor_Events")) {
						if(line.toLowerCase().contains("true")) {
							Read_In_New_Vendor_Events = true;
						}
					}
				}
			}
			scan.close();
			System.out.println("Read_From_TSM_Files: \t\t"+Read_From_TSM_Files);
			System.out.println("Min_Date: \t\t\t"+Min_Date);
			System.out.println("Use_Events_All_Save_File: \t"+Use_Events_All_Save_File);
			System.out.println("Use_Events_Vendor_Save_File: \t"+Use_Events_Vendor_Save_File);
			System.out.println("Use_Events_Auction_Save_File: \t"+Use_Events_Auction_Save_File);
			System.out.println("Use_Events_Trade_Save_File: \t"+Use_Events_Trade_Save_File);
			System.out.println("Use_Events_Auction_Save_File: \t"+Use_Events_Auction_Save_File);
			System.out.println("Read_In_New_Vendor_Events: \t"+Read_In_New_Vendor_Events);
			System.out.println();
		}
	}

	//handles Reading in saved event data that was processed by previous runs of this application.
	//Only reads in files based on settings.txt
	private static void getSavedEvents(long min_date) throws IOException {
		if(Use_Events_All_Save_File) {
			readSavedEvents("Events-All.txt", min_date);
		}
		if(Use_Events_Vendor_Save_File) {
			readSavedEvents("Events-Vendor.txt", min_date);
		}
		if(Use_Events_Auction_Save_File) {
			readSavedEvents("Events-Auction.txt", min_date);
		}
		if(Use_Events_Trade_Save_File) {
			readSavedEvents("Events-Trade.txt", min_date);
		}
		if(Use_Events_COD_Save_File) {
			readSavedEvents("Events-COD.txt", min_date);
		}
		readSavedEvents("gtransfer-events.txt", min_date);
		
	}

	//not for public use
	public static void enchantProfit(Subset s) throws ParseException, IOException {
		PrintWriter out = new PrintWriter(new File("enchant_data.txt"));
		s=s.realm("Thrall");
		//s = new Subset(Filter.combine(s.name("Tidespray Linen").items,Filter.combine(s.name("Nylon Thread").items,Filter.combine(s.name("Tempest Hide").items,Filter.combine(s.name("Umbra Shard").items,Filter.combine(s.name("Mistscale").items,Filter.combine(s.name("Calcified Bone").items,Filter.combine(s.name("Gloom Dust").items,Filter.combine(s.name("Veiled Crystal").items,s.name("Enchant").items)))))))));

		double linen = Data_processing.sum(s.type("Purchase").name("Tidespray Linen").items,"goldearned");
		double tempesthide = Data_processing.sum(s.type("Purchase").name("Tempest Hide").items,"goldearned") + Data_processing.sum(s.type("Purchase").name("Mistscale").items,"goldearned");
		double calcifiedbones = Data_processing.sum(s.type("Purchase").name("Calcified Bone").items,"goldearned");
		double dust = Data_processing.sum(s.type("Purchase").name("Gloom Dust").items,"goldearned");
		double enchantSales = Data_processing.sum(s.type("Sale").name("Enchant").items,"goldearned");
		double crystals = Data_processing.sum(s.type("Sale").name("Veiled Crystal").items,"goldearned");
		double crystalsPurchase = Data_processing.sum(s.type("Purchase").name("Veiled Crystal").items,"goldearned");
		double thread = Data_processing.sum(s.nameExact("Tidespray Linen").type("Purchase").items,"quantity")/2d*-0.6;
		double umbra = Data_processing.sum(s.type("Sale").name("Umbra Shard").items,"goldearned");
		double enchantsBought = Data_processing.sum(s.type("Purchase").loc("Auction").name("Enchant").items,"goldearned");

		double linenavg = linen / Data_processing.sum(s.type("Purchase").name("Tidespray Linen").items,"quantity");
		double linenq = Data_processing.sum(s.type("Purchase").name("Tidespray Linen").items,"quantity");
		double tempesthideavg = tempesthide / (Data_processing.sum(s.type("Purchase").name("Tempest Hide").items,"quantity") + Data_processing.sum(s.type("Purchase").name("Mistscale").items,"quantity"));
		double calcifiedbonesavg = calcifiedbones / Data_processing.sum(s.type("Purchase").name("Calcified Bone").items,"quantity");
		double dustavg = dust / Data_processing.sum(s.type("Purchase").name("Gloom Dust").items,"quantity");
		double crystalsPurchaseavg = crystalsPurchase / Data_processing.sum(s.type("Purchase").name("Veiled Crystal").items,"quantity");
		double crystalsavg = crystals / Data_processing.sum(s.type("Sale").name("Veiled Crystal").items,"quantity");
		double umbraavg = umbra / Data_processing.sum(s.type("Sale").name("Umbra Shard").items,"quantity");
		
		
		String one = "%-14s";
		String two = "%11s";
		String three = "%11s";
		
		out.print(String.format(one, "thread:"));
		out.print(String.format(two, String.format("%.2f", thread)));
		out.print(String.format(three, " "));
		//out.println("\t"+String.format(three, String.format("%.2f", threadq)));
		out.println();
		
		out.print(String.format(one, "Linen:"));
		out.print(String.format(two, String.format("%.2f", linen)));
		out.print(String.format(three, String.format("%.2f", linenavg)));
		//out.print("\t"+String.format(three, String.format("%.2f", linenq)));
		out.println();
		
		out.print(String.format(one, "tempesthide:"));
		out.print(String.format(two, String.format("%.2f", tempesthide)));
		out.println(String.format(three, String.format("%.2f", tempesthideavg)));
		
		out.print(String.format(one, "calcbone:"));
		out.print(String.format(two, String.format("%.2f", calcifiedbones)));
		out.println(String.format(three, String.format("%.2f", calcifiedbonesavg)));
		
		out.print(String.format(one, "dust:"));
		out.print(String.format(two, String.format("%.2f", dust)));
		out.println(String.format(three, String.format("%.2f", dustavg)));
		
		out.print(String.format(one, "umbra:"));
		out.print(String.format(two, String.format("%.2f", umbra)));
		out.println(String.format(three, String.format("%.2f", umbraavg)));
		
		out.print(String.format(one, "crystals:"));
		out.print(String.format(two, String.format("%.2f", crystals)));
		out.println(String.format(three, String.format("%.2f", crystalsavg)));
		
		out.print(String.format(one, "\n\ncrystals bought:"));
		out.print(String.format(two, String.format("%.2f", crystalsPurchase)));
		out.println(String.format(three, String.format("%.2f", crystalsPurchaseavg)));

		out.print(String.format(one, "Thread from AH:"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(s.type("Purchase").loc("Auction").name("Nylon Thread").items,"quantity")*.6+Data_processing.sum(s.type("Purchase").loc("Auction").name("Nylon Thread").items,"goldearned"))));

		out.print(String.format(one, "Enchants Bought:"));
		out.print(String.format(two, String.format("%.2f", enchantsBought)));
		out.println("\t"+String.format(three, String.format("%.2f", enchantsBought/Data_processing.sum(s.type("Purchase").loc("Auction").name("Enchant").items,"quantity"))));
		//sinister(s);
		
		
		double cards = Data_processing.sum(s.type("Sale").loc("Auction").name("of the Tides").items,"goldearned") +
				Data_processing.sum(s.type("Sale").loc("Auction").name("of Blockades").items,"goldearned")+
		Data_processing.sum(s.type("Sale").loc("Auction").name("of Fathoms").items,"goldearned")+
		Data_processing.sum(s.type("Sale").loc("Auction").name("of Squalls").items,"goldearned");
		double decks = Data_processing.sum(s.type("Sale").loc("Auction").name("Darkmoon Deck:").items,"goldearned");
		
		//out.println("Viridescent: "+String.format("%.2f", Data_processing.sum(s.type("Purchase").loc("Auction").name("Viridescent").items,"goldearned")));
		//out.println("Herbs: "+String.format("%.2f", Data_processing.sum(s.type("Purchase").loc("Auction").name("Viridescent").items,"goldearned")));
		
		//out.println("\nDarkmoon Cards: "+String.format("%.2f", cards));
		//out.println("Darkmoon Cards: "+String.format("%.2f", decks));
		out.println(String.format(one, "\nExpenses:"));
		out.println(String.format(two, String.format("%.2f", linen + tempesthide + calcifiedbones + dust + thread + crystalsPurchase + enchantsBought /*+ sinister + coarseleather + hardenedhide*/)));

		out.println(String.format(one, "\nSales:"));
		out.println(String.format(two, String.format("%.2f", crystals + enchantSales + umbra)));
		
		out.println(String.format(one, "\nProfit:"));
		out.println(String.format(two, String.format("%.2f", (crystals + enchantSales + umbra + linen + tempesthide + calcifiedbones + dust + thread + crystalsPurchase + enchantsBought /*+ sinister + coarseleather + hardenedhide*/))));
		out.println("\n");
		out.println("\n\n\n--------------------------------------------------------\n\n\n");
		ArrayList<String> enchants =
				new ArrayList<String>(Arrays.asList(
				"Accord of Critical Strike",
				"Accord of Haste",
				"Accord of Mastery",
				"Accord of Versatility",
				"Masterful Navigation",
				"Quick Navigation",
				"Versatile Navigation",
				"Deadly Navigation",
				"Force Multiplier",
				"Machinist's Brilliance",
				"Naga Hide",
				"Oceanic Restoration"
				));

		for(int i=0; i<enchants.size();i++) {
			double quantity = Data_processing.sum(s.type("Sale").name(enchants.get(i)).items,"quantity");
			if(quantity != 0) {
				double sale = Data_processing.sum(s.type("Sale").name(enchants.get(i)).items,"goldearned");
				double avg = sale / quantity;
				out.println(String.format("%.0f", quantity)+"\t"+String.format("%.2f", sale)+"\t"+String.format("%.2f", avg));
				
			}
			else {
				out.println();
			}
			if(i==3||i==7) {
				out.println("\n");
			}
		}
		out.println("\n\n\n--------------------------------------------------------\n\n\n");
		out.close();
	}

	//Writes any/all event data to Purchase.txt and Sales.txt, seperated into various time periods. (Today, Yesterday, The previous 7 days, Total (all events).
	//Data includes item name, total gold earned/spent, average gold per item
	private static void writePerDayData(Subset s, String file) throws ParseException, IOException {
		
		String one = "%-50s";
		String two = "%12s";
	
		
		System.out.println("writePerDayData");
		PrintWriter out = new PrintWriter(new FileOutputStream(file, false));
		Subset a  = s.date(mdy.parse(Master.mdy.format(new Date())).getTime(), mdy.parse(Master.mdy.format(new Date().getTime()+1000*60*60*24l)).getTime());
		
		Subset b  = s.date(mdy.parse(Master.mdy.format(new Date())).getTime()-1000*60*60*24l, mdy.parse(Master.mdy.format(new Date().getTime())).getTime());
		Subset c  = s.date(mdy.parse(Master.mdy.format(new Date())).getTime()-1000*60*60*24*7l, mdy.parse(Master.mdy.format(new Date().getTime()+1000*60*60*24l)).getTime());
		
		//System.out.println(mdy.parse(Master.mdy.format(new Date())).getTime()+" to "+ mdy.parse(Master.mdy.format(new Date().getTime()+1000*60*60*24l)).getTime());
		//System.out.println(mdy.parse(Master.mdy.format(new Date())).getTime()-1000*60*60*24l+" to "+ mdy.parse(Master.mdy.format(new Date().getTime())).getTime());
		//System.out.println(mdy.parse(Master.mdy.format(new Date())).getTime()-1000*60*60*24*7l);
		
		
		out.println("TODAY");
		
		out.print(String.format(one, "Total"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(a.loc("Vendor").items,"goldearned")+ Data_processing.sum(a.loc("Auction").items,"goldearned"))));
		
		out.print(String.format(one, "Vendor"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(a.loc("Vendor").items,"goldearned"))));
		
		out.print(String.format(one, "Auction"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(a.loc("Auction").items,"goldearned"))));
		
		getPerDayData(a.loc("Auction"),"Sale","Auction");
		for(String event : eventsPerDay) {
			out.println(event);
		}
		out.println("\n\n\n\n\n\nYESTERDAY");
	
		
		out.print(String.format(one, "Total"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(b.loc("Vendor").items,"goldearned")+ Data_processing.sum(b.loc("Auction").items,"goldearned"))));
		
		out.print(String.format(one, "Vendor"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(b.loc("Vendor").items,"goldearned"))));
		
		out.print(String.format(one, "Auction"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(b.loc("Auction").items,"goldearned"))));
		
		getPerDayData(b.loc("Auction"),"Sale","Auction");
		for(String event : eventsPerDay) {
			out.println(event);
		}
		
		out.println("\n\n\n\n\n\nWeek");
		
		out.print(String.format(one, "Total"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(c.loc("Vendor").items,"goldearned")+ Data_processing.sum(c.loc("Auction").items,"goldearned"))));
		
		out.print(String.format(one, "Vendor"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(c.loc("Vendor").items,"goldearned"))));
		
		out.print(String.format(one, "Auction"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(c.loc("Auction").items,"goldearned"))));
		
		getPerDayData(c.loc("Auction"),"Sale","Auction");
		for(String event : eventsPerDay) {
			out.println(event);
		}
		
		out.println("\n\n\n\n\n\nTotal");
		
		out.print(String.format(one, "Total"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(s.loc("Vendor").items,"goldearned")+ Data_processing.sum(s.loc("Auction").items,"goldearned"))));
		
		out.print(String.format(one, "Vendor"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(s.loc("Vendor").items,"goldearned"))));
		
		out.print(String.format(one, "Auction"));
		out.println(String.format(two, String.format("%.2f", Data_processing.sum(s.loc("Auction").items,"goldearned"))));
		
		getPerDayData(s.loc("Auction"),"Sale","Auction");
		for(String event : eventsPerDay) {
			out.println(event);
		}
		out.close();
	}

	private static void getPerDayData(Subset s, String type, String loc) throws ParseException, IOException {
		eventsPerDay.clear();
		//System.out.println("getPerDayData");
		if(s.items.size() > 0) {
			for(Item item : s.items) {
			Subset itemSub = new Subset(s).id(item.idname.id);
			if(itemSub.items.size() > 0) {
				if(item.idname.name.equals("") || Double.toString(Data_processing.avg(itemSub.items,"goldearned")).equals("NaN") ||  item.idname.id == 0) {

				}
				else {
					eventsPerDay.add(
						String.format("%-50s", item.idname.name)+
							String.format("%12s", (int)Data_processing.sum(itemSub.items,"quantity"))/*+","*/+
							String.format("%20s", String.format("%.2f", Data_processing.sum(itemSub.items,"goldearned")))+
							String.format("%12s", String.format("%.2f", Data_processing.sum(itemSub.items,"goldearned")/(int)Data_processing.sum(itemSub.items,"quantity")))
							);
					}
				}
			}
		}
	}
	
	//Not for general use
	public static void getFullDirectories() {
		String directory = "\\\\TOWER\\WoW\\tsm\\Tradeskillmaster-lua\\Account\\";
		ArrayList<String> backups = Data_processing.cleanArrayListToString(new ArrayList<String>(Arrays.asList(Arrays.toString(new File(directory).list()).split(","))));
		
		for(String dir : backups) {
			//if(dir.equals("81892423#1") || dir.equals("81892423#2") || dir.equals("81892423#6")) {
				String str = directory+dir; 
				ArrayList<String> individualfiles = Data_processing.cleanArrayListToString(new ArrayList<String>(Arrays.asList(Arrays.toString(new File(str).list()).split(","))));
				for(String file : individualfiles) {
					String filepath = str+"\\"+file;
					if (new File(filepath).isFile()&&!usefulFiles.contains(filepath)) {
						//directories.add(filepath);
					}
				}
			//}
		}
		
		directory = "\\\\TOWER\\WoW\\tsm\\Tradeskillmaster-lua\\Backups\\Account\\";
		backups.clear();
		backups = Data_processing.cleanArrayListToString(new ArrayList<String>(Arrays.asList(Arrays.toString(new File(directory).list()).split(","))));
		
		for(String dir : backups) {
			String str = directory+dir; 
			ArrayList<String> individualfiles = Data_processing.cleanArrayListToString(new ArrayList<String>(Arrays.asList(Arrays.toString(new File(str).list()).split(","))));
			for(String file : individualfiles) {
				String filepath = str+"\\"+file;
				if (new File(filepath).isFile()&&!usefulFiles.contains(filepath)) {
					//directories.add(filepath);
				}
			}
		}
	}
	
	//Rads in saved event data that was processed by previous runs of this application
	private static void readSavedEvents(String fileName,long min_date) throws IOException {
		System.out.println("readSavedEvents: "+fileName);
		File file = new File(fileName);
		if(file.exists()) {
			BufferedReader bf = new  BufferedReader(new FileReader(fileName));
			String line;
			while((line = bf.readLine())  != null) {
				if(eventHash.contains(line)) {
					//System.out.println(eventHash.size()+" \t\t\t\t\t"+allEvents.size());
    				//System.out.println(line);
    			}
				//System.out.println(fileName);
				//System.out.println(line);
				if(!line.contains("Customer Support")&&
	    				!eventHash.contains(line)&&
	    				Long.parseLong(line.substring(line.lastIndexOf(",")-10,line.lastIndexOf(",")))*1000l>min_date
	    				&&!line.contains(",i:0,")
	    					) {
					eventHash.add(line);
					eventDataHandler(line);
					
					if(allEvents.size()%10000==0) {
						System.out.println(allEvents.size());
					}
				}
			}
			bf.close();
			Collections.sort(allEvents);
			Files.copy(file.toPath(), new File(fileName+".bak").toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		else {
			System.out.println("File does not exist");
		}
	}
	
	//Reads data from TradeSkillMaster.lua files
	public static void readFiles(long min) throws IOException, ParseException {
		System.out.println("readFiles");
		
		System.out.println(min+" "+mdy.format(min));
		System.out.println(Master.allEvents.size());
		PrintWriter out = new PrintWriter(new File("inventory.txt"));
		PrintWriter out2 = new PrintWriter(new FileOutputStream(new File("which-files.txt"), true));
		System.out.println(directories);
		int count =1;
		long numEvents = 0;
		for(String account : directories) {
			
			System.out.println(count +"/"+directories.size()+" "+ account);
			HashSet<String> fileEventHash = new HashSet<>();
			count++;
			long time = ((long) Data_processing.max(new Subset().account(account).items,"time"))-(60*60*24*1000l*3); //Why is this looking at Subset data in the middle of readFiles function?
			if(time<=0) {
				time = min;
			}
			
			if (new File(account).isFile()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new  FileInputStream(new File(account)),"UTF-8"));
				String data;
				
				boolean inBagObject = false;
				boolean inGuildsObject = false;
				boolean inGuildObject = false;
				String location = "";
				location = location + "";
			    while ((data = reader.readLine()) != null) {
			    	data = data.replace("\\n","\n");
					List<String> allData = Arrays.asList(data.split("\n"));
					if(Data_processing.whatTypeEvent(data)!=null) {
			    		int num = 0;
			    		for(String eventdata : allData) {
			    			eventdata = Data_processing.parseFixes(eventdata);
			    			if(fileEventHash.contains(eventdata)) {
			    				//System.out.println(eventdata);
			    			}
			    			String str =new File(new File(new File(account).getParent()).getParent()).getName()+","+data.substring(data.indexOf("@")+1,data.indexOf("@",data.indexOf("@")+1))+","+Data_processing.whatTypeEvent(data)+","+eventdata;
			    			
			    			if(new File(new File(account).getParent()).getName().contains("#")) {
			    				String filename = new File(new File(account).getParent()).getName();
			    				filename = filename.substring(0,filename.indexOf("#")+2);
			    				str =filename+","+data.substring(data.indexOf("@")+1,data.indexOf("@",data.indexOf("@")+1))+","+Data_processing.whatTypeEvent(data)+","+eventdata;

			    			}
			    			//System.out.println(str);
			    			if(((Read_In_New_Vendor_Events && str.endsWith("Vendor")) || !str.endsWith("Vendor"))&&
			    				!str.contains("Customer Support")&&
			    				!eventHash.contains(str)&&num!=0&&
			    				Long.parseLong(str.substring(str.lastIndexOf(",")-10,str.lastIndexOf(",")))*1000l>time
			    				&&!str.contains(",i:0,")
			    					) {
			    				
			    				
				    				eventHash.add(str);
				    				//System.out.println(str);
				    				eventDataHandler(str);
			    				
			    			}
			    			num++;
			    			
			    		}
			    	}
					else {
						
						//PERSONAL BAG AND BANK
						if(data.contains("},")&&inBagObject) {
							inBagObject=false;
						}
						if(inBagObject) {
							
							if(data.indexOf("[\"i:") >=0) {
								int id = Integer.parseInt(data.substring(data.indexOf("[\"i:")+4,data.indexOf("\"]")));
								Data_processing.idToIDName(id);
							}
							else {
								out.println(data);
							}
							
							
						}
						if(data.contains("bagQuantity")||data.contains("bankQuantity")||data.contains("mailQuantity")||data.contains("reagentBankQuantity")||data.contains("auctionQuantity")) {
							out.println(data);
							inBagObject = true;
							location=data;
							
						}
						
						//SPECIFIC GUILD BANK
						if(data.contains("},")&&inGuildsObject&&!inGuildObject) {
							inGuildsObject=false;
						}
						if(data.contains("},")&&inGuildsObject) {
							inGuildObject=false;
						}
						
						if(inGuildsObject&&data.contains("] = {")) {
							inGuildObject = true;
							location=data;
						}
						
						//GUILD BANKS
						if(inGuildsObject) {
							//out.println(data);
							
							if(data.indexOf("[\"i:") >=0) {
								int id = Integer.parseInt(data.substring(data.indexOf("[\"i:")+4,data.indexOf("\"]")));
								out.println(data);
								Data_processing.idToIDName(id);
							}
						}
						if(data.contains("guildVaults")) {
							out.println(data);
							inGuildsObject = true;
							location=data;
						}
						
	
					}
			    }
			    reader.close();
			}
			System.out.println(numEvents);//+"\t"+Master.allEvents.size());
			if(numEvents != Master.allEvents.size()&& account.toLowerCase().contains("tower")&&!usefulFiles.contains(account)) {
				//System.out.println("useful");
				out2.println(account);
			}
			else {
				//System.out.println("not useful");
			}
			numEvents = Master.allEvents.size();
		}
		out.close();
		out2.close();
	}
	
	//Event data helper function
	public static void eventDataHandler(String line) throws IOException {
		//System.out.println(line);
		ArrayList<String> data = new ArrayList<String>(Arrays.asList(line.split(",")));
		Event e = Data_processing.createNewEvent(data);
		if(e.iid == 172318) {
			//System.out.println(line);
			//System.out.println(e.id);
		}
		IDName idname = Data_processing.idToIDName(e.iid);
		if(e.iid == 0) {
			return;
		}
		if(idname.parent==null) {
			Item item = new Item(idname);
			idname.parent=item;
			item.addEvent(e);
			allItems.add(item);
		}
		else { 
			idname.parent.addEvent(e);
		}
	}
	
	//Writes data so that it's saved for future runs of this program
	public static void writeSavedEvents(String str, Subset s) throws ParseException, IOException {
		System.out.println("writeSavedEvents: "+str);
		ArrayList<Event> events = new ArrayList<Event>();
		PrintWriter out = new PrintWriter(new File(str));
		for(Item item : s.items) {
			for(Event event : item.events) {
				events.add(event);
			}
		}
		Collections.sort(events);
		for(Event event : events) {
			out.println(event.printData());
		}
		events.clear();
		out.close();
	}
	
	//handles writing data so that it's saved for future runs of this program
	public static void writeSavedEvents(Subset s) throws IOException, ParseException {
		if(Use_Events_All_Save_File) {
			writeSavedEvents("Events-All.txt",s);
		}
		if(Use_Events_Vendor_Save_File) {
			writeSavedEvents("Events-Vendor.txt",s.loc("Vendor"));
		}
		if(Use_Events_Auction_Save_File) {
			writeSavedEvents("Events-Auction.txt",s.loc("Auction"));
		}
		if(Use_Events_Trade_Save_File) {
			writeSavedEvents("Events-Trade.txt",s.loc("Trade"));
		}
		if(Use_Events_COD_Save_File) {
			writeSavedEvents("Events-COD.txt",s.loc("COD"));
		}
	}
	
	//Reads in IDNames.txt, so that item IDs are associated with item names
	public static void IDNamesFileIN() throws NumberFormatException, IOException {
		File file = new File("IDNames.txt");
		if(file.exists()) {
			BufferedReader bf = new  BufferedReader(new FileReader(file));
			String line;
			while((line = bf.readLine())  != null) {
				int id = Integer.parseInt(line.substring(0, line.indexOf(",")));
				IDName idname = Data_processing.getIDName(id);
				if(idname==null) {
					idnames.add(new IDName(Integer.parseInt(line.substring(0, line.indexOf(","))),line.substring(line.indexOf(",")+2,line.length())));
				}
			}
			bf.close();
		}
		
	}

	@SuppressWarnings("unused")
	private static Comparator<IDName> compareByName = new Comparator<IDName>() {
		public int compare(IDName a, IDName b) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(a.name, b.name);
			return (res != 0) ? res : a.name.compareTo(b.name);
		}
	};
	
	private static Comparator<IDName> compareByID = new Comparator<IDName>() {
		public int compare(IDName a, IDName b) {
			return a.id-b.id;
		}
	};
	
	//Sorts and then writes all item ID/name combos
	public static void IDNamesFileOUT() throws IOException {
		System.out.println("IDNamesFileOut");
		Collections.sort(idnames, compareByID);
		
		PrintWriter out = new PrintWriter(new File("IDNames.txt"));
		for(IDName idname : idnames) {
			//if(idname.id>4) {
				out.println(idname.id+", "+idname.name);
			//}
		}
		out.close();
	}

	public static void main(String Args[]) throws IOException, ParseException, URISyntaxException {
		System.out.println("Master main");
		@SuppressWarnings("unused")
		Master master = new Master();
		System.out.println("Done");
		//System.in.read();
	}
}
