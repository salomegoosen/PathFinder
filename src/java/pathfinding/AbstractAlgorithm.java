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
	 * @returns solutionPath
	 * @throws Exception
	 */
	public abstract List findPath(int oldX, int oldY, int newX, int newY) throws Exception;

	
	/**
	 * @return solutionPath
	 * @throws Exception
	 * 
	 * Uses the startNode and goalNode from the loaded terrainMap
	 */
	public abstract List findPathToGoal() throws Exception; 
	
}
