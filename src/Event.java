import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An Event object represents an in-game Sale or Purchase
 */
public class Event implements Comparable<Object> {
	//#region properties
	List<String> data=new ArrayList<String>();
	String id = "";
	String bonusIDs = "";
	String price = "";
	double priceGold = 0l;
	long rprice = 0l;
	String time = "";
	long ltime=0l;
	int itime=0;
	double dtime=0d;
	double number=0d;
	String character = "";
	String server = "";
	int quantity = 0;
	String account = "";
	String otherPlayer = "N/A";
	String type = "";
	String loc = "";
	static int count = 0;
	Item parent = null;
	String name ="";
	int iid = 0;
	String stackSize ="0";
	String gold="-0";
	//#endregion

	//#region constructors
	Event(List<String> data) throws IOException{
		count++;
		this.data=data;
		//System.out.println(data);
		if(data.get(0).indexOf("Account\\")>0) {
			account = data.get(0).substring(data.get(0).indexOf("Account\\")+8,data.get(0).indexOf("#")+2);
		}
		else {
			account = data.get(0);
		}
		if(account.contentEquals("Account")) {
			System.out.println("ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR");
		}
		server = data.get(1);
		type = data.get(2);
	}
	//#endregion

	//#region methods
	/** 
	 * @return String
	 * @throws IOException
	 */
	public String printData() throws IOException {
		//account,realm,type,id,stacksize,quantity,number,otherplayer,character,time,loc
		return account+","+server+","+type+","+id+","+stackSize+","+quantity+","+gold+","+otherPlayer+","+character+","+ltime/1000l+","+loc;
	}

	/** 
	 * @return String
	 * @throws IOException
	 */
	public String printDataCSV() throws IOException {
		return Master.YYYYMMDD.format(new Date(ltime))+","+quantity*priceGold;
	}

	/** 
	 * @param o
	 * @return int
	 */
	@Override
	public int compareTo(Object o) {
		int comparetime=(int)((Event)o).dtime;
		return comparetime-(int)this.dtime;
	}

	/** 
	 * @param o
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		Event obj = (Event) o;
		if(!obj.name.equals(name) || 
				obj.iid != iid ||
				//obj.bonusIDs != bonusIDs ||
				obj.priceGold != priceGold ||
				obj.ltime != ltime ||
				!obj.type.equals(type) ||
				!obj.account.equals(account) ||
				!obj.character.equals(character) ||
				!obj.server.equals(server) ||
				obj.quantity != quantity
				
		) {
			return false;
		}
				return true;
	}
	
	/** 
	 * @return List<String>
	 */
	public List<String> getData() {
		return data;
	}

	/** 
	 * @throws IOException
	 */
	public void callback() throws IOException {
		//System.out.println(id);
		name=Data_processing.idToIDName(Data_processing.idToShortID(id)).name.replace(",", "-").replaceAll("\"", "");
		iid = Data_processing.idToShortID(id);
		bonusIDs = Data_processing.bonusIDS(id);
		//System.out.println(bonusIDs);
		priceGold = number/10000L;
		double copper = number%100d;
		number = number/100L;
		double silver = number%100d;
		number = number/100L;
		price = Double.toString(number)+"g"+Double.toString(silver)+"s"+Double.toString(copper)+"c";
		Master.eventHash.add(printData());
	}
	//#endregion
}

