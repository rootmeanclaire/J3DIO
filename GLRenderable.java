package j3dio;

public interface GLRenderable {
	/**
	 * <b>DEPRECATED</b> use <code>glrender()</code> instead
	 * <br />
	 * Draw this object using GL11
	**/
	@Deprecated
	public void render();
	/**Draw this object using GL11**/
	public void glrender();
}