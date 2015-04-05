package j3dio.stl;

import j3dio.Point3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.jogamp.opengl.GL2;

/**
 * @author Evan Shimoniak
 * @since 4.0
**/
public class StlModel implements j3dio.Exportable, j3dio.Byteable, j3dio.LWJGLRenderable, j3dio.JOGLRenderable {
	public final String header;
	private List<FaceT> faces = new ArrayList<FaceT>();
	
	/**
	 * @param file The file to be loaded
	 * @throws IOException
	**/
	public StlModel(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		if (!file.getPath().substring(file.getPath().lastIndexOf('.')).equals(".stl")) {
			throw new InvalidParameterException("File is not .stl");
		}
		
		line = br.readLine();
		
		//If file is ASCII
		if (line.startsWith("solid ")) {
			boolean readingFace = false;
			StringBuilder faceSb = new StringBuilder();
			
			header = line.substring(6);
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				if (line.startsWith("facet")) {
					readingFace = true;
					faceSb.append(line + "\n");
				} else if (line.equals("endfacet")) {
					faceSb.append(line + "\n");
					faces.add(new FaceT(faceSb.toString()));
					faceSb = new StringBuilder();
				} else if (line.equals("endsolid " + header)) {
					return;
				} else if (readingFace) {
					faceSb.append(line + "\n");
				}
			}
		
		//If file is binary
		} else {
			char[] headerChars = new char[80];
			Path path = Paths.get(file.getPath());
			byte[] data = Files.readAllBytes(path);
			
			for (int i = 0; i < 80; i++) {
				headerChars[i] = (char)data[i];
			}
			
			header = new String(headerChars);
			
			/*
			 * Increment by 50 because each iteration reads 4 objects,
			 * each consisting of 3 floats(4 bytes each) followed by a
			 * short(2 bytes)
			 */
			for (int i = 80; i < data.length - data.length % 50; i += 4 * 3 * 4 + 2) {
				Point3f normal, vert1, vert2, vert3;
				
				float nx = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 0, i + 4 * 1)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float ny = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 1, i + 4 * 2)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float nz = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 2, i + 4 * 3)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				
				normal = new Point3f(nx, ny, nz);
				
				float x1 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 3, i + 4 * 4)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float y1 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 4, i + 4 * 5)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float z1 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 5, i + 4 * 6)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				
				vert1 = new Point3f(x1, y1, z1);
				
				float x2 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 6, i + 4 * 7)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float y2 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 7, i + 4 * 8)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float z2 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 8, i + 4 * 9)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				
				vert2 = new Point3f(x2, y2, z2);
				
				float x3 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 9, i + 4 * 10)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float y3 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 10, i + 4 * 11)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float z3 = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 11, i + 4 * 12)
				).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				
				vert3 = new Point3f(x3, y3, z3);
				
				short abc = ByteBuffer.wrap(
					Arrays.copyOfRange(data, i + 4 * 12, i + 4 * 12 + 2)
				).order(ByteOrder.LITTLE_ENDIAN).getShort();
				
				faces.add(new FaceT(normal, vert1, vert2, vert3, abc));
			}
		}
	}

	@Override
	public void export(String fileName) throws IOException {
		exportBinary(fileName);
	}
	
	public void exportAscii(String fileName) throws IOException {
		if (fileName.endsWith(".stl")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		
		PrintWriter out = new PrintWriter(fileName + ".stl");
		
		out.print("solid ");
		if (header != null) {
			out.print(header);
		}
		out.print('\n');
		
		for (FaceT face : faces) {
			out.println("	facet normal " + face.normal.x + " " + face.normal.y + " " + face.normal.z);
			out.println("		outer loop");
			out.println("			vertex " + face.vert1.x + " " + face.vert1.y + " " + face.vert1.z);
			out.println("			vertex " + face.vert2.x + " " + face.vert2.y + " " + face.vert2.z);
			out.println("			vertex " + face.vert3.x + " " + face.vert3.y + " " + face.vert3.z);
			out.println("		endloop");
			out.println("	endfacet");
		}
		
		out.print("endsolid");
		if (header != null) {
			out.print(" " + header);
		}
		
		out.close();
	}
	
	public void exportBinary(String fileName) throws IOException {
		FileOutputStream fios = new FileOutputStream(new File(fileName));
		fios.write(this.toBytes());
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[getByteSize()];
		int i = 0;
		
		//Write to bytes
		char[] headerChars;
		
		if (header.length() < 80) {
			headerChars = new char[80];
			
			for (int j = 0; j < header.length(); j++) {
				headerChars[j] = header.charAt(j);
			}
			for (int j = header.length(); j < 80; j++) {
				headerChars[j] = '\u0000';
			}
		} else {
			headerChars = header.substring(0, 80).toCharArray();
		}
		
		for (char c : headerChars) {
			bytes[i] = (byte) c;
			i++;
		}
		
		for (FaceT face : faces) {
			byte[] faceBytes = face.toBytes();
			
			for (byte b : faceBytes) {
				bytes[i] = b;
				i++;
			}
		}
		
		return bytes;
	}
	
	@Override
	public int getByteSize() {
		return 80 + faces.size() * faces.get(0).getByteSize();
	}
	
	@Override
	public void joglrender(GL2 gl) {
		for (FaceT face : faces) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
				gl.glVertex3f(face.vert1.x, face.vert1.y, face.vert1.z);
				gl.glVertex3f(face.vert2.x, face.vert2.y, face.vert2.z);
				gl.glVertex3f(face.vert3.x, face.vert3.y, face.vert3.z);
			gl.glEnd();
		}
	}
	
	@Override
	public void lwjglrender() {
		for (FaceT face : faces) {
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glVertex3f(face.vert1.x, face.vert1.y, face.vert1.z);
				GL11.glVertex3f(face.vert2.x, face.vert2.y, face.vert2.z);
				GL11.glVertex3f(face.vert3.x, face.vert3.y, face.vert3.z);
			GL11.glEnd();
		}
	}
}
