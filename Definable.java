package j3dio;

/**
 * For objects that are defined in a file by a string
 * @author Evan Shimoniak
 * @since 4.1.1 beta
**/
public interface Definable {
	/**@return A String definition of this object as it would appear in a file**/
	public String toDefinition();
}