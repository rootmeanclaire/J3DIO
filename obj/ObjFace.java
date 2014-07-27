package jml.obj;

import jml.raw.RawFace;

public class ObjFace extends RawFace {
	public int[] txtrIndxs;
	public int[] normIndxs;
	public int size;
	
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