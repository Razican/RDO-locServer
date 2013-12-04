package application;

import java.io.File;
import java.io.IOException;

import network.Server;
import razican.db.NoDatabaseException;
import razican.db.SQLite3db;
import razican.utils.KeyboardUtils;
import utilities.Locator;

/**
 * @author Razican (Iban Eguia)
 */
public class Launch {

	private static Server	server;
	private static int		port;

	/**
	 * Launches the server
	 * 
	 * @param args - The arguments for the application
	 */
	public static void main(String[] args)
	{
		if (args[0].equals("init"))
		{
			File f = new File("data/database.sqlite3");

			if ( ! f.exists())
			{
				f.mkdirs();
				try
				{
					f.createNewFile();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
			}

			SQLite3db db = null;
			try
			{
				db = new SQLite3db("data/database");
			}
			catch (NoDatabaseException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			init_db(db);
		}
		else if ((port = Integer.parseInt(args[0])) > 1023 && port < 65536)
		{
			(new Thread()
			{

				@Override
				public void run()
				{
					try
					{
						server = new Server(port);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();

			try
			{
				String command;
				while ( ! (command = KeyboardUtils.readString()).equals("exit"))
				{
					if (command.contains("GETCOORD"))
					{
						try
						{
							int cell = Integer.parseInt(command.split(" ")[1]);
							String response = "224 OK ";
							response += Locator.format(Locator
							.getLatitude(cell)) + ";";
							response += Locator.format(Locator
							.getLongitude(cell));

							System.out.println(response);
						}
						catch (NullPointerException e)
						{
							System.err
							.println("No se ha introducido la celda.");
						}
					}
				}
				server.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void init_db(SQLite3db db)
	{
		db.rawModify("CREATE TABLE USER ("
		+ "\"id\" INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT,"
		+ "\"username\" TEXT NOT NULL UNIQUE, \"password\" TEXT NOT NULL);");

		db.rawModify("CREATE TABLE CELL ("
		+ "\"id\" INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT,"
		+ "\"latitude\" REAL NOT NULL, \"longitude\" REAL NOT NULL);");

		// TODO populate
	}
}