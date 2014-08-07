package j3dio;

/**Intended for use with binary file IO**/
public interface Byteable {
	/**@return A byte array representing this object**/
	public byte[] toBytes();
	/**@return The number of bytes needed to represent this object**/
	public int getByteSize();
}