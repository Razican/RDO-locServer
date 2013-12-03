package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import utilities.CommandAnalizer;
import utilities.Locator;


/**
 * @author Razican (Iban Eguia)
 */
public class Server {

	private ServerSocket	server;
	private Vector<Client>	clients;

	/**
	 * Creates a server
	 * 
	 * @param port - The port for the server
	 * @throws IOException - If there is an exception when creating the socket
	 */
	public Server(int port) throws IOException
	{
		this.server = new ServerSocket(port);
		this.clients = new Vector<Client>();

		System.out.println("Server listening on port " + port);

		acceptNewClient();
	}

	private void acceptNewClient()
	{
		System.out.println("Waiting for a new client");
		(new Thread()
		{

			@Override
			public void run()
			{
				try
				{
					Client c = new Client(server.accept());
					clients.add(c);

					System.out.println("Client " + c + " accepted");
					System.out.println(clients.size() + " client(s) in server");

					acceptNewClient();

					String line = null;

					while ((line = c.read()) != null
					&& ! CommandAnalizer.getCommand(line).equals("SALIR")
					&& ! c.isOpen())
					{
						doCommand(line, c);
					}

					if (line != null)
					{
						c.write("318 OK Adiós.");
					}

					c.close();
					clients.remove(c);

					System.out.println("Client " + c + " closed");
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void doCommand(String line, Client c)
	{
		switch (CommandAnalizer.getCommand(line))
		{
			case "USER":
				String user = CommandAnalizer.getParameter(line);
				if (user != null)
				{
					c.setUser(user);
					c.write("311 OK Bienvenido" + user + ".");
				}
				else
				{
					c.write("511 ERR Falta el nombre de usuario.");
				}
			break;
			case "PASS":
				String password = CommandAnalizer.getParameter(line);
				if (password != null)
				{
					if (c.checkPassword(password))
					{
						c.write("312 OK Bienvenido al sistema.");
					}
					else
					{
						c.write("512 ERR La clave es incorrecta.");
					}
				}
				else
				{
					c.write("513 ERR Falta la clave.");
				}
			break;
			case "GETCOORD":
				String cell = CommandAnalizer.getParameter(line);
				int cellInt;
				if (cell != null
				&& Locator.isvalid(cellInt = Integer.parseInt(cell)))
				{
					String response = "224 OK ";
					response += Locator.format(Locator.getLatitude(cellInt))
					+ ";";
					response += Locator.format(Locator.getLongitude(cellInt));

					c.write(response);
				}
				else if (cell == null)
				{
					c.write("528 ERR Falta parámetro cell_id.");
				}
				else
				{
					c.write("527 ERR Celda desconocida.");
				}
		}
	}

	/**
	 * Closes the server
	 * 
	 * @throws IOException - If the socket fails to close
	 */
	public void close() throws IOException
	{
		Iterator<Client> i = clients.iterator();

		while (i.hasNext())
		{
			try
			{
				Client c = i.next();
				c.close();
				clients.remove(c);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		server.close();
	}
}