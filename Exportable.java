package jml;

import java.io.FileNotFoundException;

public interface Exportable {
	/**
	 * @param fileName The full path, including file name, of the export destination without an extension.
	 * @throws FileNotFoundException
	**/
	public void export(String fileName) throws FileNotFoundException;
}