package j3dio.dae;


import j3dio.Point3f;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.jogamp.opengl.GL2;

/**
 * @author Evan Shimoniak
 * @since 5.0 beta
 * @see <a href="http://www.khronos.org/files/collada_spec_1_4.pdf">COLLADA File Specification</a>
**/
public class DaeModel implements j3dio.Exportable, j3dio.LWJGLRenderable, j3dio.JOGLRenderable {
	enum Axis {X, Y, Z};
	
	Axis upAxis;
	/**A {@link List} of the vertices in this model.**/
	private List<Point3f> verts = new ArrayList<Point3f>();
	/**A {@link List} of the normals in this model.**/
	private List<Point3f> norms = new ArrayList<Point3f>();
	/**A {@link List} of the faces in this model.**/
	private List<DaeFace> faces = new ArrayList<DaeFace>();
	
	/**
	 * @param file The file to be loaded
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws JAXBException
	**/
	public DaeModel(File file) throws IOException, ParserConfigurationException, SAXException, JAXBException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		dbFactory.setValidating(true);
		dbFactory.setNamespaceAware(true);
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL schemaURL = new URL("http://www.khronos.org/files/collada_schema_1_4_1.xsd");
		Schema schema = sf.newSchema(schemaURL);
		dbFactory.setSchema(schema);
		
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		docBuilder.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException e) throws SAXException {
//				System.err.print("Warning: ");
//				e.getMessage();
			}
			
			@Override
			public void fatalError(SAXParseException e) throws SAXException {
				System.err.print("FATAL ERROR: ");
				e.printStackTrace();
			}
			
			@Override
			public void error(SAXParseException e) throws SAXException {
//				System.err.print("Error: ");
//				e.getMessage();
			}
		});
		Document doc = docBuilder.parse(file);
		
		doc.getDocumentElement().normalize();
		
		//Metadata
		{
			Element asset = (Element) doc.getElementsByTagName("asset").item(0);
			
			//Get up axis
			//If file has no up axis, y is default
			try {
				char upAxisChar = asset.getElementsByTagName("up_axis")
					//Get first up_axis tag
					.item(0)
					//Get text content of tag
					.getFirstChild().getTextContent()
					//Remove "_UP" from end
					.charAt(0);
				upAxis = Axis.valueOf(String.valueOf(upAxisChar));
			} catch (NullPointerException e) {
				upAxis = Axis.Y;
			}
		}
		
		//Geometry
		{
			//Collection of geometric objects
			Node geomLib = doc.getElementsByTagName("library_geometries").item(0);
			//Valid contents of geomLib
			NodeList geometries = ((Element) geomLib).getElementsByTagName("geometry");
			
			//Iterate through and read geometric objects in geomLib
			for (int i = 0; i < geometries.getLength(); i++) {
				Element geom = (Element) geometries.item(i);
				Element mesh = (Element) geom.getElementsByTagName("mesh").item(0);
				
				if (mesh == null) {
					System.err.println("Non-mesh geometries are not yet supported.");
					continue;
				}
				
				//Faces
				{
					//Get polygon list of the geometry
					Element rootTag = (Element) mesh.getElementsByTagName("polylist").item(0);
					//Get the number of faces in the geometry
					int numFaces = Integer.parseInt(rootTag.getAttribute("count"));
					List<Point3f> verts = null;
					List<Point3f> norms = null;
					List<DaeFace> faces = new ArrayList<DaeFace>();
					//The data input channels
					NodeList inputs = rootTag.getElementsByTagName("input");
					
					for (int j = 0; j < inputs.getLength(); j++) {
						//If input should be parsed as vertices
						if (((Element) inputs.item(j)).getAttribute("semantic").equalsIgnoreCase("VERTEX")) {
							String src = ((Element) inputs.item(j)).getAttribute("source");
							
							if (src != null) {
								Element vertTag = doc.getElementById(src.substring(1));
								
								if (vertTag != null) {
									verts = srcToVerts(
										doc.getElementById(
											(
												(Element) vertTag.getElementsByTagName("input").item(0)
											).getAttribute("source").substring(1)
										)
									);
								} else {
									System.err.println("Cannot find element with id \"" + src.substring(1) + "\"");
								}
							}
						//If input should be parsed as normals
						} else if (((Element) inputs.item(j)).getAttribute("semantic").equalsIgnoreCase("NORMAL")) {
							String src = ((Element) inputs.item(j)).getAttribute("source");
							
							if (src != null) {
								Element normTag = doc.getElementById(src.substring(1));
								
								if (normTag != null) {
									norms = srcToVerts(normTag);
								} else {
									System.err.println("Cannot find element with id \"" + src.substring(1) + "\"");
								}
							}
						} else if (((Element) inputs.item(j)).getAttribute("semantic") != null) {
							System.err.println("Unrecognized semantic");
						} else {
							System.err.println("No semantic");
						}
					}
					
					if (verts == null) {
						throw new DaeException("No vertices in face");
					}
					
					String[] vertCounts = rootTag.getElementsByTagName("vcount").item(0).getTextContent().split("\\s|\\n");
					String[] indexes = rootTag.getElementsByTagName("p").item(0).getTextContent().split("\\s|\\n");
					int indexesSequenced = 0;
					
					for (int j = 0; j < numFaces; j++) {
						int numVerts = Integer.parseInt(vertCounts[j]);
						int[] faceInds = new int[numVerts];
						
						for (int k = 0; k < numVerts; k++) {
							faceInds[k] = Integer.parseInt(indexes[indexesSequenced]);
							indexesSequenced++;
						}
						
						if (norms != null) {
							faces.add(new DaeFace(faceInds, faceInds));
						} else {
							faces.add(new DaeFace(faceInds));
						}
					}
					
					this.verts.addAll(verts);
					this.norms.addAll(norms);
					this.faces.addAll(faces);
				}
			}
		}
	}
	
	private static List<Point3f> srcToVerts(Element source) {
		List<Point3f> verts = new ArrayList<Point3f>();
		
		Element accessor =
			(Element) (
				(Element) (
					source.getElementsByTagName("technique_common").item(0)
				)
			).getElementsByTagName("accessor").item(0);
			//
//			.getFirstChild();
		if (accessor.getTagName() != "accessor") {
			//TODO
		}
		
		Element ary = (Element) source.getOwnerDocument().getElementById(accessor.getAttribute("source").substring(1));
		
		//Check if vertex accessor is set up correctly
		{
			NodeList params = accessor.getElementsByTagName("param");
			
			if (!(((Element) params.item(0)).getAttribute("name").equalsIgnoreCase("X")
					&& ((Element) params.item(1)).getAttribute("name").equalsIgnoreCase("Y")
					&& ((Element) params.item(2)).getAttribute("name").equalsIgnoreCase("Z"))) {
				throw new DaeException("Invalid vertex source");
			}
		}
		
		if (ary.getTagName() == "float_array" || ary.getTagName() == "int_array") {
			try {
				float[] data = new float[Integer.parseInt(ary.getAttribute("count"))];
				
				String splitAry[] = ary.getTextContent().split("\\s|\\n");
				
				//Populate data
				for (int i = 0; i < data.length; i++) {
					data[i] = Float.parseFloat(splitAry[i]);
				}
				
				//Parse data
				for (int i = 0; i < Integer.parseInt(accessor.getAttribute("count")); i++) {
					verts.add(new Point3f(
						data[(i * 3) + 0],
						data[(i * 3) + 1],
						data[(i * 3) + 2]
					));
				}
			} catch (NumberFormatException e) {
				System.err.println(".dae Syntax Error: Non-Numeric Value \""
					+ ary.getAttribute("count") + "\" of Attribute \"count\"");
			}
		} else {
			
		}
		
		
		return verts;
	}

	@Override
	public void export(String fileName) throws IOException {
		if (fileName.endsWith(".dae")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".dae");
		
		//Header
		out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.println("<COLLADA xmlns=\"http://www.collada.org/2005/11/COLLADASchema\" version=\"1.4.1\">");
		
		//Metadata
		out.println("\t<asset>");
		out.println("\t\t<up_axis>" + upAxis.toString() + "_UP</up_axis>");
		out.println("\t</asset>");
		
		//Cameras
		//TODO
		out.println("\t<library_cameras />");
		
		//Lights
		//TODO
		out.println("\t<library_lights />");
		
		//Images
		//TODO
		out.println("\t<library_images />");
		
		//Effects
		//TODO
		out.println("\t<library_effects />");
		
		//Materials
		//TODO
		out.println("\t<library_materials />");
		
		//Geometries
		out.println("\t<library_geometries>");
		out.println("\t\t<geometry>");
		out.println("\t\t\t<mesh>");
		
		//Vertex Data
		out.println("\t\t\t\t<source id=\"mesh-positions\">");
		out.print("\t\t\t\t\t<float_array id=\"mesh-positions-array\" count=\"" + verts.size() * 3 + "\">");
		for (Point3f vert : verts) {
			out.printf("%f %f %f ", vert.x, vert.y, vert.z);
		}
		out.print("</float_array>\n");
		out.println("\t\t\t\t\t<technique_common>");
		out.println(makeXYZAccessor("#mesh-positions-array", verts.size(), 6));
		out.println("\t\t\t\t\t</technique_common>");
		out.println("\t\t\t\t</source>");
		
		//Normal Data
		out.println("\t\t\t\t<source id=\"mesh-normals\">");
		out.print("\t\t\t\t\t<float_array id=\"mesh-normals-array\" count=\"" + norms.size() * 3 + "\">");
		for (Point3f norm : norms) {
			out.printf("%f %f %f ", norm.x, norm.y, norm.z);
		}
		out.print("</float_array>\n");
		out.println("\t\t\t\t\t<technique_common>");
		out.println(makeXYZAccessor("#mesh-normals-array", norms.size(), 6));
		out.println("\t\t\t\t\t</technique_common>");
		out.println("\t\t\t\t</source>");
		
		//Vertices
		out.println("\t\t\t\t<vertices id=\"mesh-vertices\">");
		out.println("\t\t\t\t\t<input semantic=\"POSITION\" source=\"#mesh-positions\"/>");
		out.println("\t\t\t\t</vertices>");
		
		//Polylist
		out.println("\t\t\t\t<polylist count=\"" + faces.size() + "\">");
		out.println("\t\t\t\t\t<input semantic=\"VERTEX\" source=\"#mesh-vertices\" offset=\"0\"/>");
		out.println("\t\t\t\t\t<input semantic=\"NORMAL\" source=\"#mesh-normals\" offset=\"0\"/>");
		out.print("\t\t\t\t\t<vcount>");
		for (DaeFace face : faces) {
			out.printf("%i ", face.size);
		}
		out.print("</vcount>\n");
		out.print("\t\t\t\t\t<p>");
		for (DaeFace face : faces) {
			for (int i : face.vertIndxs) {
				out.printf("%i ", i);
			}
		}
		out.print("</p>\n");
		out.println("\t\t\t\t</polylist>");
		
		out.println("\t\t\t</mesh>");
		out.println("\t\t</geometry>");
		out.println("\t</library_geometries>");
		
		
		//Animations
		//TODO
		out.println("\t<library_animations />");
		
		//Controllers
		//TODO
		out.println("\t<library_controllers />");
		
		//Visual Scenes
		//TODO
		out.println("\t<library_visual_scenes />");
		
		//Scene
		//TODO
		
		out.println("</COLLADA>");
		out.close();
	}

	@Override
	public void joglrender(GL2 gl) {
		for (DaeFace face : faces) {
			gl.glBegin(GL2.GL_POLYGON);
				for (int i = 0; i < face.size; i++) {
					if (face.hasNorms()) {
						Point3f norm = norms.get(face.normIndxs[i]);
						gl.glNormal3f(norm.x, norm.y, norm.z);
					}
					{
						Point3f vert = verts.get(face.vertIndxs[i]);
						gl.glVertex3f(vert.x, vert.y, vert.z);
					}
				}
			gl.glEnd();
		}
	}

	@Override
	public void lwjglrender() {
		for (DaeFace face : faces) {
			GL11.glBegin(GL11.GL_POLYGON);
				for (int i = 0; i < face.size; i++) {
					if (face.hasNorms()) {
						Point3f norm = norms.get(face.normIndxs[i]);
						GL11.glNormal3f(norm.x, norm.y, norm.z);
					}
					{
						Point3f vert = verts.get(face.vertIndxs[i]);
						GL11.glVertex3f(vert.x, vert.y, vert.z);
					}
				}
			GL11.glEnd();
		}
	}
	
	private static String makeXYZAccessor(String source, int count, int indentation) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < indentation; i++)
			sb.append("\t");
		sb.append("<accessor source=\"" + source + "\" count=\"" + count + "\" stride=\"3\">\n");
		
		for (int i = 0; i < indentation + 1; i++)
			sb.append("\t");
		sb.append("<param name=\"X\" type=\"float\"/>");
		for (int i = 0; i < indentation + 1; i++)
			sb.append("\t");
		sb.append("<param name=\"Y\" type=\"float\"/>");
		for (int i = 0; i < indentation + 1; i++)
			sb.append("\t");
		sb.append("<param name=\"Z\" type=\"float\"/>");
		
		for (int i = 0; i < indentation; i++)
			sb.append("\t");
		sb.append("</accessor>\n");
		
		
		return sb.toString();
	}
}