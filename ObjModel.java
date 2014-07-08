package obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjModel {
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
	private List<MtlMaterial> mtls = new ArrayList<MtlMaterial>();
	
	public ObjModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".obj")) {
			throw new InvalidParameterException("File is not .obj");
		}
		
		while ((line = br.readLine()) != null) {
			String[] splitStr = line.split("\\s");
			
			//If line's arguments is three floats
			if (splitStr[0] == "v" || splitStr[0] == "vt" || splitStr[0] == "vn" || splitStr[0] == "vp") {
				float x = Float.parseFloat(splitStr[1]);
				float y = Float.parseFloat(splitStr[2]);
				float z = Float.parseFloat(splitStr[3]);
				
				switch(splitStr[0]) {
					//Vertex
					case "v":
						verts.add(new Point3f(x, y, z));
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
						break;
					//Load material file
					case "mtllib":
						mtls.addAll(MtlMaterial.loadMtls(new File(splitStr[1])));
						break;
					//Use material by name
					case "usemtl":
						//TODO
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
	 * A {@link List} of the materials that were loaded from the .obj file.
	**/
	public List<MtlMaterial> getMaterials() {
		return mtls;
	}
}