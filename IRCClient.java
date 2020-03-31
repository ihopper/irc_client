import java.net.*;
import java.io.*;


public class IRCClient {

	//Initialize the input and output streams
	public static Socket socket				= null;
	public static BufferedReader reader		= null; //What we receive from the host
	public static BufferedWriter writer		= null; //What we send to the host
	public static String userPass			= null;

	//Client constructor
	public static void ChatClient(String serverAddress, int serverPort, String nick, String login, String channel) {

		try
		{
			//Establish a connection to the host
			socket = new Socket(serverAddress, serverPort);

			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			//Login to the host
				//writer.write("PASS " + userPass + "\r\n");
				writer.write("NICK " + nick + "\r\n");
				writer.write("USER " + login + "\r\n");
				writer.flush();

			//Read lines from the server to make sure we are connected
			String line = null;

			//debug
			System.out.println(line = reader.readLine());

			while ((line = reader.readLine()) != null) {
				if (line.indexOf("004") >= 0) {
					// We are logged in
					System.out.println("Logged in.");
					break;
				} else if (line.indexOf("433") >= 0) {
					System.out.println("Nickname is already in use");
					return;
				}
			}
	
		// Join the specified channel
			writer.write("JOIN #" + channel + "\r\n");
			writer.flush();

		// Keep reading lines from the host.
			while ((line = reader.readLine()) != null) {
				if (line.toLowerCase().startsWith("PING ")) {
					//Respond with PONG tro avoid being disconnected
					writer.write("PONG " + line.substring(5) + "\r\n");
					writer.flush();
				}
				else {
					//Print the line received
					System.out.println(line);
				}
			} //end while

		} catch(Exception e) {
			System.out.println(e);
		}
		
		//Close the connection
		try
		{
			//message.close();
			//input.close();
			//output.close();
			socket.close();
			System.out.println("Connection closed by client.");
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	//Instantiate the client object
	public static void main(String[] args){

		//Connection details
		String serverAddress 	= "irc.chat.twitch.tv";
		int serverPort 			= 6667;
		String nick 			= args[0];
		String login 			= args[1];
		String channel 			= args[2];

		//debug
		//System.out.println("Nick: " + nick + ", Login: " + login + ", Channel: " + channel + "\n");
		ChatClient(serverAddress, serverPort, nick, login, channel);


	} //end main

} //end class
