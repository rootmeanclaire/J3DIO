package j3dio;

/**
 * Texture coordinate
 * @author Evan Shimoniak
 * @since 4.1 beta
**/
public class UV {
	public float u;
	public float v;
	
	public UV(float u, float v) {
		this.u = u;
		this.v = v;
	}
	public UV(float[] uv) {
		this.u = uv[0];
		this.v = uv[1];
	}
}