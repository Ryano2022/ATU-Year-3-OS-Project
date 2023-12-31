import java.net.Socket;
import java.io.*;

public class ServerThread extends Thread {
	Socket myConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message, name, ppsn, email, password, address, balance;
	String usersFilePath = "users.txt";
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
			System.out.println("Connection successful. ");

			// Read in the list of users from users.txt.
			userList.readUserInfoFromFile(usersFilePath);

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
				// Check the email exists using searchList.
				userList.searchList(email);
				// If it doesn't exist, ask for it again.
				while (userList.searchList(email).equalsIgnoreCase("Not found. ")) {
						sendMessage("E-mail not found. Please enter a valid e-mail: ");
						email = (String) in.readObject();
				}
				sendMessage("Email found. ");
				System.out.println("client> " + email);

				sendMessage("Please enter your password: ");
				password = (String) in.readObject();
				System.out.println("client> " + password);
				// Check if the password matches the password on the same line as the email.
				while (userList.searchList(email).contains(password) == false) {
						sendMessage("Password incorrect. Please enter a valid password: ");
						password = (String) in.readObject();
				}
				sendMessage("Password accepted. ");
				System.out.println("client> " + password);

				// Successful log-in message.
				sendMessage("Log-in successful. ");

				// Show a list of options that the user can now do.
				// 1. View balance.
				// 2. Lodge money.
				// 3. Withdraw money.
				// 4. Transfer money.
				// 5. Change password.
				// 6. Show all transactions.
				// 7. Show all users.
				// 8. Exit.
				
				boolean keepGoing = true;

				do{
					// Send the options list.
					sendMessage("Please select an option: \n1. View balance. \n2. Lodge money. \n3. Withdraw money. \n4. Transfer money. \n5. Change password. \n6. Show all transactions. \n7. Show all users. \n8. Exit.");
					message = (String) in.readObject();
					System.out.println("client> " + message);

					// View balance.
					if (message.equalsIgnoreCase("1")) {
						// Send the balance.
						sendMessage(userList.searchList(email).split(",")[5] + " is your balance. ");
					}
					// Lodge money.
					else if (message.equalsIgnoreCase("2")) {
						// Ask for the amount to lodge.
						sendMessage("Please enter the amount to lodge: ");
						message = (String) in.readObject();
						System.out.println("client> " + message);
						userList.addBalance(email, Double.parseDouble(message));
						sendMessage(userList.searchList(email).split(",")[5] + " is your new total. ");
						// Update the file.
						userList.printUpdatedUserInfoToFile(usersFilePath, email);
						// Success message.
						sendMessage("Lodged successfully. ");
					}
					// Withdraw money.
					else if (message.equalsIgnoreCase("3")) {
						// Ask for the amount to withdraw.
						sendMessage("Please enter the amount to withdraw: ");
						message = (String) in.readObject();
						System.out.println("client> " + message);
						userList.subtractBalance(email, Double.parseDouble(message));
						sendMessage(userList.searchList(email).split(",")[5] + " is your new total. ");
						// Update the file.
						userList.printUpdatedUserInfoToFile(usersFilePath, email);
						// Success message.
						sendMessage("Withdrawn successfully. ");
					}
					// Transfer money.
					else if (message.equalsIgnoreCase("4")) {
						String emailTo;

						// Ask for the amount to transfer.
						sendMessage("Please enter the amount to transfer: ");
						message = (String) in.readObject();
						System.out.println("client> " + message);
						// Ask for the email to transfer to.
						sendMessage("Please enter the email to transfer to: ");
						emailTo = (String) in.readObject();
						System.out.println("client> " + emailTo);
						
						// Check if the email exists using searchList.
						userList.searchList(emailTo);
						// If it doesn't exist, ask for it again.
						while (userList.searchList(emailTo).equalsIgnoreCase("Not found. ")) {
								sendMessage("E-mail not found. Please enter a valid e-mail: ");
								emailTo = (String) in.readObject();
						}
						sendMessage("Email found. ");
						System.out.println("client> " + emailTo);

						// Subtract the balance from the sender.
						userList.subtractBalance(email, Double.parseDouble(message));
						
						// Add the balance to the receiver.
						userList.addBalance(emailTo, Double.parseDouble(message));

						// Send the new balance. 
						sendMessage(userList.searchList(email).split(",")[5] + " is your new total. ");
						// Update the file.
						userList.printUpdatedUserInfoToFile(usersFilePath, email);
						userList.printUpdatedUserInfoToFile(usersFilePath, emailTo);
						// Success message.
						sendMessage("Transferred successfully. ");
					}
					// Change password.
					else if (message.equalsIgnoreCase("5")) {
						// Ask for the new password.
						sendMessage("Please enter your new password: ");
						message = (String) in.readObject();
						System.out.println("client> " + message);
						// Change the password.
						userList.changePassword(email, message);
						// Send the new password.
						sendMessage(userList.searchList(email).split(",")[3] + " is your new password. ");
						// Update the file.
						userList.printUpdatedUserInfoToFile(usersFilePath, email);
					}
					// Show all transactions.
					else if (message.equalsIgnoreCase("6")) {
						// Send the transactions.
						sendMessage("Not yet implemented. ");
					}
					// Show all users.
					else if (message.equalsIgnoreCase("7")) {
						// Send the users.
						sendMessage(userList.printAllUsers(1));
					}
					else {
						// Send an exiting message.
						sendMessage("Exiting. ");
						keepGoing = false;
					}
				}while(keepGoing == true);
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
				userList.printNewUserInfoToFile(usersFilePath);

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