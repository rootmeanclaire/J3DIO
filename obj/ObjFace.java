package j3dio.obj;

import j3dio.Definable;
import j3dio.raw.RawFace;

public class ObjFace extends RawFace implements Definable {
	public int[] txtrIndxs;
	public int[] normIndxs;
	
	public ObjFace(String[] params) {
		super(new int[params.length]);
		txtrIndxs = new int[size];
		normIndxs = new int[size];
		
		for (int i = 0; i < params.length; i++) {
			vertIndxs[i] = Integer.parseInt(params[i].split("/")[0]);
			if (params[i].split("/").length > 1) {
				txtrIndxs[i] = Integer.parseInt(params[i].split("/")[1]);
			}
			if (params[i].split("/").length > 2) {
				normIndxs[i] = Integer.parseInt(params[i].split("/")[2]);
			}
		}
	}
	
	@Override
	public String toString() {
		return Integer.toHexString(hashCode()) + "@\"" + toDefinition() + '"';
	}
	
	@Override
	public String toDefinition() {
		if (hasTextures() && hasNorms()) {
			StringBuilder sb = new StringBuilder("f");
			for (int i = 0; i < size; i++) {
				sb.append(" " + vertIndxs[i] + "/" + txtrIndxs + "/" + normIndxs);
			}
			return (sb.toString());
		} else if (hasTextures()) {
			StringBuilder sb = new StringBuilder("f");
			for (int i = 0; i < size; i++) {
				sb.append(" " + vertIndxs[i] + "/" + txtrIndxs);
			}
			return (sb.toString());
		} else if (hasNorms()) {
			StringBuilder sb = new StringBuilder("f");
			for (int i = 0; i < size; i++) {
				sb.append(" " + vertIndxs[i] + "//" + normIndxs);
			}
			return (sb.toString());
		} else {
			StringBuilder sb = new StringBuilder("f");
			for (int i = 0; i < size; i++) {
				sb.append(" " + vertIndxs[i]);
			}
			return (sb.toString());
		}
	}
	
	public boolean hasTextures() {
		for (int i : txtrIndxs) {
			if (i != 0) {
				return true;
			}
		}
		
		return false;
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