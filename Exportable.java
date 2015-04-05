package j3dio;

import java.io.IOException;

/**
 * @author Evan Shimoniak
 * @since 4.3 beta
**/
public interface Exportable {
	/**
	 * @param fileName The path, including the file name, of the export destination.
	 * @throws IOException
	**/
	public void export(String fileName) throws IOException;
}