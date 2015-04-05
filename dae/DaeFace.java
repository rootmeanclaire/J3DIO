package j3dio.dae;

import j3dio.raw.RawFace;

/**
 * @author Evan Shimoniak
 * @since 5.0 beta
**/
public class DaeFace extends RawFace {
	public int[] normIndxs;
	
	public DaeFace(int[] vertexIndices) {
		super(vertexIndices);
		normIndxs = new int[size];
	}
	public DaeFace(int[] vertexIndices, int[] normalIndices) {
		super(vertexIndices);
		normIndxs = normalIndices;
	}

	public boolean hasNorms() {
		for (int i : normIndxs) {
			if (i != 0) {
				return true;
			}
		}
		
		return false;
	}
}