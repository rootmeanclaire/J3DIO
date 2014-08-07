package j3dio.obj;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MtlMaterial implements j3dio.Exportable {
	/**The string that will be used to reference this material.**/
	private String name;
	/**The ambient color of this material.**/
	private Color ambient;
	/**The diffuse color of this material.**/
	private Color diffuse;
	/**The specular color of this material.**/
	private Color specular;
	/**The weighted specular coefficient.**/
	private float wSpec;
	/**
	 * .mtl materials do not support true transparency. Instead
	 * they have "dissolve", which is different from true
	 * transparency because it doesn't depend upon the thickness
	 * of an object.
	**/
	private float dissolve;
	private float refraction;
	/**The illumination model of this material.**/
	private boolean[] illums = new boolean[11];
	
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
	 * The transparency or "dissolve" factor of this material
	 * @param illuminationModel
	 * The illumination models of this material.
	**/
	private MtlMaterial(
			String name,
			Color ambient,
			Color diffuse,
			Color specular,
			float weightedSpecularCoeff,
			float transparency,
			float refraction,
			boolean[] illuminationModels) {
		this.name = name;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.wSpec = weightedSpecularCoeff;
		this.dissolve = transparency;
		this.refraction = refraction;
		this.illums = illuminationModels;
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
			float refraction = 1f;
			boolean[] illuminationModels = new boolean[11];
			
			//If line is comment
			if (line.trim().startsWith("#")) {
				//TODO
				continue;
			}
			
			//If line's arguments is three floats
			if (splitStr[0].equals("Ka") || splitStr[0].equals("Kd") || splitStr[0].equals("Ks")) {
				float r = Float.parseFloat(splitStr[1]);
				float g = Float.parseFloat(splitStr[2]);
				float b = Float.parseFloat(splitStr[3]);
				
				switch(splitStr[0]) {
					//Ambient color
					case "Ka":
						ambient = new Color(r, g, b);
						break;
					//Diffuse color
					case "Kd":
						diffuse = new Color(r, g, b);
						break;
					//Specular color
					case "Ks":
						specular = new Color(r, g, b);
						break;
				}
			} else if (splitStr[0].equals("Ns") || splitStr[0].equals("d") || splitStr[0].equals("Tr") || splitStr[0].equals("Ni")) {
				float arg = Float.parseFloat(splitStr[1]);
				
				switch (splitStr[0]) {
					//Weighted specular coefficient
					case "Ns":
						wSpec = arg;
						break;
					//Transparency
					case "d":
						dissolve = arg;
						if (splitStr[1].equals("-halo")) {
							//TODO
						}
						break;
					//Transparency (alternative key)
					case "Tr":
						dissolve = arg;
						break;
					//Optical density (Index of refraction)
					case "Ni":
						refraction = arg;
						break;
					default:
						//TODO
						break;
				}
			} else {
				switch (splitStr[0]) {
					//Material definition
					case "newmtl":
						if (firstMtl) {
							firstMtl = false;
							name = splitStr[1];
						} else {
							materials.add(new MtlMaterial(name, ambient, diffuse, specular, wSpec, dissolve, refraction, illuminationModels));
							//Set default values
							name = splitStr[1];
							ambient = null;
							diffuse = null;
							specular = null;
							wSpec = 0;
							dissolve = 0;
							refraction = 1f;
							illuminationModels = new boolean[11];
						}
						break;
					//Illumination model
					case "illum":
						int temp = Integer.parseInt(splitStr[1]);
						
						if (temp >= 0 && temp <= 10) {
							illuminationModels[temp] = true;
						}
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
	public float getIndexOfRefraction() {
		return refraction;
	}
	public boolean[] getIlluminationModels() {
		return illums;
	}

	@Override
	public void export(String fileName) throws FileNotFoundException {
		if (fileName.endsWith(".mtl")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".mtl");
		
		out.println("newmtl " + name);
		out.println(
			"Ka " + ambient.getRed() / 255f
			+ ' ' + ambient.getGreen() / 255f
			+ ' ' + ambient.getBlue() / 255f
		);
		out.println(
			"Kd " + diffuse.getRed() / 255f
			+ ' ' + diffuse.getGreen() / 255f
			+ ' ' + diffuse.getBlue() / 255f
		);
		out.println(
			"Ks " + diffuse.getRed() / 255f
			+ ' ' + diffuse.getGreen() / 255f
			+ ' ' + diffuse.getBlue() / 255f
		);
		out.println("Ns " + wSpec);
		out.println("d " + dissolve / 255f);
		out.println("Ni " + refraction);
		out.println("illum " + illums);
	}
	
	public static void exportGroup(String fileName, Collection<MtlMaterial> materials) throws FileNotFoundException {
		if (fileName.endsWith(".mtl")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".mtl");
		
		for (MtlMaterial mtl : materials) {
			out.println("newmtl " + mtl.name);
			out.println(
				"Ka " + mtl.ambient.getRed() / 255f
				+ ' ' + mtl.ambient.getGreen() / 255f
				+ ' ' + mtl.ambient.getBlue() / 255f
			);
			out.println(
				"Kd " + mtl.diffuse.getRed() / 255f
				+ ' ' + mtl.diffuse.getGreen() / 255f
				+ ' ' + mtl.diffuse.getBlue() / 255f
			);
			out.println(
				"Ks " + mtl.diffuse.getRed() / 255f
				+ ' ' + mtl.diffuse.getGreen() / 255f
				+ ' ' + mtl.diffuse.getBlue() / 255f
			);
			out.println("Ns " + mtl.wSpec);
			out.println("d " + mtl.dissolve / 255f);
			out.println("Ni " + mtl.getIndexOfRefraction());
			for (int i = 0; i <= 10; i++) {
				if (mtl.getIlluminationModels()[i]) {
					out.println("illum " + i);
				}
			}
		}
		
		
		out.close();
	}
}