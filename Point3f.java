package j3dio;

import java.nio.ByteBuffer;

public class Point3f implements Byteable {
	public float x;
	public float y;
	public float z;
	
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
	public Point3f(float[] pts) {
		if (pts.length >= 1) {
			x = pts[0];
		}
		if (pts.length >= 2) {
			y = pts[1];
		}
		if (pts.length >= 3) {
			z = pts[2];
		}
	}
	
	public boolean equals(Point3f pt) {
		return x == pt.x && y == pt.y && z == pt.z;
	}
	
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