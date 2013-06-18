package pathfinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
	
	public static Logger LOGGER = Logger.getLogger(MyMap.class);
	
    /**
     * constructs a terrain map
     * <p>
     * The nodes will be instantiated through the given nodeFactory
     *
     * @param nodeFactory 
     * @throws Exception 
     */
    public MyMap(NodeFactory nodeFactory) throws Exception {
        this.nodeFactory = nodeFactory;        
        nodes = new ArrayList(); 
        propertyHelper = PropertyHelper.getInstance();
        startNode = null;
        goalNode = null;
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
		if (validXY(x,y))
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
		char startNodeSymbol = propertyHelper.getStartNodeSymbol();
		char goalNodeSymbol = propertyHelper.getGoalNodeSymbol();
		
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
     * prints solution to file <mapName>+solution.txt
     * <p>
     * @param mapName
     * @return 2D solution char array
     * @throws Exception
     */
    public char[][] writeSolutionMapToFile(String mapName) throws Exception  {
    	String outputFileName = mapName.replaceAll(".txt", "_")+"solution.txt";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFileName)));
    	char[][] solution = new char[getNumRows()][getNumColumns()];
		try {
    	LOGGER.info("To print to output file "+outputFileName);
    	System.out.println("SOLUTION MAP:");
        for (int i = 0; i < getNumRows(); i++) {
        	String lineToLog = new String();
            for (int j = 0; j < getNumColumns(); j++) {
                AbstractNode node = getNode(i,j);
                if (node == null)  // don't break if inconsistency in map data
                	solution[i][j] =' ';
                else
                	solution[i][j] = node.getTerrainSymbol();
                writer.write(solution[i][j]);
                lineToLog += solution[i][j];
            }
            System.out.println(lineToLog);
            LOGGER.info(lineToLog);
            writer.newLine();
        }
        System.out.println("Solution file created: "+outputFileName);
    }  catch (Exception ex) {
    	throw ex;
    } finally {
    	try {
    		writer.close();
    	} catch (Exception ex) {
    	}
    }
        return solution;
    }

    public int getNumRows() {
		return nodes.size();
	}
    
    public int getNumColumns() {
		return ((ArrayList)nodes.get(0)).size();
	}

	public void plotSolutionOnMap(List solution) {
		LOGGER.info("SOLUTION: "+solution.size());
		for (int i = 0; i < solution.size(); i++) {
			MyNode node = (MyNode) (solution.get(i));
			MyNode nodeOnMap = (MyNode) getNode(node.getxPosition(), node.getyPosition());
			nodeOnMap.setTerrainSymbol(PropertyHelper.PATH_SYMBOL);
			LOGGER.info("(" + (node.getxPosition()) + ", "
					+ node.getyPosition() + ") ");
		}
	}

	public int weightForSymbol(char terrainSymbol) throws UndefinedPropertyException {
		return propertyHelper.weightForSymbol(terrainSymbol);
	}

	public boolean validXY(int x, int y) {
		ArrayList rowX= (ArrayList) nodes.get(x);
		return ((x >= 0 && x < getNumRows()) && 
				(y >= 0 && y < rowX.size()));
	}

	public AbstractNode getStartNode() {
		return startNode;
	}


	public AbstractNode getGoalNode() {
		return goalNode;
	}

    public void drawMapToStdOut() {
    	System.out.println("*****MAP:");
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
     * prints to stdout.
     */
    private void print(char c) {
        System.out.print(c);
    }
    
    public void finalize() {
    	nodes = null;
    	propertyHelper = null;
    }

}
