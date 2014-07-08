package obj;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class MtlMaterial {
	/**
	 * The string that will be used to reference this material.
	**/
	private String name;
	/**
	 * The ambient color of this material.
	**/
	private Color ambient;
	/**
	 * The diffuse color of this material.
	**/
	private Color diffuse;
	/**
	 * The specular color of this material.
	**/
	private Color specular;
	/**
	 * The weighted specular coefficient.
	**/
	private float wSpec;
	/**
	 * .mtl materials do not support true transparency. Instead
	 * they have "dissolve", which is different from true
	 * transparency because it doesn't depend upon the thickness
	 * of an object.
	**/
	private float dissolve;
	/**
	 * The illumination model of this material.
	**/
	private byte illum;
	
	/**
	 * @param name
	 * The string that will be used to reference this material.
	 * @param ambient
	 * The ambient color of this material.
	 * @param diffuse
	 * The diffuse color of this material.
	 * @param specular
	 * The weighted specular coefficient.
	 * @param weightedSpecularCoeff
	 * .mtl materials do not support true transparency. Instead
	 * they have "dissolve", which is different from true
	 * transparency because it doesn't depend upon the thickness
	 * of an object.
	 * @param transparency
	 * The illumination model of this material.
	 * @param illuminationModel
	 * The illumination model of this material.
	**/
	private MtlMaterial(String name, Color ambient, Color diffuse, Color specular, float weightedSpecularCoeff, float transparency, byte illuminationModel) {
		this.name = name;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.wSpec = weightedSpecularCoeff;
		this.dissolve = transparency;
		this.illum = illuminationModel;
	}
	
	/**
	 * @param file a .mtl file
	 * @return An {@link ArrayList} of all of the materials defined in file.
	 * @throws IOException
	 */
	public static List<MtlMaterial> loadMtls(File file) throws IOException {
		List<MtlMaterial> materials = new ArrayList<MtlMaterial>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		boolean firstMtl = true;
		
		if (file.getPath().substring(file.getPath().lastIndexOf('.') + 1) != "mtl") {
			throw new InvalidParameterException("File is not .mtl");
		}
		
		while ((line = br.readLine()) != null) {
			String[] splitStr = line.split("\\s");
			String name = null;
			Color ambient = null;
			Color diffuse = null;
			Color specular = null;
			float wSpec = 0;
			float dissolve = 0;
			byte illuminationModel = 0;
			
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
						if (firstMtl) {
							firstMtl = false;
							name = splitStr[1];
						} else {
							materials.add(new MtlMaterial(name, ambient, diffuse, specular, wSpec, dissolve, illuminationModel));
							//Set default values
							name = splitStr[1];
							ambient = null;
							diffuse = null;
							specular = null;
							wSpec = 0;
							dissolve = 0;
							illuminationModel = 0;
						}
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
	
	public String getName() {
		return name;
	}
	public Color getAmbientColor() {
		return ambient;
	}
	public Color getDiffuseColor() {
		return diffuse;
	}
	public Color getSpecularColor() {
		return specular;
	}
	public float getWeightedSpecularCoeff() {
		return wSpec;
	}
	public float getTransparency() {
		return dissolve;
	}
	public byte getIlluminationModel() {
		return illum;
	}
}