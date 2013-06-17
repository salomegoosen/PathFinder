/*
 * // TODO
 * possible optimizations:
 * - calculate f as soon as g or h are set, so it will not have to be
 *      calculated each time it is retrieved
 * - store nodes in openList sorted by their f value.
 */

package pathfinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;


/**
 * This class represents a simple terrain map.
 * <p>
 * The map represent a terrainMap with nodes, weighted or not and walkable or not 
 *  it calculates the shortest path between two nodes avoiding non-walkable nodes.
 * <p>
 * <p>
 * Usage of this package:
 * Create a node class which extends AbstractNode and implements the setFuturePathCosts
 * method.
 * Create a NodeFactory that implements the NodeFactory interface.
 * Create Map instance with those created classes.
 * @see ExampleUsage ExampleUsage
 * <p>
 *
 * @see AbstractNode
 * @see NodeFactory
 * @version 1.0
 * @param 
 */
public class MyMap {

    /** keeps the nodes in a 2D ArrayList */
    private ArrayList nodes;
	private PropertyHelper propertyHelper;
	protected NodeFactory nodeFactory;
	private AbstractNode startNode;
	private AbstractNode goalNode;
	private AbstractAlgorithm algorithm;
	
    /**
     * constructs a terrain map
     * <p>
     * The nodes will be instantiated through the given nodeFactory
     *
     * @param nodeFactory 
     * @throws Exception 
     */
    public MyMap(NodeFactory nodeFactory, AbstractAlgorithm algorithm) throws Exception {
        this.nodeFactory = nodeFactory;        
        this.algorithm = algorithm;
        nodes = new ArrayList(); 
        propertyHelper = PropertyHelper.getInstance();
        startNode = null;
        goalNode = null;
    }


    /**
     * sets nodes walkable field at given coordinates to given value.
     * <p>
     * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
     *
     * @param x
     * @param y
     * @param bool
     */
    public void setWalkable(int x, int y, boolean bool) {
        // TODO check parameter.
        getNode(x, y).setWalkable(bool);
    }
    
    public ArrayList getNodes() {
		return nodes;
	}

    /**
     * returns node at given coordinates.
     * <p>
     * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
     *
     * @param x
     * @param y
     * @return node
     */
    public final AbstractNode getNode(int x, int y) {
        return nodeAt(x,y);
    }

    private AbstractNode nodeAt(int x, int y) {
		ArrayList nodeRowI = null;
		AbstractNode nodeAtxy = null;
		nodeRowI = getRowAt(x);
		if (y < nodeRowI.size())
			nodeAtxy = (AbstractNode) nodeRowI.get(y);
		return nodeAtxy;
	}


	/*
	 * This method will insert or update a node in the map 
	 * on loading of the map
	 * It will also load the unique startNode and
	 * 	goalNode by checking the terrainSymbol
	 *  against the configure startNodeSymbol
	 *  and goalNodeSymbol
	 * ONLY one startNodeSymbol must exist on the terrainMap
	 * ONLY one goalNodeSymbol must exist on the terrainMap
	 */
	public void setNode(AbstractNode nodeToAdd) throws Exception {
		int rowNum = nodeToAdd.getxPosition();
		int columnNum = nodeToAdd.getyPosition();
		char startNodeSymbol = PropertyHelper.getStartNodeSymbol();
		char goalNodeSymbol = PropertyHelper.getGoalNodeSymbol();
		
		// check for startNode uniqueness
		if (nodeToAdd.getTerrainSymbol() == startNodeSymbol)
			if (startNode == null)
				startNode = nodeToAdd;
			else if (!sameNodes(nodeToAdd, startNode)) 
				throw new Exception("ONLY one node with STARTING symbol ["+startNodeSymbol+"] is valid");
		
		// check if unique goalNode	
		if (nodeToAdd.getTerrainSymbol() == goalNodeSymbol)
			if (goalNode == null)
				goalNode = nodeToAdd;
			else if (!sameNodes(nodeToAdd, goalNode)) {
				throw new Exception("ONLY one node with GOAL symbol ["+goalNodeSymbol+"] is valid");
		} 
			
		ArrayList nodeListForRowX = this.getRowAt(rowNum);
		nodeListForRowX.add(columnNum, nodeToAdd);		
	}

	private boolean sameNodes(AbstractNode nodeToAdd,
			AbstractNode startOrGoalNode) {
		return (startOrGoalNode!= null &&
				(nodeToAdd.getxPosition() == startOrGoalNode.getxPosition() &&
				 nodeToAdd.getyPosition() == startOrGoalNode.getyPosition()));
			 
	}


	private ArrayList getRowAt(int x) {
		ArrayList row;
		if ((nodes.size() > x) && nodes.get(x)!=null) {
			row = (ArrayList) nodes.get(x);
		} else {
			row = new ArrayList();
			nodes.add(row);
		} 
		return row;
	}


