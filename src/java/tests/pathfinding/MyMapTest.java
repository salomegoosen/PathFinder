package tests.pathfinding;

import java.util.List;

import pathfinding.AbstractNode;
import pathfinding.MyAStarPathFinderAlgorithm;
import pathfinding.MyMap;
import pathfinding.MyNode;
import pathfinding.MyNodeFactory;
import pathfinding.NodeFactory;
import junit.framework.TestCase;

public class MyMapTest extends TestCase {

	public void testMyMap() {
		NodeFactory nodeFactory = new MyNodeFactory();
		try {
			MyMap map = new MyMap(nodeFactory, new MyAStarPathFinderAlgorithm());
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
			MyMap map = new MyMap(new MyNodeFactory(), new MyAStarPathFinderAlgorithm());
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

	public void testFindPath() {
		try {
			String mapName = MapLoaderTest.MAP1_NAME;
			MyMap testMap = MapLoaderTest.createTestMapFromFile(mapName);
			List solutionPath = testMap.findPathToGoal();  //(oldX, oldY, newX, newY);
			testMap.drawMap(mapName);
			testMap.plotSolutionOnMap();
			testMap.drawMap(mapName);
			assertEquals(6, solutionPath.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}

	public void testFindPathForLargeMap() {
		try {
			String mapName = MapLoaderTest.LARGE_MAP_NAME;
			MyMap testMap = MapLoaderTest.createTestMapFromFile(mapName);
			List solutionPath = testMap.findPathToGoal();  //(oldX, oldY, newX, newY);
			testMap.drawMap(mapName);
			testMap.plotSolutionOnMap();
			testMap.drawMap(mapName);
			assertEquals(69, solutionPath.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}

}
