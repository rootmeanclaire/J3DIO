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
			txtrIndxs[i] = Integer.parseInt(params[i].split("/")[1]);
			normIndxs[i] = Integer.parseInt(params[i].split("/")[2]);
		}
	}
}
