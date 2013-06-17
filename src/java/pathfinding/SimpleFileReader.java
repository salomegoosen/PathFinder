package pathfinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SimpleFileReader 
{
	protected BufferedReader reader;
	protected String line;
	protected int index;
	
	/**
	 * Creates a reader
	 * @param file
	 * @throws java.io.IOException
	 *
	 */
	public SimpleFileReader(File file) throws java.io.IOException
	{
		this(new BufferedReader(new FileReader(file)));
	}

	
	/**
	 * @param aReader
	 */
	private SimpleFileReader(BufferedReader aReader)
	{
		reader = aReader;
	}

	/**
	 * @throws java.io.IOException
	 */
	public void close() throws java.io.IOException
	{
		reader.close();
		reader = null;
		line = null;
		index = 0;
	}

	/**
	 * * Is this reader ready to give content
	 */
	public boolean ready()
	{
		return (reader != null && line != null);
	}

	/**
	 * * Read one line
	 */
	public String nextLine()
	{
		readNextLine();
		if (line == null) 
			return (null);
		while (line.length() == 0)  // skip blank lines
		{		
			readNextLine();
			if (line == null) 
				return (null);
		}

		return line;
	}

	/**
	 * * Read the next line from the file
	 */
	private void readNextLine()
	{
		try
		{
			line = reader.readLine();
		} catch (Exception ex)
		{
			line = null;
		}
	}
}