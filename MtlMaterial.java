package obj;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MtlMaterial {
	private String name;
	private Color ambient;
	private Color diffuse;
	private Color specular;
	private float wSpec;
	private float dissolve;
	private byte illum;
	
	private MtlMaterial(String name, Color ambient, Color diffuse, Color specular, float wSpec, float transparency, byte illuminationModel) {
		this.name = name;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.wSpec = wSpec;
		this.dissolve = transparency;
		this.illum = illuminationModel;
	}
	
	public static List<MtlMaterial> loadMtls(File file) throws IOException {
		List<MtlMaterial> materials = new ArrayList<MtlMaterial>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] splitStr = line.split("\\s");
			String name;
			Color ambient;
			Color diffuse;
			Color specular;
			float wSpec;
			float dissolve;
			byte illuminationModel;
			
			//If line's arguments is three floats
			if (splitStr[0] == "Ka" || splitStr[0] == "Kd" || splitStr[0] == "Ks") {
				float x = Float.parseFloat(splitStr[1]);
				float y = Float.parseFloat(splitStr[2]);
				float z = Float.parseFloat(splitStr[3]);
				
				switch(splitStr[0]) {
					//Ambient color
					case "Ka":
						ambient = new Color(x, y, z);
						break;
					//Diffuse color
					case "Kd":
						diffuse = new Color(x, y, z);
						break;
					//Specular color
					case "Ks":
						specular = new Color(x, y, z);
						break;
				}
			} else if (splitStr[0] == "Ns" || splitStr[0] == "d" || splitStr[0] == "Tr") {
				float arg = Float.parseFloat(splitStr[1]);
				
				switch (splitStr[0]) {
					//Weighted specular coefficient
					case "Ns":
						wSpec = arg;
						break;
					//Transparency
					case "d":
						dissolve = arg;
						break;
					//Transparency (alternative key)
					case "Tr":
						dissolve = arg;
						break;
					default:
						
						break;
				}
			} else {
				switch (splitStr[0]) {
					//Comment
					case "#":
						
						break;
					//Illumination model
					case "newmtl":
						name = splitStr[1];
						break;
					//Illumination model
					case "illum":
						illuminationModel = Byte.parseByte(splitStr[1]);
						break;
					default:
						
						break;
				}
			}
		}
		
		
		br.close();
		return materials;
	}
}