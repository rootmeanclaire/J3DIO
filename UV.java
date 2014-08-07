package j3dio;

/**Texture coordinate**/
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