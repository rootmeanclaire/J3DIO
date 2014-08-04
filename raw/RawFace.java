package j3dio.raw;

import java.util.List;

public class RawFace {
	public int[] vertIndxs;
	public int size;
	
	public RawFace(int[] vertexIndices) {
		size = vertexIndices.length;
		vertIndxs = vertexIndices;
	}
	public RawFace(Integer[] vertexIndices) {
		size = vertexIndices.length;
		vertIndxs = new int[size];
		
		for (int i = 0; i < size; i++) {
			vertIndxs[i] = vertexIndices[i];
		}
	}
	public RawFace(List<Integer> vertexIndices) {
		size = vertexIndices.size();
		vertIndxs = new int[size];
		
		for (int i = 0; i < size; i++) {
			vertIndxs[i] = vertexIndices.get(i);
		}
	}
}