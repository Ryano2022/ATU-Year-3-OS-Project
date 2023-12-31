import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	String response;
	Scanner input;

	Requester() {
		input = new Scanner(System.in);
	}

	void run() {
		try {

			// 1. Creating a socket to connect to the server.
			requestSocket = new Socket("127.0.0.1", 2004);
			System.out.println("Connected to localhost in port 2004. ");

			// 2. Get the input and output streams.
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			// 3. Communicating with the server.
			try {
				// Welcome message.
				message = (String) in.readObject();
				System.out.println(message);

				// User input.
				if (message.equalsIgnoreCase("No users exist. Please create an account. ")) {
					createAccount();
				} 
				else {
					// 1 = Log-in.
					// 2 = Create an account.
					response = input.nextLine();
					sendMessage(response);

					if (response.equalsIgnoreCase("1")) {
						// Log-in.
						// Email
						message = (String) in.readObject();
						System.out.println(message);
						response = input.nextLine();
						sendMessage(response);
				
						// Check if the email exists.
						message = (String) in.readObject();
						while (message.equalsIgnoreCase("E-mail not found. Please enter a valid e-mail: ")) {
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);
								message = (String) in.readObject();
						}
						System.out.println(message);

						// Password
						message = (String) in.readObject();
						System.out.println(message);
						response = input.nextLine();
						sendMessage(response);
				
						// Check if the password is correct.
						message = (String) in.readObject();
						while (message.equalsIgnoreCase("Password incorrect. Please enter a valid password: ")) {
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);
								message = (String) in.readObject();
						}
						System.out.println(message);

						// Successful log-in message.
						message = (String) in.readObject();
						System.out.println(message);

						boolean keepGoing = true;

						do{
							// Show a list of options that the user can now do.
							message = (String) in.readObject();
							System.out.println(message);
							response = input.nextLine();
							sendMessage(response);

							// View balance.
							if (response.equalsIgnoreCase("1")) {
								// Send the balance.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Lodge money.
							else if(response.equalsIgnoreCase("2")) {
								// Ask for the amount to lodge.
								message = (String) in.readObject();
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);

								// Receive the new balance.
								message = (String) in.readObject();
								System.out.println(message);

								// Success message.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Withdraw money.
							else if(response.equalsIgnoreCase("3")) {
								// Ask for the amount to withdraw.
								message = (String) in.readObject();
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);

								// Receive the new balance.
								message = (String) in.readObject();
								System.out.println(message);

								// Success message.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Transfer money.
							else if(response.equalsIgnoreCase("4")) {
								// Ask for the amount to transfer.
								message = (String) in.readObject();
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);

								// Ask for the email to transfer to.
								message = (String) in.readObject();
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);

								// Receive the new balance.
								message = (String) in.readObject();
								System.out.println(message);

								// Success message.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Change password.
							else if(response.equalsIgnoreCase("5")) {
								// Input the new password.
								message = (String) in.readObject();
								System.out.println(message);
								response = input.nextLine();
								sendMessage(response);

								// Receive the new password.
								message = (String) in.readObject();
								System.out.println(message);

								// Success message.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Show all transactions.
							else if(response.equalsIgnoreCase("6")) {
								// Receive the transactions.
								message = (String) in.readObject();
								System.out.println(message);
							}
							// Show all users.
							else if(response.equalsIgnoreCase("7")) {
								// Receive the users.
								message = (String) in.readObject();
								System.out.println(message);
							}
							else {
								message = (String) in.readObject();
								System.out.println(message);
								keepGoing = false;
							}
						}while(keepGoing == true);
				} 
					else if (response.equalsIgnoreCase("2")) {
						// Create an account.
						createAccount();
					} 
					else {
						System.out.println("Invalid input. ");
					}
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host! ");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4. Closing connection.
			try {
				in.close();
				out.close();
				requestSocket.close();
			} 
			catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			// System.out.println("\nclient> " + msg);
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void createAccount() {
		try {
			// Name
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);

			// PPSN
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);
			message = (String) in.readObject();
			while (message.equalsIgnoreCase("PPSN already exists. Please enter a unique PPSN: ")) {
					System.out.println(message);
					response = input.nextLine();
					sendMessage(response);
					message = (String) in.readObject();
			}
			System.out.println(message);

			// Email
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);
			message = (String) in.readObject();
			while (message.equalsIgnoreCase("E-mail already exists. Please enter a unique e-mail: ")) {
					System.out.println(message);
					response = input.nextLine();
					sendMessage(response);
					message = (String) in.readObject();
			}
			System.out.println(message);

			// Password
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);

			// Address
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);

			// Balance
			message = (String) in.readObject();
			System.out.println(message);
			response = input.nextLine();
			sendMessage(response);

			// Success message.
			message = (String) in.readObject();
			System.out.println(message);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Requester client = new Requester();
		client.run();
	}
}