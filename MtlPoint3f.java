package obj;

public class MtlPoint3f extends Point3f {
	private MtlMaterial mtl = null;
	
	public MtlPoint3f(float x, float y, float z) {
		super(x, y, z);
	}
	public MtlPoint3f(float x, float y, float z, MtlMaterial material) {
		super(x, y, z);
		this.mtl = material;
	}
	
	public MtlMaterial getMaterial() {
		return mtl;
	}
	
	public void setMaterial(MtlMaterial newMtl) {
		this.mtl = newMtl;
	}
}
