package j3dio.ply.element;

import j3dio.ply.ElementInstance;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class PlyFace {
	List<Integer> vertexIndices = new ArrayList<Integer>();
	
	@SuppressWarnings("unchecked")
	public PlyFace(ElementInstance faceInstance) {
		if (!faceInstance.tyepname.equals("face")) {
			throw new InvalidParameterException("Element instance is not vertex");
		}
		
		vertexIndices = (List<Integer>) faceInstance.get("vertex_indices");
	}
}