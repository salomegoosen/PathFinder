package tests.pathfinding;

import java.util.ArrayList;

import pathfinding.AbstractNode;
import pathfinding.MyAStarPathFinderAlgorithm;
import pathfinding.MyNodeFactory;
import pathfinding.MyMap;
import pathfinding.NodeFactory;
import junit.framework.TestCase;

public class MyNodeFactoryTest extends TestCase {

	public void testGetNodeAt() {
		MyMap map;
		try {
			NodeFactory nodeFactory = new MyNodeFactory();
			map = new MyMap(nodeFactory, new MyAStarPathFinderAlgorithm());
			AbstractNode startNode = nodeFactory.createNode(0,0,'0');
			map.setNode(startNode);
			AbstractNode nextNode = nodeFactory.createNode(0,1,'1');
			map.setNode(nextNode);
			assertEquals(1, map.getNodes().size());			
			assertEquals(2, (((ArrayList) map.getNodes().get(0))).size());
			
			AbstractNode node = map.getNode(0, 1);
			assertEquals(0, node.getxPosition());
			assertEquals(1, node.getyPosition());
		} catch (Exception ex) {
			fail (ex.getMessage());
		}
	}
	
	public void testUniqueStartNode() {
		MyMap map;
		try {
			NodeFactory nodeFactory = new MyNodeFactory();
			map = new MyMap(nodeFactory, new MyAStarPathFinderAlgorithm());
			AbstractNode startNode = nodeFactory.createNode(0,0,'@');
			map.setNode(startNode);
			assertTrue(map.getNodes().size() > 0);
			
			AbstractNode nextNode = nodeFactory.createNode(0,1,'@');  
			map.setNode(nextNode); // should throw exception

		} catch (Exception ex) {
			// failed as expected
		}
	}

}
