import java.net.Socket;
import java.io.*;

public class ServerThread extends Thread {
	Socket myConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message, name, ppsn, email, password, address, balance;
	String usersFilePath = "users";
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

			// Read in the list of users from users.txt.
			userList.readUserInfoFromFile(usersFilePath + ".txt");

			// Server comms.
			if (userList.hasUsers() == false) {
				sendMessage("No users exist. Please create an account. ");
				message = "2";
				System.out.println("server> " + message);
			} 
			else {
				sendMessage("Welcome to the banking app. Please select an option: \n1. Log-in. \n2. Create an account. ");
				message = (String) in.readObject();
				System.out.println("client> " + message);
			}

			// Log-in.
			if (message.equalsIgnoreCase("1")) {
				sendMessage("Please enter your email: ");
				email = (String) in.readObject();
				System.out.println("client> " + email);

				sendMessage("Please enter your password: ");
				password = (String) in.readObject();
				System.out.println("client> " + password);
			}
			// Create an account.
			else if (message.equalsIgnoreCase("2")) {
				// Name
				sendMessage("Please enter your name: ");
				name = (String) in.readObject();
				System.out.println("client> " + name);

				// PPSN
				sendMessage("Please enter your PPSN: ");
				ppsn = (String) in.readObject();
				// Check if it's unique.
				while (userList.isUniquePPSN(ppsn) == false) {
						sendMessage("PPSN already exists. Please enter a unique PPSN: ");
						ppsn = (String) in.readObject();
				}
				sendMessage("PPSN accepted. ");
				System.out.println("client> " + ppsn);

				// Email
				sendMessage("Please enter your email: ");
				email = (String) in.readObject();
				// Check if it's unique.
				while (userList.isUniqueEmail(email) == false) {
						sendMessage("E-mail already exists. Please enter a unique e-mail: ");
						email = (String) in.readObject();
				}
				sendMessage("Email accepted. ");
				System.out.println("client> " + email);

				// Password
				sendMessage("Please enter your password: ");
				password = (String) in.readObject();
				System.out.println("client> " + password);

				// Address
				sendMessage("Please enter your address: ");
				address = (String) in.readObject();
				System.out.println("client> " + address);

				// Balance
				sendMessage("Please enter your balance: ");
				balance = (String) in.readObject();
				System.out.println("client> " + balance);

				// Add the user to the list.
				userList.addUser(name, ppsn, email, password, address, balance);
				userList.printNewUserInfoToFile(usersFilePath + ".txt");

				// Send success message.
				sendMessage("Account created successfully. \nPlease re-run the program to log-in. ");
			}

			in.close();
			out.close();
		} 
		catch (ClassNotFoundException classnot) {
			System.err.println("Data received in unknown format. ");
		} 
		catch (IOException e) {}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server> " + msg);
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}