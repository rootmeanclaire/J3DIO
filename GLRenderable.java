package j3dio;

/**
 * <b>DEPRECATED:</b> use {@link LWJGLRenderable} or {@link JOGLRenderable} instead
 * Used for rendering with LWJGL's GL11
 * @author Evan Shimoniak
 * @since 2.1 beta
**/
@Deprecated
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