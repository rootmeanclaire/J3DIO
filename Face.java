package obj;

public class Face {
	public int[] vertIndxs;
	public int[] txtrIndxs;
	public int[] normIndxs;
	
	public Face(String[] params) {
		vertIndxs = new int[params.length];
		txtrIndxs = new int[params.length];
		normIndxs = new int[params.length];
		
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
}
