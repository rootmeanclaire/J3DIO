package jml.raw;

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

import jml.Exportable;
import jml.GLRenderable;
import jml.Point3f;

import static org.lwjgl.opengl.GL11.*;

public class RawModel implements Exportable, GLRenderable {
	private List<Point3f> verts = new ArrayList<Point3f>();
	private List<RawFace> faces = new ArrayList<RawFace>();
	
	public RawModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".raw")) {
			throw new InvalidParameterException("File is not .raw");
		}
		
		while ((line = br.readLine()) != null) {
			Pattern ptrn = Pattern.compile("(\\-{0,1}\\d+\\.\\d* ){3}");
			Matcher mtch = ptrn.matcher(line);
			List<Point3f> ptsInLine = new ArrayList<Point3f>();
			List<Integer> vertIndices = new ArrayList<Integer>();
			
			while (mtch.find()) {
				String[] xyz = mtch.group().split("\\s");
				ptsInLine.add(new Point3f(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2])));
			}
			
			for (Point3f pt : ptsInLine) {
				boolean ptIsDup = false;
				
				for (Point3f vert : verts) {
					if (pt.equals(vert)) {
						vertIndices.add(verts.indexOf(vert));
						ptIsDup = true;
						break;
					}
				}
				
				if (!ptIsDup) {
					verts.add(pt);
					vertIndices.add(verts.indexOf(pt));
				}
			}
			
			faces.add(new RawFace(vertIndices));
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
	public void render() {
		for (RawFace face : faces) {
			glBegin(GL_LINE_LOOP);
				for (int i : face.vertIndxs) {
					Point3f vert = verts.get(i);
					glVertex3f(vert.x, vert.y, vert.z);
				}
			glEnd();
		}
	}
}