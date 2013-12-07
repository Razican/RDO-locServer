package utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import razican.db.NoDatabaseException;
import razican.db.SQLite3db;

/**
 * @author Razican (Iban Eguia)
 */
public class Locator {

	private static SQLite3db	db;

	/**
	 * Get the latitude of the given cell
	 * 
	 * @param cell - The cell to locate
	 * @return The latitude of the cell
	 */
	public static double getLatitude(int cell)
	{
		return get("latitude", cell);
	}

	/**
	 * Get the longitude of the given cell
	 * 
	 * @param cell - The cell to locate
	 * @return The longitude of the cell
	 */
	public static double getLongitude(int cell)
	{
		return get("longitude", cell);
	}

	private static double get(String column, int cell)
	{
		if (db == null)
		{
			try
			{
				db = new SQLite3db("data/database");
			}
			catch (NoDatabaseException e)
			{
				System.err.println("La base de datos no existe");
				System.exit(0);
			}
		}

		ResultSet r = db.rawQuery("SELECT " + column + " FROM CELL WHERE id = "
		+ cell + ";");
		double data = 0;
		try
		{
			data = r.getDouble(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Check if a cell is valid
	 * 
	 * @param cell - The cell integer to check
	 * @return If the given cell is valid or not
	 */
	public static boolean isvalid(int cell)
	{
		if (db == null)
		{
			try
			{
				db = new SQLite3db("data/database");
			}
			catch (NoDatabaseException e)
			{
				System.err.println("La base de datos no existe");
				System.exit(0);
			}
		}
		int rows = 0;
		try
		{
			rows = db.rawQuery(
			"SELECT COUNT(*) FROM CELL WHERE id = " + cell + ";").getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return rows > 0;
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
		int minutes = (int) (Math.abs(coordinate - degrees) * 60);
		double seconds = (Math.abs(coordinate - degrees) * 60 - minutes) * 60;

		return degrees + ":" + minutes + ":"
		+ String.format(Locale.ENGLISH, "%.5f", seconds);
	}
}