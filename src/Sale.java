import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Represents a sale event in-game
 * 
 */
public class Sale extends Event{
	//#region constructors
	Sale(List<String> data) throws IOException {
		super(data);
		id = data.get(3);
		quantity = Integer.parseInt(data.get(5));
		number = Double.parseDouble(data.get(6));	
		otherPlayer = data.get(7);
		character = data.get(8);
		stackSize=data.get(4);
		gold = data.get(6);
		//itime=Integer.parseInt(data.get(9));
		dtime=Double.parseDouble(data.get(9));
		ltime=Long.parseLong(data.get(9))*1000L;
		time = Master.hm_md.format(new Date(ltime*1000L));
		loc = data.get(10);
		callback();
		Master.allEvents.add(this);
	}
	//#endregion
}
