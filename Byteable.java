package j3dio;

/**
 * Intended for use with binary file IO
 * 
 * @author Evan Shimoniak
 * @since 4.0 beta
**/
public interface Byteable {
	/**@return A byte array representing this object**/
	public byte[] toBytes();
	/**@return The number of bytes needed to represent this object**/
	public int getByteSize();
}