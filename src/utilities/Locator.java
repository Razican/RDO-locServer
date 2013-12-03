package utilities;

/**
 * @author Razican (Iban Eguia)
 */
public class Locator {

	/**
	 * Get the latitude of the given cell
	 * 
	 * @param cell - The cell to locate
	 * @return The latitude of the cell
	 */
	public static double getLatitude(int cell)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Get the longitude of the given cell
	 * 
	 * @param cell - The cell to locate
	 * @return The longitude of the cell
	 */
	public static double getLongitude(int cell)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Check if a cell is valid
	 * 
	 * @param cell - The cell integer to check
	 * @return If the given cell is valid or not
	 */
	public static boolean isvalid(int cell)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Formates a coordinate in dd:mm:ss.ss format
	 * 
	 * @param coordinate - The coordinate to format
	 * @return The formatted string
	 */
	public static String format(double coordinate)
	{
		int degrees = (int) coordinate;
		int minutes = (int) (coordinate - degrees) * 60;
		double seconds = ((coordinate - degrees) * 60 - minutes) * 60;

		return degrees + ":" + minutes + ":" + seconds;
	}
}