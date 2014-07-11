package jml.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjModel implements jml.Exportable {
	/**
	 * A {@link List} of the vertices in this model.
	**/
	private List<Point3f> verts = new ArrayList<Point3f>();
	/**
	 * A {@link List} of the texture coordinates(UV's) in this model.
	**/
	private List<Point3f> txtrs = new ArrayList<Point3f>();
	/**
	 * A {@link List} of the normals in this model.
	**/
	private List<Point3f> norms = new ArrayList<Point3f>();
	/**
	 * A {@link List} of the parameter space vertices in this model.
	**/
	private List<Point3f> paramSVs = new ArrayList<Point3f>();
	/**
	 * A {@link List} of the faces in this model. Faces link together vertices, textures, and normals.
	**/
	private List<Face> faces = new ArrayList<Face>();
	/**
	 * A {@link List} of the materials that were loaded from the .obj file.
	**/
	private Map<String, MtlMaterial> mtls = new HashMap<String, MtlMaterial>();
	
	public ObjModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String currMtlName = null;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".obj")) {
			throw new InvalidParameterException("File is not .obj");
		}
		
		while ((line = br.readLine()) != null) {
			String[] splitStr = line.split("\\s");
			
			//If line's arguments is three floats
			if (splitStr[0].equals("v") || splitStr[0].equals("vt") || splitStr[0].equals("vn") || splitStr[0].equals("vp")) {
				float x = Float.parseFloat(splitStr[1]);
				float y = Float.parseFloat(splitStr[2]);
				float z = Float.parseFloat(splitStr[3]);
				
				switch(splitStr[0]) {
					//Vertex
					case "v":
						if (currMtlName == null) {
							verts.add(new Point3f(x, y, z));
						} else {
							verts.add(new MtlPoint3f(x, y, z, mtls.get(currMtlName)));
							currMtlName = null;
						}
						break;
					//Vertex texture
					case "vt":
						txtrs.add(new Point3f(x, y, z));
						break;
					//Vertex normal
					case "vn":
						norms.add(new Point3f(x, y, z));
						break;
					//Parameter space vertices
					case "vp":
						paramSVs.add(new Point3f(x, y, z));
						break;
				}
			} else {
				switch (splitStr[0]) {
					//Comment
					case "#":
						//TODO
						break;
					//Face
					case "f":
						faces.add(new Face(Arrays.copyOfRange(splitStr, 1, splitStr.length)));
						
						if (currMtlName != null) {
							for (int i : faces.get(faces.size() - 1).vertIndxs) {
								verts.set(i, new MtlPoint3f(verts.get(i), mtls.get(currMtlName)));
							}
							currMtlName = null;
						}
						break;
					//Load material file
					case "mtllib":
						List<MtlMaterial> materials = MtlMaterial.loadMtls(new File(splitStr[1]));
						for (MtlMaterial mtl : materials) {
							mtls.put(mtl.getName(), mtl);
						}
						break;
					//Use material by name
					case "usemtl":
						currMtlName = splitStr[1];
						break;
					//Define object name
					case "o":
						//TODO
						break;
					//Define group name
					case "g":
						//TODO
						break;
					//Smoothing groups
					case "s":
						if (splitStr[1] == "off") {
							//TODO
						} else {
							Integer.parseInt(splitStr[1]);
							//TODO
						}
						break;
					default:
						//TODO
						break;
				}
			}
		}
		
		
		br.close();
	}
	
	
	/**
	 * A {@link List} of the vertices in this model.
	**/
	public List<Point3f> getVertices() {
		return verts;
	}
	/**
	 * A {@link List} of the texture coordinates(UV's) in this model.
	**/
	public List<Point3f> getTextureCoords() {
		return txtrs;
	}
	/**
	 * A {@link List} of the normals in this model.
	**/
	public List<Point3f> getNormals() {
		return norms;
	}
	/**
	 * A {@link List} of the parameter space vertices in this model.
	**/
	public List<Point3f> getParamSpaceVerts() {
		return paramSVs;
	}
	/**
	 * A {@link List} of the faces in this model. Faces link together vertices, textures, and normals.
	**/
	public List<Face> getFaces() {
		return faces;
	}
	/**
	 * An associative array of the materials loaded in the obj file. The keys are the names of the materials
	**/
	public Map<String, MtlMaterial> getMaterials() {
		return mtls;
	}


	@Override
	public void export(String fileName) throws FileNotFoundException {
		if (fileName.endsWith(".obj") || fileName.endsWith(".mtl")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out;
		if (!mtls.isEmpty()) {
			out = new PrintWriter(fileName + ".mtl");
			
			for (MtlMaterial mtl : mtls.values()) {
				out.println("newmtl " + mtl.getName());
				
				out.println(
					"Ka " + mtl.getAmbientColor().getRed() / 255f
					+ ' ' + mtl.getAmbientColor().getGreen() / 255f
					+ ' ' + mtl.getAmbientColor().getBlue() / 255f
				);
				
				out.println(
					"Kd " + mtl.getDiffuseColor().getRed() / 255f
					+ ' ' + mtl.getDiffuseColor().getGreen() / 255f
					+ ' ' + mtl.getDiffuseColor().getBlue() / 255f
				);
				
				out.println(
					"Ks " + mtl.getSpecularColor().getRed() / 255f
					+ ' ' + mtl.getSpecularColor().getGreen() / 255f
					+ ' ' + mtl.getSpecularColor().getBlue() / 255f
				);
				out.println("Ns " + mtl.getWeightedSpecularCoeff());
				
				out.println("d " + mtl.getTransparency() / 255f);
				
				out.println("illum " + mtl.getIlluminationModel());
			}
		}
		{
			out = new PrintWriter(fileName + ".obj");
			for (Point3f vert : verts) {
				if (vert instanceof MtlPoint3f) {
					out.print("usemtl " + ((MtlPoint3f) vert).mtl.getName());
				}
				out.println("v " + vert.x + ' ' + vert.y + ' ' + vert.z);
			}
			for (Point3f txtr : txtrs) {
				out.println("vt " + txtr.x + ' ' + txtr.y + ' ' + txtr.z);
			}
			for (Point3f norm : norms) {
				out.println("vn " + norm.x + ' ' + norm.y + ' ' + norm.z);
			}
			for (Point3f paramSv : paramSVs) {
				out.println("vp " + paramSv.x + ' ' + paramSv.y + ' ' + paramSv.z);
			}
			for (Face face : faces) {
				if (face.hasTextures() && face.hasNorms()) {
					StringBuilder sb = new StringBuilder("f");
					for (int i = 0; i < face.size; i++) {
						sb.append(" " + face.vertIndxs[i] + "/" + face.txtrIndxs + "/" + face.normIndxs);
					}
					out.println(sb.toString());
				} else if (face.hasTextures()) {
					StringBuilder sb = new StringBuilder("f");
					for (int i = 0; i < face.size; i++) {
						sb.append(" " + face.vertIndxs[i] + "/" + face.txtrIndxs);
					}
					out.println(sb.toString());
				} else if (face.hasNorms()) {
					StringBuilder sb = new StringBuilder("f");
					for (int i = 0; i < face.size; i++) {
						sb.append(" " + face.vertIndxs[i] + "//" + face.normIndxs);
					}
					out.println(sb.toString());
				} else {
					StringBuilder sb = new StringBuilder("f");
					for (int i = 0; i < face.size; i++) {
						sb.append(" " + face.vertIndxs[i]);
					}
					out.println(sb.toString());
				}
			}
		}
		
		
		out.close();
	}
}