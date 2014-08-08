package j3dio.ply.element;

import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glNormal3f;
import j3dio.GLRenderable;
import j3dio.Point3f;
import j3dio.ply.ElementInstance;

import java.security.InvalidParameterException;

public class PlyVertex implements GLRenderable {
	public Point3f position = null;
	public Point3f normal = null;
	
	public PlyVertex(ElementInstance vertexInstance) {
		if (!vertexInstance.tyepname.equals("vertex")) {
			throw new InvalidParameterException("Element instance is not vertex");
		}
		
		position.x = (Float) vertexInstance.get("x");
		position.y = (Float) vertexInstance.get("y");
		position.z = (Float) vertexInstance.get("z");
		if (vertexInstance.getProperties().containsKey("nx") ||
			vertexInstance.getProperties().containsKey("ny") ||
			vertexInstance.getProperties().containsKey("nz")) {

			normal.x = (Float) vertexInstance.get("nx");
			normal.y = (Float) vertexInstance.get("ny");
			normal.z = (Float) vertexInstance.get("nz");
		}
	}
	

	/**
	 * <b>DEPRECATED</b> use <code>glrender()</code> instead
	 * <br />
	 * Draw this object using GL11
	**/
	@Deprecated
	@Override
	public void render() {
		glrender();
	}
	/**Draw this object using GL11**/
	@Override
	public void glrender() {
		if (normal != null) {
			glNormal3f(normal.x, normal.y, normal.z);
		}
		glVertex3f(position.x, position.y, position.z);
	}
}