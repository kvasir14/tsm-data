import java.io.IOException;

public class IDName {
		int id = -9999;
		String name = "";
		Item parent=null;
		
		public IDName(int inc_id, String inc_name) throws IOException{
			id = inc_id;
			name = inc_name;
		}
		
		@Override
		public boolean equals(Object o) {
			return ((IDName)o).id == id && ((IDName)o).name.equals(name);
		}
	}