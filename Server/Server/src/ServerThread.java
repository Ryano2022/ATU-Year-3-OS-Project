import java.net.Socket;
import java.io.*;

public class ServerThread extends Thread {
	Socket myConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message, name, ppsn, email, password, address, balance;
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
			if (userList.hasUsers() == false) {
				sendMessage("No users exist. Please create an account. ");
				message = "2";
			} 
			else {
				sendMessage("Welcome to the banking app. Please select an option: \n1. Create an account. \n2. Log-in.\n");
				message = (String) in.readObject();
			}

			// Log-in.
			if (message.equalsIgnoreCase("1")) {
				sendMessage("Please enter your email: ");
				email = (String) in.readObject();

				sendMessage("Please enter your password: ");
				password = (String) in.readObject();
			}
			// Create an account.
			else if (message.equalsIgnoreCase("2")) {
				// Name
				sendMessage("Please enter your name: ");
				name = (String) in.readObject();
				
				// PPSN
				sendMessage("Please enter your PPSN: ");
				ppsn = (String) in.readObject();

				// Email
				sendMessage("Please enter your email: ");
				email = (String) in.readObject();

				// Password
				sendMessage("Please enter your password: ");
				password = (String) in.readObject();

				// Address
				sendMessage("Please enter your address: ");
				address = (String) in.readObject();

				// Balance
				sendMessage("Please enter your balance: ");
				balance = (String) in.readObject();

				// Add the user to the list.
				userList.addUser(name, ppsn, email, password, address, balance);
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