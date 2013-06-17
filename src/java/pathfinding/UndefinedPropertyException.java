package pathfinding;

public class UndefinedPropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndefinedPropertyException(String propertyName) {
		super("Property["+propertyName+"] must be defined");
	}

	
}
