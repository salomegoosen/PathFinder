package pathfinding;


/**
 * A Factory which creates new instances of an implementation of 
 * <code>AbstractNode</code> at given coordinates.
 * <p>
 * Implementation must be injected to <code>Map</code> instance on
 * construction.
 *
 * @see AbstractNode
 * @version 1.0
 */
public interface NodeFactory {

    /**
     * creates new instances of an implementation of the
     * <code>AbstractNode</code>.
     * In an implementation, it should return a new node with its position
     * set to the given x and y values.
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param terrainWeightSymbol 
     * @return
     */
    public AbstractNode createNode(int x, int y, char terrainWeightSymbol);

}
