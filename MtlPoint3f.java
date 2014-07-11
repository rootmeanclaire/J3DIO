package jml.obj;

public class MtlPoint3f extends Point3f {
	public MtlMaterial mtl = null;
	
	public MtlPoint3f(float x, float y, float z, MtlMaterial material) {
		super(x, y, z);
		this.mtl = material;
	}
	public MtlPoint3f(Point3f pt, MtlMaterial material) {
		super(pt.x, pt.y, pt.z);
		this.mtl = material;
	}
}
