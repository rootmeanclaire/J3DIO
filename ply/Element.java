package j3dio.ply;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evan Shimoniak
 * @since 3.0
**/
public class Element {
	/**Totally meta.**/
	enum Datatype {
		CHAR, UCHAR, SHORT, USHORT, INT, UINT, FLOAT, DOUBLE,
		INT8, UNIT8, INT16, UINT16, INT32, UINT32, FLOAT32, FLOAT64
	};
	class ListType {
		public Datatype lengthType;
		public Datatype valType;
		
		public ListType(Datatype lengthType, Datatype valueType) {
			this.lengthType = lengthType;
			this.valType = valueType;
		}
	};
	
	public final String name;
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public Element(String definition) {
		String[] lines = definition.split("\n");
		
		name = lines[0].split("\\s")[1];
		
		for (String ln : lines) {
			String[] splitln = ln.split("\\s");
			
			if (splitln[0].equals("property")) {
				if (splitln[1].equals("list")) {
					properties.put(splitln[4], new ListType(Datatype.valueOf(splitln[2].toUpperCase()), Datatype.valueOf(splitln[3].toUpperCase())));
				} else {
					properties.put(splitln[2], Datatype.valueOf(splitln[1].toUpperCase()));
				}
			} else if (splitln[0].equals("element")) {
				continue;
			} else {
				//TODO error/warning
			}
			
		}
	}
	public Element(String name, Map<String, Object> properties) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + "@" + Integer.toHexString(super.hashCode()) + "\"" + name + "\"";
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public ElementInstance instantiate() {
		return new ElementInstance(this);
	}
}