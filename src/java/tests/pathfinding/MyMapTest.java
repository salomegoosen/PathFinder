package tests.pathfinding;

import java.util.List;

import pathfinding.AbstractAlgorithm;
import pathfinding.AbstractNode;
import pathfinding.MyAStarPathFinderAlgorithm;
import pathfinding.MyMap;
import pathfinding.MyNode;
import pathfinding.MyNodeFactory;
import pathfinding.NodeFactory;
import pathfinding.UndefinedPropertyException;
import junit.framework.TestCase;

public class MyMapTest extends TestCase {

	public void testMyMap() {
		NodeFactory nodeFactory = new MyNodeFactory();
		try {
			MyMap map = new MyMap(nodeFactory);
			assertNotNull(map);
			assertNotNull(map.getNodes());
			assertEquals(0, map.getNodes().size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}

	public void testGetNodes() {
		try {
			MyMap map = new MyMap(new MyNodeFactory());
			AbstractNode nodeToAdd = new MyNode(0, 0, '@');
			map.setNode(nodeToAdd);
			AbstractNode foundNode = map.getNode(0, 0);
			assertNotNull(foundNode);
			assertEquals(0, foundNode.getxPosition());
			assertEquals(0, foundNode.getyPosition());
			assertEquals('@', foundNode.getTerrainSymbol());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}
	
	public void testFindAdjacentNodes() {
		try {
			String mapName = MapLoaderTest.MAP1_NAME;
			MyMap testMap = MapLoaderTest.createTestMapFromFile(mapName);
			AbstractNode nodeAt11 = testMap.getNode(1, 1);
			assertFalse(nodeAt11.isWalkable());
			
			testMap.drawMapToStdOut();
			AbstractAlgorithm pathFinderAlgorithm = new MyAStarPathFinderAlgorithm(testMap);
			AbstractNode nodeAt00 = testMap.getNode(0, 0);
			List solutionPath = ((MyAStarPathFinderAlgorithm)pathFinderAlgorithm).findAdjacentNodesTo(nodeAt00);
			assertEquals(1, solutionPath.size());
		} catch (UndefinedPropertyException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}  	
	}

	public void testFindPath() {
		try {
			String mapName = MapLoaderTest.MAP1_NAME;
			MyMap testMap = MapLoaderTest.createTestMapFromFile(mapName);
			AbstractAlgorithm pathFinderAlgorithm = new MyAStarPathFinderAlgorithm(testMap);
			
			//List solutionPath = pathFinderAlgorithm.findPath(3, 3, 4, 4);  
			//assertEquals(2, solutionPath.size());
			
			List solutionPath = pathFinderAlgorithm.findPath(2, 3, 4, 4);  
			assertEquals(3, solutionPath.size());
			
			solutionPath = pathFinderAlgorithm.findPath(0, 0, 4, 4);  
			
			//solutionPath = pathFinderAlgorithm.findPathToGoal();
			assertEquals(6, solutionPath.size());
			testMap.drawMapToStdOut();
			testMap.plotSolutionOnMap(solutionPath);
			testMap.drawMapToStdOut();		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testFindPathForLargeMap() {
		try {
			String mapName = MapLoaderTest.LARGE_MAP_NAME;
			MyMap testMap = MapLoaderTest.createTestMapFromFile(mapName);
			AbstractAlgorithm pathFinderAlgorithm = new MyAStarPathFinderAlgorithm(testMap);
			List solutionPath = pathFinderAlgorithm.findPathToGoal();  //(oldX, oldY, newX, newY);
			testMap.drawMapToStdOut();
			testMap.plotSolutionOnMap(solutionPath);
			testMap.drawMapToStdOut();
			assertEquals(65, solutionPath.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}

}
