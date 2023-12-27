import java.net.Socket;
import java.io.*;

public class ServerThread extends Thread {
	Socket myConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	AllUsers userList;

	public ServerThread(Socket s, AllUsers registeredUsers) {
		myConnection = s;
		userList = registeredUsers;
	}

	public void run() {
		try {
			out = new ObjectOutputStream(myConnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(myConnection.getInputStream());

			// Server comms.
			if (!userList.hasUsers()) {
				sendMessage("No users exist. Please create an account. \n1. Create an account. \n2. Log-in.\n");
				message = (String) in.readObject();
			} 
			else {
				sendMessage("Welcome to the banking app. Please select an option: \n1. Create an account. \n2. Log-in.\n");
				message = (String) in.readObject();
			}

			// Log-in.
			if (message.equalsIgnoreCase("1")) {

			}
			// Create an account.
			else if (message.equalsIgnoreCase("2")) {

			}

			in.close();
			out.close();
		} catch (ClassNotFoundException classnot) {
			System.err.println("Data received in unknown format. ");
		} catch (IOException e) {

		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}