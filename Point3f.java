package jml.obj;

public class Point3f {
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
}