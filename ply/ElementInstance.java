package j3dio.ply;

import static j3dio.ply.Element.Datatype.CHAR;
import static j3dio.ply.Element.Datatype.DOUBLE;
import static j3dio.ply.Element.Datatype.FLOAT;
import static j3dio.ply.Element.Datatype.FLOAT32;
import static j3dio.ply.Element.Datatype.FLOAT64;
import static j3dio.ply.Element.Datatype.INT;
import static j3dio.ply.Element.Datatype.INT16;
import static j3dio.ply.Element.Datatype.INT32;
import static j3dio.ply.Element.Datatype.SHORT;
import static j3dio.ply.Element.Datatype.UCHAR;
import static j3dio.ply.Element.Datatype.UINT;
import static j3dio.ply.Element.Datatype.UINT16;
import static j3dio.ply.Element.Datatype.UINT32;
import static j3dio.ply.Element.Datatype.USHORT;

import j3dio.ply.Element.ListType;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class ElementInstance {
	public final String tyepname;
	/**
	 * <b>Key:</b> Property name<br />
	 * <b>Value:</b> Property value  
	**/
	private Map<String, Object> props = new HashMap<String, Object>();
	
	ElementInstance(Element datatype) {
		this.tyepname = datatype.name;
		Map<String, Object> elemProps = datatype.getProperties();
		
		for (String propName : elemProps.keySet()) {
			props.put(propName, newProperty(elemProps.get(propName)));
		}
	}
	ElementInstance(Element datatype, String str) {
		this.tyepname = datatype.name;
		Map<String, Object> elemProps = datatype.getProperties();
		String[] splitStr = str.split("\\s+");
		int i = 0;
		
		//Clean up array
		{
			int removed = 0;
			String[] newArray = new String[splitStr.length];
			
			for (int j = 0; j < splitStr.length; j++) {
				if (splitStr[j] != null && !splitStr[j].isEmpty()) {
					newArray[j - removed] = splitStr[j];
				} else {
					removed++;
				}
			}
			
			splitStr = Arrays.copyOfRange(newArray, 0, newArray.length - removed);
		}
		
		//Instantiate all properties
		for (String propName : elemProps.keySet()) {
			//If property is not a list
			if (!(elemProps.get(propName) instanceof ListType)) {
				props.put(propName,
						newProperty(elemProps.get(propName),
							splitStr[i]
						)
				);
			} else {
				props.put(propName,
						newProperty(elemProps.get(propName),
							str
						)
				);
			}
			i++;
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + "@" + Integer.toHexString(hashCode())
				+ "\"" + tyepname + "\"{" + this.toLine() + "}";
	}
	
	public String toLine() {
		StringBuilder sb = new StringBuilder();
		
//		System.out.print("Properties: ");
//		for (String propName : props.keySet()) {
//			System.out.print(propName + " ");
//		}
//		System.out.print("\n");
		for (Object propVal : props.values()) {
			if (propVal instanceof List) {
				List<?> listProp = (List<?>) propVal;
				
				sb.append(listProp.size() + " ");
				
				for (Object obj : listProp) {
					sb.append(obj.toString() + " ");
				}
			} else {
				sb.append(propVal.toString() + " ");
			}
		}
		
		
		return sb.toString();
	}
	
	public Object get(String propertyName) {
		if (props.containsKey(propertyName)) {
			return props.get(propertyName);
		} else {
			throw new InvalidParameterException("Element \"" + tyepname + "\" has no property \"" + propertyName + "\"");
		}
	}
	
	private static Object newProperty(Object datatype) {
		if (datatype == CHAR ||
			datatype == UCHAR) {
			return Character.valueOf('\u0000');
		} else if (datatype == SHORT ||
				   datatype == USHORT ||
				   datatype == INT16 ||
				   datatype == UINT16) {
			return Short.valueOf((short) 0);
		} else if (datatype == INT ||
				   datatype == UINT ||
				   datatype == INT32 ||
				   datatype == UINT32) {
			return Integer.valueOf(0);
		} else if (datatype == FLOAT ||
				   datatype == FLOAT32) {
			return Float.valueOf(0f);
		} else if (datatype == DOUBLE ||
				   datatype == FLOAT64) {
			return Double.valueOf(0d);
		} else if (datatype instanceof ListType) {
			ListType list = (ListType) datatype;
			
			if (newProperty(list.valType).getClass() == Character.class) {
				return new ArrayList<Character>();
			} else if (newProperty(list.valType).getClass() == Short.class) {
				return new ArrayList<Short>();
			} else if (newProperty(list.valType).getClass() == Integer.class) {
				return new ArrayList<Integer>();
			} else if (newProperty(list.valType).getClass() == Float.class) {
				return new ArrayList<Float>();
			} else if (newProperty(list.valType).getClass() == Double.class) {
				return new ArrayList<Double>();
			} else {
				return null;
			}
		} else {
			throw new InvalidParameterException(
					"Unrecognized property type: \"" + datatype.getClass().getSimpleName() + "\""
			);
		}
	}
	private static Object newProperty(Object datatype, String valStr) {
		if (datatype == CHAR ||
			datatype == UCHAR) {
			return Character.valueOf(valStr.toCharArray()[0]);
		} else if (datatype == SHORT ||
				   datatype == USHORT ||
				   datatype == INT16 ||
				   datatype == UINT16) {
			return Short.valueOf(valStr);
		} else if (datatype == INT ||
				   datatype == UINT ||
				   datatype == INT32 ||
				   datatype == UINT32) {
			return Integer.valueOf(valStr);
		} else if (datatype == FLOAT ||
				   datatype == FLOAT32) {
			return Float.valueOf(valStr);
		} else if (datatype == DOUBLE ||
				   datatype == FLOAT64) {
			return Double.valueOf(valStr);
		} else if (datatype instanceof ListType) {
			ListType list = (ListType) datatype;
			List listToReturn = (List<?>) newProperty(list);
			String[] splitStr = valStr.split("\\s+");
			
			int listLength = Integer.parseInt(splitStr[0]);
			
			
			for (int i = 1; i <= listLength; i++) {
				listToReturn.add(newProperty(list.valType, splitStr[i]));
			}
			
			return listToReturn;
		} else {
			throw new InvalidParameterException("Unrecognized property type");
		}
	}
	public Map<String, Object> getProperties() {
		return props;
	}
}