package pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class MyAStarPathFinderAlgorithm extends AbstractAlgorithm {

	/*
	 * // TODO
	 * possible optimizations:
	 * - calculate totalCost as soon as past or future costs are set, so it will not have to be
	 *      calculated each time it is retrieved
	 * - store nodes in openList sorted by their totalCost value.
	 */
	
	
	// Variables and methods for path finding */
	/**
	 * list of type AbstractNode objects containing nodes not yet visited,
	 * walkable and adjacent to visited node
	 */
	private List openList; // list of type Node objects
	/** list containing nodes already visited/taken care of. */
	private List closedList; // list of type Node objects
	/** the map of the terrain */
	private MyMap terrainMap;
	
	public static Logger LOGGER = Logger.getLogger(MyAStarPathFinderAlgorithm.class);

	public MyAStarPathFinderAlgorithm(MyMap terrainMap) {
		this.terrainMap = terrainMap;
		openList = new LinkedList();
		closedList = new LinkedList();
	}

	/**
	 * finds an allowed path from start to goal coordinates on this map.
	 * <p>
	 * This method uses the A* algorithm. The hCosts value is calculated in the
	 * given Node implementation.
	 * <p>
	 * This method will return a LinkedList containing the start node at the
	 * beginning followed by the calculated shortest allowed path ending with
	 * the end node.
	 * <p>
	 * If no allowed path exists, an empty list will be returned.
	 * <p>
	 * <p>
	 * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 * @return list of type node
	 * @throws Exception
	 */
	public final List findPath(int oldX, int oldY, int newX, int newY)
			throws Exception {
		List solution = null;
		closedList = new ArrayList();
		openList = new ArrayList();
		if (!terrainMap.validXY(oldX, oldY))
			throw new Exception("Invalid FROM path coordinates");
		if (!terrainMap.validXY(newX, newY))
			throw new Exception("Invalid TO path coordinates");
		AbstractNode goalNode = getNodeAt(newX, newY); // goal node
		openList.add(getNodeAt(oldX, oldY)); // add starting node to open list

		AbstractNode current;
		while (!openList.isEmpty()) {
			current = lowestCostNodeInOpenList(); // get node with lowest costs
													// from openList
			closedList.add(current); // add current node to closed list
			openList.remove(current); // delete current node from open list
			if ((current.getxPosition() == newX)
					&& (current.getyPosition() == newY)) { // found goal
				solution = calcPath(getNodeAt(oldX, oldY), current);
				return solution;
			}
			// for all adjacent nodes:
			List adjacentNodes = findAdjacentNodesTo(current);
			for (int i = 0; i < adjacentNodes.size(); i++) {
				AbstractNode currentAdj = (AbstractNode) adjacentNodes.get(i);
				if (currentAdj.isWalkable()) {
					if (!openList.contains(currentAdj)) { // node is not in
															// openList
						currentAdj.setPrevious(current); // set current node as
															// previous for this
															// node
						currentAdj.setFuturePathCosts(goalNode); // set
																	// estimated
																	// costs to
																	// goal for
																	// this node
						currentAdj.setPastPathCosts(current); // set costs from
																// start to this
																// node
						openList.add(currentAdj); // add node to openList
					} else { // node is in openList
						if (currentAdj.getPastPathCosts() > currentAdj
								.calculatePastPathCosts(current)) { // costs
																	// from
																	// current
																	// node are
																	// cheaper
																	// than
																	// previous
																	// costs
							currentAdj.setPrevious(current); // set current node
																// as previous
																// for this node
							currentAdj.setPastPathCosts(current); // set costs
																	// from
																	// start to
																	// this node
						}
					}
				}
			}

			if (openList.isEmpty()) { // no path exists
				return new LinkedList(); // return empty list
			}
		}
		return null; // unreachable
	}

	private AbstractNode getNodeAt(int x, int y) {
		return terrainMap.getNode(x, y);
	}

	public List findAdjacentNodesTo(AbstractNode currentNode)
			throws UndefinedPropertyException {
		int currX = currentNode.getxPosition(); // 0,1 adjacent is 1,0; 0,2 and
												// 1,1
		int currY = currentNode.getyPosition(); // 1,0 adjacent is x-1,x,x+1 for
												// y-1 x-1,y x+1,y x-1,x,x+1 for
												// y+1
		List adjacent = new ArrayList();
		LOGGER.info("Looking for adjacent nodes to (" + currX + ","
				+ currY + "): ");

		

		// add bottom, if exists
		LOGGER.info("Bottom: (" + (currX + 1) + "," + currY + ") ");
		int bottomX = currX + 1;
		adjacent.addAll(findAdjacentForRowFromYPos(bottomX, currY));

		// add right
		LOGGER.info("Right: (" + currX + "," + (currY + 1) + ")");
		AbstractNode temp = this.getNodeAt(currX, currY + 1);
		if (temp != null && temp.isWalkable() && !closedList.contains(temp)) {
			adjacent.add(temp);
		}
		
		// add top if exists
		LOGGER.info("Top: (" + (currX - 1) + "," + currY + ") ");
		int topX = currX - 1;
		if (topX > 0)
			adjacent.addAll(findAdjacentForRowFromYPos(currX - 1, currY));

		// add left if exists
		LOGGER.info("Left: (" + currX + "," + (currY - 1) + ") ");
		if (currY - 1 > 0) {
			temp = getNodeAt(currX, currY - 1);
			if (temp != null && temp.isWalkable() && !closedList.contains(temp))
				adjacent.add(temp);
		}
		LOGGER.info("Found adjacent: "+adjacent.toString());
		return adjacent;
	}

	private List findAdjacentForRowFromYPos(int currX, int currY)
			throws UndefinedPropertyException {
		List adjacent = new ArrayList();
		for (int y = currY - 1; y <= currY + 1; y++) {
			if (y > 0) {
				LOGGER.info("* (" + currX + "," + y + ") ");
				AbstractNode temp = getNodeAt(currX, y);
				if (temp != null && temp.isWalkable()
						&& !closedList.contains(temp)) {
					adjacent.add(temp);
				}
			}
		}
		return adjacent;
	}

	/**
	 * calculates the found path between two points according to their given
	 * <code>previousNode</code> field.
	 * 
	 * @param start
	 * @param goal
	 * @return
	 */
	private List calcPath(AbstractNode start, AbstractNode goal) {
		LinkedList path = new LinkedList();

		AbstractNode curr = goal;
		boolean done = false;
		while (!done) { // TODO change to curr.getPrevious() != null
			path.addFirst(curr);
			curr = curr.getPrevious();

			if (curr.equals(start)) {
				path.addFirst(start);
				done = true;
			}
		}
		return path;
	}

	/**
	 * returns the node with the lowest costs.
	 * 
	 * @return
	 */
	private AbstractNode lowestCostNodeInOpenList() {
		// TODO currently, this is done by going through the whole openList!
		AbstractNode cheapest = (AbstractNode) openList.get(0);
		for (int i = 0; i < openList.size(); i++) {
			if (((AbstractNode) openList.get(i)).getPastPlusFutureCosts() < cheapest
					.getPastPlusFutureCosts()) {
				cheapest = (AbstractNode) openList.get(i);
			}
		}
		return cheapest;
	}

	/*
	 * calls the injected algortihmm class to find the path from the start to
	 * the goal node
	 */
	public List findPathToGoal() throws Exception {
		int oldX = terrainMap.getStartNode().getxPosition();
		int oldY = terrainMap.getStartNode().getyPosition();
		int newX = terrainMap.getGoalNode().getxPosition();
		int newY = terrainMap.getGoalNode().getyPosition();
		List solution = findPath(oldX, oldY, newX, newY);
		return solution;
	}
}
