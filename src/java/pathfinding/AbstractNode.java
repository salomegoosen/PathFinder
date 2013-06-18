package pathfinding;

/**
 * This class represents an AbstractNode. It has all the appropriate fields as well
 * as getter and setter to be used by the A* algorithm.
 * <p>
 * <p>
 * An <code>AbstractNode</code> has x- and y-coordinates and can be walkable or not.
 * A previous AbstractNode may be set, as well as the
 * <code>fCosts</code>, <code>pastPathCosts</code> and <code>hCosts</code>.
 * <p>
 * <p>
 * <code>totalCosts</code>: <code>pastPathCosts</code> + <code>hCosts</code>
 * <p>
 * <code>pastPathCosts</code>: calculated costs from start AbstractNode to this AbstractNode
 * <p>
 * <code>futurePathCosts</code>: estimated costs to get from this AbstractNode to end AbstractNode
 * <p>
 * <p>
 * A subclass has to override the heuristic function to estimate the future path
 * <p>
 * <code>setFuturePathCosts(AbstractNode endAbstractNode)</code>
 * <p>
 * @see MyNode#sethCosts(AbstractNode endNode) example Implementation using manhatten method
 * <p>
 *
 * @version 1.0
 */
public abstract class AbstractNode {

    /** costs to move sideways from one square to another. */
    protected static final int BASICMOVEMENTCOST = 10;
    /** costs to move diagonally from one square to another. */
    protected static final int DIAGONALMOVEMENTCOST = 14;

    private int xPosition;
    private int yPosition;
    private boolean walkable;
    private char terrainSymbol;

    // for pathfinding:

    /** the previous AbstractNode of this one on the currently calculated path. */
    private AbstractNode previous;

    /** calculated costs from start AbstractNode to this AbstractNode. */
    protected int pastPathCosts;

    /** estimated costs to get from this AbstractNode to end AbstractNode. */
    private int futurePathCosts;
	private int weight;

    /**
     * constructs a walkable AbstractNode with given coordinates.
     *
     * @param xPosition
     * @param yPosition
     */
    public AbstractNode(int xPosition, int yPosition, char terrainSymbol) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.walkable = true;
        this.terrainSymbol = terrainSymbol;
    }

    public char getTerrainSymbol() {
		return terrainSymbol;
	}

	public void setTerrainSymbol(char terrainSymbol) {
		this.terrainSymbol = terrainSymbol;
	}
 
    /**
     * sets x and y coordinates.
     *
     * @param x
     * @param y
     */
    public void setCoordinates(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * @return the xPosition
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * @return the yPosition
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * @return the walkable
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * @param walkable the walkable to set
     */
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    /**
     * returns the node set as previous node on the current path.
     *
     * @return the previous
     */
    public AbstractNode getPrevious() {
        return previous;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(AbstractNode previous) {
        this.previous = previous;
    }

    /**
     * returns <code>pastPathCosts</code> + <code>futurePathCosts</code>.
     * <p>
     *
     *
     * @return the PastPlusFutureCosts
     */
    public int getPastPlusFutureCosts() {
        return pastPathCosts + futurePathCosts;
    }

    /**
     * returns the calculated costs from start AbstractNode to this AbstractNode.
     *
     * @return the pastPathCosts
     */
    public int getPastPathCosts() {
        return pastPathCosts;
    }

    /**
     * sets pastPathCosts to <code>pastPathCosts</code> plus <code>movementPanelty</code>
     * for this AbstractNode.
     *
     * @param pastPathCosts the pastPathCosts to set
     */
    private void setPastPathCosts(int pastPathCosts) {
        this.pastPathCosts = pastPathCosts;
    }

    /**
     * sets pastPathCosts to <code>pastPathCosts</code> plus <code>movementPanelty</code>
     * for this AbstractNode given the previous AbstractNode as well as the basic cost
     * from it to this AbstractNode.
     *
     * @param previousAbstractNode
     */
    public void setPastPathCosts(AbstractNode previousAbstractNode) {
        setPastPathCosts(previousAbstractNode.getPastPathCosts() + previousAbstractNode.getWeight());
    }

    /*
     * returns the weight as calculated on loading from terrainSymbol
     */
    private int getWeight() {
		return weight;
	}

	/**
     * calculates - but does not set - path past costs.
     * <p>
     * 
     * finds the adjacent AbstractNodes.
     *
     * @param previousAbstractNode
     * @return pastPathCosts
     */
    public int calculatePastPathCosts(AbstractNode previousAbstractNode) {
          return previousAbstractNode.getPastPathCosts();
    }

    /**
     * calculates - but does not set - g costs, adding a movementPanelty.
     *
     * @param previousAbstractNode
     * @param movementCost costs from previous AbstractNode to this AbstractNode.
     * @return pastPathCosts
     */
    public int calculatePastPathCosts(AbstractNode previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getPastPathCosts() + movementCost);
    }

    /**
     * returns estimated costs to get from this AbstractNode to end AbstractNode.
     *
     * @return the hCosts
     */
    public int getFuturePathCosts() {
        return futurePathCosts;
    }

    /**
     * sets hCosts.
     *
     * @param hCosts the hCosts to set
     */
    protected void setFuturePathCosts(int futurePathCosts) {
        this.futurePathCosts = futurePathCosts;
    }

    /**
     * calculates hCosts for this AbstractNode to a given end AbstractNode.
     * Uses Manhatten method.
     *
     * @param endAbstractNode
     */
    public abstract void setFuturePathCosts(AbstractNode endAbstractNode);

    /**
     * returns a String containing the coordinates, as well as past, future 
     * and total costs.
     *
     * @return
     */
    public String toString() {
        return getTerrainSymbol()+" (" + getxPosition() + ", " + getyPosition() + "): " + getPastPlusFutureCosts();
        		//"futurePathCost: "
               // + getFuturePathCosts() + " pastPathCost: " + getPastPathCosts();
    }

    /**
     * returns weather the coordinates of AbstractNodes are equal.
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractNode other = (AbstractNode) obj;
        if (this.xPosition != other.xPosition) {
            return false;
        }
        if (this.yPosition != other.yPosition) {
            return false;
        }
        return true;
    }

    /**
     * returns hash code calculated with coordinates.
     *
     * @return
     */
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.xPosition;
        hash = 17 * hash + this.yPosition;
        return hash;
    }

	public static boolean isAtSameCoordinates(AbstractNode startNode,
			AbstractNode nodeToAdd) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setWeightForSymbol(int weightForSymbol) {
		this.weight = weightForSymbol;
		setWalkable(weight >= 0);
	}

}
