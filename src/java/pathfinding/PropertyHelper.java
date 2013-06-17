package pathfinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyHelper {
	public static final String PROPERTY_NAME_START_NODE_SYMBOL = "StartNodeSymbol";
	public static final String PROPERTY_NAME_GOAL_NODE_SYMBOL = "GoalNodeSymbol";
	public static final String PROPERTY_NAME_NONWALKABLE_NODE_SYMBOL = "NonWalkableSymbol";
	public static final String PROPERTY_RESOURCE_FOLDER_PATH = "ResourceFolderPath";
	static final String DIST_PATH_NAME="dist"+File.separator;
	public static final String CONFIG_FILE_PATH = "config";
	
	/* TODO - For EXTENDIBILITY: optimise to allow a config files per map file, 
	 * 	eg large_map.txt to have large_map.properties
	 * and test_map.txt to use test_map.properties
	 */ 
	private static final String CONFIG_FILE_NAME = "terrain.properties";
	
	// the properties loaded from CONFIG_FILE_NAME
	private static Properties properties;  
	
	 // symbol indicating the start node, eg @
	private static char startNodeSymbol; 
	
	// symbol indicating the goal node, eg X
	private static char goalNodeSymbol;  
	
	// symbol indicating the non-walkable nodes, eg X
	// TODO - extend to maybe have multiple non-walkable symbols
	private static char notWalkableSymbol;  
	
	// symbol indicating the path walked/found
	public static final char PATH_SYMBOL = '#';
	
	// the resource path name where the properties files and data resides
	private static String resourcesPathName;

	private static PropertyHelper instance;  // singleton instance to manage the properties

	/*
	 * 
	 */
	/**
	 * @return instance of PropertyHelper
	 * @throws Exception
	 */
	public synchronized static PropertyHelper getInstance() throws Exception {
		if (instance == null) {
			instance = new PropertyHelper();
		}
		return instance;
	}
	
	/**
	 * private constructor
	 * @throws Exception
	 */
	private PropertyHelper() throws Exception {
		resourcesPathName = null;
		if (properties == null) {
			properties = new Properties();
			File file = new File(CONFIG_FILE_PATH + File.separator
					+ CONFIG_FILE_NAME);
			if (!file.exists()) {
				file = new File(DIST_PATH_NAME+File.separator+CONFIG_FILE_PATH
						+ File.separator + CONFIG_FILE_NAME);
			}
			
			if (!file.exists())
				throw new Exception("Could not find file " + file.getAbsolutePath()+"]");

			System.out.println("Found file - loading properties from "
					+ file.getAbsolutePath());
			properties.load(new FileInputStream(file));
			loadStartNodeSymbol();
			loadGoalNodeSymbol();
			notWalkableSymbol = getMandatoryProperty(
					PROPERTY_NAME_NONWALKABLE_NODE_SYMBOL).charAt(0);
		}	
		
	}

	private static String getMandatoryProperty(String propertyName)
			throws UndefinedPropertyException {
		String property = properties.getProperty(propertyName);
		if (property == null)
			throw new UndefinedPropertyException(propertyName);
		return property.trim();
	}

	public final String getResourcesPathName()
			throws UndefinedPropertyException {
		if (resourcesPathName == null)
			resourcesPathName = getMandatoryProperty(PROPERTY_RESOURCE_FOLDER_PATH);
		return resourcesPathName;
	}
	
	public static char getStartNodeSymbol() {
		return startNodeSymbol;
	}

	public static char getGoalNodeSymbol() {
		return goalNodeSymbol;
	}

	public static char getNotWalkableSymbol() {
		return notWalkableSymbol;
	}

	/*
	 * read configured weight for symbol
	 */
	public int weightForSymbol(char terrainSymbol)
			throws UndefinedPropertyException {
		if (isStartNodeSymbol(terrainSymbol) || isGoalNodeSymbol(terrainSymbol))
			return 0;
		String weightString = getMandatoryProperty("W" + terrainSymbol);
		int weight = Integer.parseInt(weightString);
		return weight;
	}

	private static final void loadStartNodeSymbol() throws UndefinedPropertyException {
		startNodeSymbol = getMandatoryProperty(PROPERTY_NAME_START_NODE_SYMBOL)
				.charAt(0);
	}

	private static final void loadGoalNodeSymbol() throws UndefinedPropertyException {
		goalNodeSymbol = getMandatoryProperty(PROPERTY_NAME_GOAL_NODE_SYMBOL)
				.charAt(0);
	}

	private boolean isGoalNodeSymbol(char terrainSymbol) {
		return terrainSymbol == goalNodeSymbol;
	}

	private boolean isStartNodeSymbol(char terrainSymbol) {
		return (terrainSymbol == startNodeSymbol);
	}

}
