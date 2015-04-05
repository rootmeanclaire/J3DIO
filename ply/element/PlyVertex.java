package j3dio.ply.element;

import j3dio.Point3f;
import j3dio.ply.ElementInstance;

import java.security.InvalidParameterException;

import org.lwjgl.opengl.GL11;

import com.jogamp.opengl.GL2;

/**
 * @author Evan Shimoniak
 * @since 4.2
**/
public class PlyVertex implements j3dio.LWJGLRenderable, j3dio.JOGLRenderable {
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
	
	@Override
	public void joglrender(GL2 gl) {
		if (normal != null) {
			gl.glNormal3f(normal.x, normal.y, normal.z);
		}
		gl.glVertex3f(position.x, position.y, position.z);
	}
	
	/**Draw this object using GL11**/
	@Override
	public void lwjglrender() {
		if (normal != null) {
			GL11.glNormal3f(normal.x, normal.y, normal.z);
		}
		GL11.glVertex3f(position.x, position.y, position.z);
	}
}