package pathfinding;

/**
 * A simple Factory for nodes.
 */
public class MyNodeFactory implements NodeFactory {

        public AbstractNode createNode(int x, int y, char terrainSymbol) {
            return new MyNode(x, y, terrainSymbol);
        }

}
