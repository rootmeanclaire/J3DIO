package j3dio.obj;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import j3dio.Point3f;
import j3dio.UV;

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

public class ObjModel implements j3dio.Exportable, j3dio.GLRenderable {
	/**A {@link List} of the vertices in this model.**/
	private List<Point3f> verts = new ArrayList<Point3f>();
	/**A {@link List} of the texture coordinates(UV's) in this model.**/
	private List<UV> txtrs = new ArrayList<UV>();
	/**A {@link List} of the normals in this model.**/
	private List<Point3f> norms = new ArrayList<Point3f>();
	/**A {@link List} of the parameter space vertices in this model.**/
	private List<Point3f> paramSVs = new ArrayList<Point3f>();
	/**
	 * A {@link List} of the faces in this model. Faces link together
	 * vertices, textures, and normals.
	**/
	private List<ObjFace> faces = new ArrayList<ObjFace>();
	/**A {@link List} of the materials that were loaded from the .obj file.**/
	private Map<String, MtlMaterial> mtls = new HashMap<String, MtlMaterial>();
	
	/**
	 * @param verts
	 * A {@link List} of the vertices in this model.
	 * @param txtrs
	 * A {@link List} of the texture coordinates(UV's) in this model.
	 * @param norms
	 * A {@link List} of the normals in this model.
	 * @param paramSVs
	 * A {@link List} of the parameter space vertices in this model.
	 * @param faces
	 * A {@link List} of the faces in this model. Faces link together
	 * vertices, textures, and normals.
	 * @param mtls
	 * A {@link List} of the materials that were loaded from the .obj file.
	**/
	@SuppressWarnings("unused")
	private ObjModel(List<Point3f> vertices,
			List<UV> textures,
			List<Point3f> normals,
			List<Point3f> paramaterSpaceVertices,
			List<ObjFace> faces,
			Map<String, MtlMaterial> materials) {
		this.verts = vertices;
		this.txtrs = textures;
		this.paramSVs = paramaterSpaceVertices;
		this.faces = faces;
		this.mtls = materials;
	}
	
	public ObjModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String currMtlName = null;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".obj")) {
			throw new InvalidParameterException("File is not .obj");
		}
		
		while ((line = br.readLine()) != null) {
			String[] splitln = line.split("\\s+");
			
			//If line is comment
			if (line.trim().startsWith("#")) {
				//TODO
				continue;
			}
			
			//If line's arguments is three floats
			if (splitln[0].equals("v") || splitln[0].equals("vn") || splitln[0].equals("vp")) {
				float x = Float.parseFloat(splitln[1]);
				float y = Float.parseFloat(splitln[2]);
				float z = Float.parseFloat(splitln[3]);
				
				switch(splitln[0]) {
					//Vertex
					case "v":
						if (currMtlName == null) {
							verts.add(new Point3f(x, y, z));
						} else {
							verts.add(new MtlPoint3f(x, y, z, mtls.get(currMtlName)));
							
						}
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
				switch (splitln[0]) {
					//Texture coordinate
					case "vt":
						txtrs.add(new UV(Float.parseFloat(splitln[1]), Float.parseFloat(splitln[2])));
						break;
					//Face
					case "f":
						faces.add(new ObjFace(Arrays.copyOfRange(splitln, 1, splitln.length)));
						
						if (currMtlName != null) {
							for (int i : faces.get(faces.size() - 1).vertIndxs) {
								verts.set(i, new MtlPoint3f(verts.get(i), mtls.get(currMtlName)));
							}
							
						}
						break;
					//Load material file
					case "mtllib":
						List<MtlMaterial> materials = MtlMaterial.loadMtls(
							new File(
								file.getPath().substring(
									0,
									file.getPath().length() - file.getName().length()
								)
								+ splitln[1]
							)
						);
						for (MtlMaterial mtl : materials) {
							mtls.put(mtl.getName(), mtl);
						}
						break;
					//Use material by name
					case "usemtl":
						currMtlName = splitln[1];
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
						if (splitln[1].equals("off")) {
							//TODO
						} else {
							Integer.parseInt(splitln[1]);
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
	
	/**@return A {@link List} of the vertices in this model.**/
	public List<Point3f> getVertices() {
		return verts;
	}
	/**@return A {@link List} of the texture coordinates(UV's) in this model.**/
	public List<UV> getTextureCoords() {
		return txtrs;
	}
	/**@return A {@link List} of the normals in this model.**/
	public List<Point3f> getNormals() {
		return norms;
	}
	/**@return A {@link List} of the parameter space vertices in this model.**/
	public List<Point3f> getParamSpaceVerts() {
		return paramSVs;
	}
	/**
	 * @return A {@link List} of the faces in this model. Faces link together
	 * vertices, textures, and normals.
	**/
	public List<ObjFace> getFaces() {
		return faces;
	}
	/**
	 * @return An associative array of the materials loaded in the obj file.
	 * The keys are the names of the materials
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
			MtlMaterial.exportGroup(fileName, mtls.values());
		}
		{
			out = new PrintWriter(fileName + ".obj");
			for (Point3f vert : verts) {
				if (vert instanceof MtlPoint3f) {
					out.print("usemtl " + ((MtlPoint3f) vert).mtl.getName());
				}
				out.println("v " + vert.x + ' ' + vert.y + ' ' + vert.z);
			}
			for (UV txtr : txtrs) {
				out.println("vt " + txtr.u + ' ' + txtr.v);
			}
			for (Point3f norm : norms) {
				out.println("vn " + norm.x + ' ' + norm.y + ' ' + norm.z);
			}
			for (Point3f paramSv : paramSVs) {
				out.println("vp " + paramSv.x + ' ' + paramSv.y + ' ' + paramSv.z);
			}
			for (ObjFace face : faces) {
				out.println(face.toDefinition());
			}
		}
		
		
		out.close();
	}
	
	/**
	 * <b>DEPRECATED</b> use <code>glrender()</code> instead
	 * <br />
	 * Does not support parameter space vertices or .mtl materials
	**/
	@Deprecated
	@Override
	public void render() {
		glrender();
	}
	
	/**Does not support parameter space vertices or .mtl materials**/
	@Override
	public void glrender() {
		for (ObjFace face : faces) {
			glBegin(GL_LINE_LOOP);
				for (int i = 0; i < face.size; i++) {
					if (face.hasNorms()) {
						Point3f norm = norms.get(face.normIndxs[i]);
						glNormal3f(norm.x, norm.y, norm.z);
					}
					if (face.hasTextures()) {
						Point3f txtr = norms.get(face.txtrIndxs[i]);
						glTexCoord3f(txtr.x, txtr.y, txtr.z);
					}
					{
						Point3f vert = verts.get(i);
						glVertex3f(vert.x, vert.y, vert.z);
					}
				}
			glEnd();
		}
	}
}