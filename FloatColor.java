package j3dio;

import java.awt.Color;

/**
 * @author Evan Shimoniak
 * @since 4.1.1 beta
**/
public class FloatColor {
	private float r, g, b;
	
	public FloatColor(float red, float green, float blue) {
		r = red;
		g = green;
		b = blue;
	}
	public FloatColor(float[] rgb) {
		r = rgb[0];
		g = rgb[1];
		b = rgb[2];
	}
	
	public float getRed() {
		return r;
	}
	public float getGreen() {
		return g;
	}
	public float getBlue() {
		return b;
	}
	
	/**@return A representation of this as a standard {@link java.awt.Color}**/
	public Color toColor() {
		return new Color(r, g, b);
	}
}