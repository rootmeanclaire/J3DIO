package j3dio;

import java.io.IOException;

public interface Exportable {
	/**
	 * @param fileName The full path, including file name, of the export destination without an extension.
	 * @throws IOException 
	**/
	public void export(String fileName) throws IOException;
}