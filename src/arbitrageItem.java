import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONObject;

public class arbitrageItem {
	List<JSONObject> auctions = new ArrayList<JSONObject>();
	TreeMap<Double, Integer> prices = new TreeMap<Double, Integer>();
	int[] magnitudes = new int[] {1,500,1000,2500,5000,10000,25000,50000,75000,100000,2500000,5000000,10000000};
	public arbitrageItem(JSONObject str) {
		
		add(str);
		
	}

	/** 
	 * @param obj
	 */
	public void add(JSONObject obj) {
		//System.out.println(obj);
		Double quantity = Double.parseDouble(obj.get("quantity").toString());
		Double goldDouble = (Double.parseDouble(obj.get("buyout").toString())/10000d)/quantity;
		DecimalFormat df = new DecimalFormat("#.##");
		goldDouble = Double.parseDouble(df.format(goldDouble));
		//System.out.println((Double.parseDouble(obj.get("buyout").toString()))+"\t\t"+(Double.valueOf(obj.get("buyout").toString())/quantity)+"\t"+(Double.valueOf(obj.get("buyout").toString())/quantity)/10000+"\t\t"+obj.get("quantity").toString());
		if(prices.get(goldDouble) == null) {
			prices.put(goldDouble, quantity.intValue());
			
		}
		else {
			prices.put(goldDouble, prices.get(goldDouble)+ quantity.intValue());
		}
	}

	/** 
	 * @return List<Double>
	 */
	public List<Double> calculateMagnitudes() {
		
		int counter = 0;
		List<Double> averages = new ArrayList<Double>();
		Double totalGold = 0d;
		
		int totalQuantity = 0;
		for(Double gold : prices.keySet()) {
			int quantity = prices.get(gold);
			if(totalQuantity>magnitudes[counter]) {
				DecimalFormat df = new DecimalFormat("#.##");
				Double avg = Double.parseDouble(df.format(totalGold/totalQuantity));
				averages.add(counter,avg);
				counter++;
			}
			
			totalGold+=gold*quantity;
			totalQuantity+=quantity;
			
		}
		return averages;
	}
}
