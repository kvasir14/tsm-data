import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Represents a expense event in-game. Different than an purchase.
 */
public class Expense extends Event {
	//#region constructors
	Expense(List<String> data) throws IOException {
		super(data);
		//System.out.println("Expense Constructor");
		//System.out.println(inc_data);
		id = data.get(3);
		number = Double.parseDouble(data.get(4))*-1d;	
		gold = data.get(4);
		character = data.get(6);
		ltime=Long.parseLong(data.get(7))*1000L;
		itime=Integer.parseInt(data.get(7));
		dtime=Double.parseDouble(data.get(7));
		time = Master.hm_md.format(new Date(ltime*1000L));
		loc = data.get(5);
		callback();
		Master.allEvents.add(this);
	}
	//#endregion
}
