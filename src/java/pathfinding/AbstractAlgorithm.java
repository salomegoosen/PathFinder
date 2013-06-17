package pathfinding;

import java.util.List;

/**
 * @author Salome_2
 *
 */
public abstract class AbstractAlgorithm {
	
	/**
	 * 
	 * IMPLEMENT in implementation depending on Algorithm used
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 * @return
	 * @throws Exception
	 */
	public abstract List findPath(int oldX, int oldY, int newX, int newY) throws Exception; 
	
}
