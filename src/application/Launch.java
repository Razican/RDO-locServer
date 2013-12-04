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
		if (args.length != 1)
		{
			System.exit(1);
		}
		if (args[0].equals("init"))
		{
			File f = new File("data/database.sqlite3");

			if ( ! f.exists())
			{
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
						catch (ArrayIndexOutOfBoundsException e)
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
		+ "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ "\"username\" TEXT NOT NULL UNIQUE, \"password\" TEXT NOT NULL);");

		db.rawModify("CREATE TABLE CELL ("
		+ "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ "\"latitude\" REAL NOT NULL, \"longitude\" REAL NOT NULL);");
		// TODO constraint check latitude && longitude

		// Users
		db
		.rawModify("INSERT INTO USER (username, password) VALUES (\"admin\", \"8cb2237d0679ca88db6464eac60da96345513964\");");

		// Cells
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(40.25947,-2.4852691);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(42.65894,0.154869);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(50.561889,134.1877894);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(10.1984,-158.298189);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(-16.59478,85.91894);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(-25.64891,95.8819);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(19.8191561,89.4982);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(-16.849,8.949);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(42.16894,-2.16849);");
		db.rawModify("INSERT INTO CELL (latitude, longitude) VALUES "
		+ "(41.6548,-1.949856);");

		db.close();
	}
}