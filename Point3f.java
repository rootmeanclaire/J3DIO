package j3dio;

import java.nio.ByteBuffer;

/**
 * @author Evan Shimoniak
 * @since 1.0 beta
**/
public class Point3f implements Byteable {
	public float x, y, z;
	
	/**Initializes a point at the origin (0, 0, 0)**/
	public Point3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	public Point3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * @param pts A float array containing the x, y, and z coordinates as follows:<br />
	 * pts[0] = x<br />
	 * pts[1] = y<br />
	 * pts[2] = z<br />
	 * If the array is too short, missing indices will be set to 0
	**/
	public Point3f(float[] pts) {
		if (pts.length >= 1) {
			x = pts[0];
		} else {
			x = 0;
			y = 0;
			z = 0;
		}
		
		if (pts.length >= 2) {
			y = pts[1];
		} else {
			y = 0;
			z = 0;
		}
		
		if (pts.length >= 3) {
			z = pts[2];
		} else {
			z = 0;
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + "@" + Integer.toHexString(super.hashCode()) + "{" + x + "," + y + "," + z + "}";
	}
	
	public boolean equals(Point3f pt) {
		return x == pt.x && y == pt.y && z == pt.z;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] allBytes = new byte[getByteSize()];
		int i = 0;
		
		byte[] xbytes = ByteBuffer.allocate(4).putFloat(x).array();
		byte[] ybytes = ByteBuffer.allocate(4).putFloat(y).array();
		byte[] zbytes = ByteBuffer.allocate(4).putFloat(z).array();
		
		for (byte b : xbytes) {
			allBytes[i] = b;
			i++;
		}
		for (byte b : ybytes) {
			allBytes[i] = b;
			i++;
		}
		for (byte b : zbytes) {
			allBytes[i] = b;
			i++;
		}
		
		
		return allBytes;
	}
	
	@Override
	public int getByteSize() {
		return 12;
	}
}