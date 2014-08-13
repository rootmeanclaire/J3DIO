package j3dio.raw;

import j3dio.Point3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.opengl.GL2;

import org.lwjgl.opengl.GL11;


public class RawModel implements j3dio.Exportable, j3dio.LWJGLRenderable, j3dio.JOGLRenderable {
	/**A {@link List} of the vertices in this model.**/
	private List<Point3f> verts = new ArrayList<Point3f>();
	/**A {@link List} of the faces in this model. Faces link together vertices**/
	private List<RawFace> faces = new ArrayList<RawFace>();
	
	public RawModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		//Local variable to store the current line
		String line;
		
		//Check file extension
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".raw")) {
			throw new InvalidParameterException("File is not .raw");
		}
		
		//Iterate through the lines in the file
		while ((line = br.readLine()) != null) {
			//Regex pattern to match point definition
			Pattern ptrn = Pattern.compile("(\\-?\\d+(\\.?\\d)?\\d*( |\\t)+){2}(\\-?\\d+(\\.?\\d)?\\d*)");
			Matcher mtch = ptrn.matcher(line);
			//The points defined in a given line of the file
			List<Point3f> ptsInLine = new ArrayList<Point3f>();
			List<Integer> vertIndices = new ArrayList<Integer>();
			
			if (!mtch.find()) {
				System.err.println("Warning: file syntax incorrect.");
				continue;
			}
			
			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
			 * Iterate through regex matches.                                        * 
			 * If no matches are found no exception is thrown, and it falls through  *
			 * the line parsing logic and the next line is parsed                    *
			\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
			do {
				//Split apart point definition into x, y, and z
				String[] xyz = mtch.group().split("\\s+");
				//Add the point to the list of points in the current line
				ptsInLine.add(
						new Point3f(
							Float.parseFloat(xyz[0]),
							Float.parseFloat(xyz[1]),
							Float.parseFloat(xyz[2])
						)
				);
			} while (mtch.find());
			
			//Remove duplicate points
			//Iterate through points in the current line
			for (Point3f pt : ptsInLine) {
				boolean ptIsDup = false;
				
				//Iterate through points in the model
				for (Point3f vert : verts) {
					/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
					 * If the point in this line matches a point that has been defined *
					 * earlier in this file, note that the point is a duplicate and    *
					 * add the index of the original to the list of vertex indices in  *
					 * this face.                                                      *
					\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
					if (pt.equals(vert)) {
						vertIndices.add(verts.indexOf(vert));
						ptIsDup = true;
						break;
					}
				}
				
				/*
				 * If the point is not a duplicate, add the point to the list of points
				 * in this model
				 */
				if (!ptIsDup) {
					verts.add(pt);
					vertIndices.add(verts.indexOf(pt));
				}
			}
			
			faces.add(new RawFace(vertIndices));
		//End of parsing current line
		}
	}

	@Override
	public void export(String fileName) throws FileNotFoundException {
		if (fileName.endsWith(".raw")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".raw");
		
		for (RawFace face : faces) {
			for (int i : face.vertIndxs) {
				out.print(verts.get(i).x + " " + verts.get(i).y + " " + verts.get(i).z + " ");
			}
			
			out.print("\n");
		}
		
		
		out.close();
	}
	
	@Override
	public void joglrender(GL2 gl) {
		for (RawFace face : faces) {
			gl.glBegin(GL2.GL_LINE_LOOP);
				for (int i : face.vertIndxs) {
					Point3f vert = verts.get(i);
					gl.glVertex3f(vert.x, vert.y, vert.z);
				}
			gl.glEnd();
		}
	}

	@Override
	public void lwjglrender() {
		for (RawFace face : faces) {
			GL11.glBegin(GL11.GL_LINE_LOOP);
				for (int i : face.vertIndxs) {
					Point3f vert = verts.get(i);
					GL11.glVertex3f(vert.x, vert.y, vert.z);
				}
			GL11.glEnd();
		}
	}
}