	/**
     * prints map
     * <p>
     * shows solution path as "#".
     * 
     */
	// TODO Override to print solution to file
    public void drawMap(String mapName) {
    	String outputFileName = mapName+"solution.txt";
		//FileOutputStream fileWriter = new FileOutputStream(file );
		System.out.println("To print to output file "+outputFileName);
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                AbstractNode node = getNode(i,j);
                if (node == null)
                	print(' ');
                else
                	print(node.getTerrainSymbol());
            }
            print('\n');
        }
    }

    /**
     * prints something to sto.
     */
    private void print(char c) {
        System.out.print(c);
    }


    /* Variables and methodes for path finding */


    // variables needed for path finding

    /** list of type AbstractNode objects
     * containing nodes not yet visited but adjacent to visited nodes. */
    private List openList; // list of type Node objects
    /** list containing nodes already visited/taken care of. */
    private List closedList;  // list of type Node objects
    private List solution;

    /**
     * finds an allowed path from start to goal coordinates on this map.
     * <p>
     * This method uses the A* algorithm. The hCosts value is calculated in
     * the given Node implementation.
     * <p>
     * This method will return a LinkedList containing the start node at the
     * beginning followed by the calculated shortest allowed path ending
     * with the end node.
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
    public final List findPath(int oldX, int oldY, int newX, int newY) throws Exception {
        if (!validXY(oldX, oldY))
    		throw new Exception("Invalid FROM path coordinates");
    	if (!validXY(newX, newY))
    		throw new Exception("Invalid TO path coordinates");
    	AbstractNode goalNode = getNode(newX,newY); // goal node
        openList = new LinkedList();
        closedList = new LinkedList();
        openList.add(getNode(oldX,oldY)); // add starting node to open list

        AbstractNode current;
        while (!openList.isEmpty()) {
            current = lowestCostNodeInOpenList(); // get node with lowest costs from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list
            if ((current.getxPosition() == newX)
                    && (current.getyPosition() == newY)) { // found goal
                solution = calcPath(getNode(oldX,oldY), current);
                return solution;
            }
            // for all adjacent nodes:
            List adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                AbstractNode currentAdj = (AbstractNode) adjacentNodes.get(i);
                int weightForNode = propertyHelper.weightForSymbol(currentAdj.getTerrainSymbol());
                if (weightForNode < 0) 
                	currentAdj.setWalkable(false);               
                if (currentAdj.isWalkable()) {
                	if (!openList.contains(currentAdj)) { // node is not in openList
                		currentAdj.setPrevious(current); // set current node as previous for this node
                		currentAdj.setFuturePathCosts(goalNode, 0 ); // set estimated costs to goal for this node
                		currentAdj.setPastPathCosts(current, weightForNode); // set costs from start to this node
                			openList.add(currentAdj); // add node to openList
                	} else { // node is in openList
                		if (currentAdj.getPastPathCosts() > currentAdj.calculatePastPathCosts(current)) { // costs from current node are cheaper than previous costs
                			currentAdj.setPrevious(current); // set current node as previous for this node
                			currentAdj.setPastPathCosts(current, weightForNode); // set costs from start to this node
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

    private boolean validXY(int x, int y) {
		return (x >= 0 && y < nodes.size());
	}


	/**
     * calculates the found path between two points according to
     * their given <code>previousNode</code> field.
     *
     * @param start
     * @param goal
     * @return
     */
    private List calcPath(AbstractNode start, AbstractNode goal) {
     // TODO if invalid nodes are given (eg cannot find from
     // goal to start, this method will result in an infinite loop!)
        LinkedList path = new LinkedList();

        AbstractNode curr = goal;
        boolean done = false;
        while (!done) {
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
            if (((AbstractNode) openList.get(i)).getPastPlusFutureCosts() < cheapest.getPastPlusFutureCosts()) {
                cheapest = (AbstractNode) openList.get(i);
            }
        }
        return cheapest;
    }

    /**
     * returns a LinkedList with nodes adjacent to the given node.
     * if those exist, are walkable and are not already in the closedList!
     */
    private List getAdjacent(AbstractNode node) {
        // TODO make loop
        int x = node.getxPosition();
        int y = node.getyPosition();
        List adj = new LinkedList();

        AbstractNode temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
               // temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (x+1 < nodes.size()) {
            temp = this.getNode((x + 1), y);
            System.out.println(temp.toString());
            if (temp.isWalkable() && !closedList.contains(temp)) {
               // temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                //temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y+1 < nodes.size()) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                //temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }


        // add nodes that are diagonaly adjacent too:
        if (x+1 < nodes.size() && y+1 < nodes.size()) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
               // temp.setIsDiagonaly(true);
                adj.add(temp);
            }
        }

            if (x > 0 && y > 0) {
                temp = this.getNode((x - 1), (y - 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                   // temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }

            if (x > 0 && y+1 < nodes.size()) {
                temp = this.getNode((x - 1), (y + 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                   // temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }

            if (x+1 < nodes.size() && y > 0) {
                temp = this.getNode((x + 1), (y - 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                    //temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }
        
        return adj;
    }
    
   /* public static void printList(List listToPrint) {
		for (int i = 0; i < listToPrint.size(); i++) 
			System.out.println(((MyNode)listToPrint.get(i)).toString());
		System.out.println();
	}*/

	public List findPathToGoal() throws Exception {
		int oldX = startNode.getxPosition();
		int oldY = startNode.getyPosition();
		int newX = goalNode.getxPosition();
		int newY = goalNode.getyPosition();
		solution = findPath(oldX, oldY, newX, newY);
		return solution;
	}


	public void plotSolutionOnMap() {
		System.out.println("SOLUTION: "+solution.size());
		for (int i = 0; i < solution.size(); i++) {
			MyNode node = (MyNode) (solution.get(i));
			node.setTerrainSymbol(PropertyHelper.PATH_SYMBOL);
			System.out.print("(" + (node.getxPosition()) + ", "
					+ node.getyPosition() + ") ");
		}
	}
}
