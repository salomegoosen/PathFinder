package pathfinding;

/**
 * Example usage of this package.
 * 
 * @see MyNodeFactory
 * @see MyNode
 */
public class MyUsage {

	/**
	 * @param args
	 *  expects one arg for mapName to load
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println ("Usage: pathfinding.ExampleUsage test_map.txt");
			System.exit(0);
		}
		String mapName = args[0];
		MyMap myMap;
		try {
			MyMapLoader mapLoader = new MyMapLoader(new MyNodeFactory(), new MyAStarPathFinderAlgorithm());
			
			// load map from file
			myMap = mapLoader.load(mapName);
			// draw terrain before solution
			myMap.drawMap(mapName);
			// find solution
			myMap.findPathToGoal(); //(0, 0, 49, 49);

			// plot solution path on terrain map
			myMap.plotSolutionOnMap();
			
			// print terrain map with solution
			myMap.drawMap(mapName);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
