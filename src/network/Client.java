package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Razican (Iban Eguia)
 */
public class Client {

	private Socket				socket;
	private DataOutputStream	output;
	private BufferedReader		input;
	private String				user;
	private String				id;
	private boolean				open;
	private boolean				isAuthenticated;

	/**
	 * Creates a new client
	 * 
	 * @param socket - The socket for the client
	 * @throws IOException - If there is an exception with the buffers
	 */
	public Client(Socket socket) throws IOException
	{
		this.socket = socket;
		this.output = new DataOutputStream(socket.getOutputStream());
		this.input = new BufferedReader(new InputStreamReader(
		socket.getInputStream()));
		this.id = UUID.randomUUID().toString();
		this.open = true;
		this.isAuthenticated = false;
	}

	/**
	 * Reads a string from the client
	 * 
	 * @return The string sent by the client
	 */
	public String read()
	{
		try
		{
			String l = input.readLine();
			if (l != null)
			{
				System.out.println("Command received:");
				System.out.println(l);
			}
			return l;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param line - The line to write to the client
	 * @throws IOException - If it was unable to send the message to the client
	 */
	public void write(String line)
	{
		try
		{
			output.writeBytes(line + "\n");
			System.out.println("Sending response:");
			System.out.println(line);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Closes the connection with the client
	 * 
	 * @throws IOException - If there is an exception when closing the
	 *             connection
	 */
	public void close() throws IOException
	{
		this.output.close();
		this.input.close();
		this.socket.close();

		this.open = false;
	}

	/**
	 * @return If the connection is open
	 */
	public boolean isOpen()
	{
		return open;
	}

	/**
	 * @return If the user is authenticated
	 */
	public boolean isAuthenticated()
	{
		return isAuthenticated;
	}

	/**
	 * Sets the user for the client
	 * 
	 * @param user - The user for the client
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * Checks the password of the user
	 * 
	 * @param password - The password of the user, SHA-1 encoded
	 * @return If the password is correct
	 */
	public boolean checkPassword(String password)
	{
		return isAuthenticated = (user != null && user.equals("admin") && password
		.equals("8cb2237d0679ca88db6464eac60da96345513964")); // pass:12345
	}

	@Override
	public String toString()
	{
		String c = "";
		if (user != null)
		{
			c += user + " @ ";
		}
		c += socket.getInetAddress().getHostAddress();
		c += " - ID: " + id.substring(0, 7);

		return c;
	}
}