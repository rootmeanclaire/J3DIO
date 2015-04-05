package j3dio.dae;

/**
 * @author Evan Shimoniak
 * @since 5.0 beta
**/
public class DaeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DaeException() {
		super("Syntax error in .dae file");
	}
	public DaeException(String message) {
		super(message);
	}
}