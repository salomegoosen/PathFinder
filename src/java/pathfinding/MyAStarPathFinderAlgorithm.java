package pathfinding;

import java.util.LinkedList;
import java.util.List;

public class MyAStarPathFinderAlgorithm extends AbstractAlgorithm {

	public MyAStarPathFinderAlgorithm() {
		// TODO Auto-generated constructor stub
	}

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

	   return null;
   }
	   /*
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
   } */
}
