import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Represents a purchase event in-game
 */
public class Purchase extends Event{
	//#region constructors
	Purchase(List<String> data) throws IOException {
		super(data);
		//System.out.println(inc_data);
		//System.out.println("Purchase Constructor");
		id = data.get(3);
		quantity = Integer.parseInt(data.get(5));
		number = Double.parseDouble(data.get(6))*-1d;	
		if(number > 0) {
			number=number*-1d;
		}
		gold = data.get(6);
		otherPlayer = data.get(7);
		character = data.get(8);
		//itime=Integer.parseInt(data.get(9));
		dtime=Double.parseDouble(data.get(9));
		ltime=Long.parseLong(data.get(9))*1000L;
		time = Master.hm_md.format(new Date(ltime*1000L));
		loc = data.get(10);
		stackSize=data.get(4);
		callback();
		Master.allEvents.add(this);
	}
	//#endregion
}
