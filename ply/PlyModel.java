package jml.ply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jml.Exportable;
import jml.ply.Element.Datatype;
import jml.ply.Element.ListType;

public class PlyModel implements Exportable {
	public enum Format {ASCII, BIN_LITTLE_ENDIAN, BIN_BIG_ENDIAN}
	private Map<Element, Integer> elements = new HashMap<Element, Integer>();
	private List<ElementInstance> data = new ArrayList<ElementInstance>();
	
	public PlyModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sb = new StringBuilder();
		Format format = null;
		boolean firstElement = true;
		int headerEndLn = 0;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".ply")) {
			throw new InvalidParameterException("File is not .ply");
		}
		
		//Parse header
		for (int ln = 1; (line = br.readLine()) != null; ln++) {
			if (ln == 1 && !line.equalsIgnoreCase("ply")) {
				System.err.println("Warning: File does not begin with \"ply\" prefix");
				continue;
			} else if (ln == 1) {
				continue;
			}
			
			String[] splitStr = line.split("\\s");
			
			//Comment
			if (splitStr[0].equals("comment")) {
				//TODO
			}
			//File format and version
			else if (splitStr[0].equals("format")) {
				switch (splitStr[1]) {
					case "ascii":
						format = Format.ASCII;
						break;
					case "binary_little_endian":
						format = Format.BIN_LITTLE_ENDIAN;
						break;
					case "binary_big_endian":
						format = Format.BIN_BIG_ENDIAN;
						break;
					default:
						System.err.println("Warning: Unrecognized format");
						break;
				}
			}
			//Element definition
			else if (splitStr[0].equals("element")) {
				if (!firstElement) {
					elements.put(new Element(sb.toString()), Integer.parseInt(sb.toString().split("\\s")[2]));
					sb = new StringBuilder();
				} else {
					firstElement = false;
				}
				sb.append(line + "\n");
			}
			else if (splitStr[0].equals("property")) {
				sb.append(line + "\n");
			}
			else if (splitStr[0].equals("end_header")) {
				headerEndLn = ln;
				elements.put(new Element(sb.toString()), Integer.parseInt(sb.toString().split("\\s")[2]));
				break;
			}
		}
		
		//Parse data
		switch (format) {
			case ASCII:
				//Iterate through defined elements
				for (Element element : elements.keySet()) {
					//Oddly necessary
					System.out.println("Instantiating " + elements.get(element) + " " + element);
					
					//Instantiate current element x times
					for (int i = 0; i < elements.get(element); i++) {
						if ((line = br.readLine()) != null) {
							data.add(new ElementInstance(element, line));
						}
					}
				}
				break;
			case BIN_LITTLE_ENDIAN:
				//TODO
				break;
			case BIN_BIG_ENDIAN:
				//TODO
				break;
			default:
				//TODO
				//Default to ASCII?
				break;
		}
		
		
		br.close();
	}

	@Override
	public void export(String fileName) throws FileNotFoundException {
		if (fileName.endsWith(".ply")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".ply");
		
		//Write header
		out.println("ply");
		out.println("format ascii 1.0");
		for (Element element : elements.keySet()) {
			out.println("element " + element.name + " " + elements.get(element));
			for (String propName : element.getProperties().keySet()) {
				if (element.getProperties().get(propName) instanceof Datatype) {
					out.println("property " + element.getProperties().get(propName).toString().toLowerCase() + ' ' + propName);
				} else if (element.getProperties().get(propName) instanceof ListType) {
					out.println(
						"property list " +
						((ListType) element.getProperties().get(propName)).lengthType.toString().toLowerCase() + ' ' +
						((ListType) element.getProperties().get(propName)).valType.toString().toLowerCase() + ' ' +
						propName
					);
				} else {
					System.err.println("Unrecognized property type, \"" + element.getProperties().get(propName).getClass().getName() + "\"");
				}
			}
		}
		out.println("end_header");
		//Write data
		for (ElementInstance dataPt : data) {
			out.println(dataPt.toLine());
		}
		
		out.close();
	}
}