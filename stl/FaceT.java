package j3dio.stl;

import java.nio.ByteBuffer;

import j3dio.Byteable;
import j3dio.Point3f;

/**
 * @author Evan Shimoniak
 * @since 4.0
**/
public class FaceT implements Byteable {
	public Point3f normal = new Point3f();
	public Point3f vert1 = null;
	public Point3f vert2 = null;
	public Point3f vert3 = null;
	public short attribByteCount = 0;
	
	public FaceT(Point3f normal, Point3f[] verts) {
		this(normal, verts, (short) 0);
	}
	public FaceT(Point3f normal, Point3f vert1, Point3f vert2, Point3f vert3) {
		this(normal, vert1, vert2, vert3, (short) 0);
	}
	public FaceT(Point3f normal, Point3f[] verts, short attributeByteCount) {
		this.normal = normal;
		this.vert1 = verts[0];
		this.vert2 = verts[1];
		this.vert3 = verts[2];
		this.attribByteCount = attributeByteCount;
	}
	public FaceT(Point3f normal, Point3f vert1, Point3f vert2, Point3f vert3, short attributeByteCount) {
		this.normal = normal;
		this.vert1 = vert1;
		this.vert2 = vert2;
		this.vert3 = vert3;
		this.attribByteCount = attributeByteCount;
	}
	public FaceT(String definition) {
		String[] lines = definition.split("\\n+");
		boolean inLoop = false;
		
		for (String line : lines) {
			String[] splitLn = line.split("\\s+");
			
			switch (splitLn[0]) {
				case "facet":
					if (splitLn[1].equals("normal")) {
						normal = new Point3f(Float.parseFloat(splitLn[2]), Float.parseFloat(splitLn[3]), Float.parseFloat(splitLn[4]));
					}
					break;
				case "outer":
					if (splitLn[1].equals("loop")) {
						inLoop = true;
					}
					break;
				case "endloop":
					inLoop = false;
					break;
				case "vertex":
					if (inLoop) {
						if (vert1 == null) {
							vert1 = new Point3f(Float.parseFloat(splitLn[1]), Float.parseFloat(splitLn[2]), Float.parseFloat(splitLn[3]));
						} else if (vert2 == null) {
							vert2 = new Point3f(Float.parseFloat(splitLn[1]), Float.parseFloat(splitLn[2]), Float.parseFloat(splitLn[3]));
						} else if (vert3 == null) {
							vert3 = new Point3f(Float.parseFloat(splitLn[1]), Float.parseFloat(splitLn[2]), Float.parseFloat(splitLn[3]));
						} else {
							//TODO
						}
					} else {
						//TODO?
					}
					break;
				case "endfacet":
					return;
			}
		}
	}
	
	@Override
	public byte[] toBytes() {
		byte[] allbytes = new byte[getByteSize()];
		int i = 0;
		
		byte[] normBytes = normal.toBytes();
		byte[] vert1Bytes = vert1.toBytes();
		byte[] vert2Bytes = vert2.toBytes();
		byte[] vert3Bytes = vert3.toBytes();
		byte[] abcBytes = ByteBuffer.allocate(2).putShort(attribByteCount).array();
		
		for (byte b : normBytes) {
			allbytes[i] = b;
			i++;
		}
		for (byte b : vert1Bytes) {
			allbytes[i] = b;
			i++;
		}
		for (byte b : vert2Bytes) {
			allbytes[i] = b;
			i++;
		}
		for (byte b : vert3Bytes) {
			allbytes[i] = b;
			i++;
		}
		for (byte b : abcBytes) {
			allbytes[i] = b;
			i++;
		}
		
		
		return allbytes;
	}
	
	@Override
	public int getByteSize() {
		return 50;
	}
}
