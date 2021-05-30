import java.io.IOException;

/**
 * Represents a key/value pair between an item's numerical ID (set by Blizzard) and the items name.
 */
public class IDName {
	//#region properties
	int id = -9999;
	String name = "";
	Item parent=null;
	//#endregion
	
	//#region constructors
	public IDName(int id, String name) throws IOException{
		this.id = id;
		this.name = name;
	}
	//#endregion
	
	//#region methods
	/** 
	 * @param o object to compare against
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		return ((IDName)o).id == id && ((IDName)o).name.equals(name);
	}
	//#endregion
}