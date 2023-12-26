import java.io.*;
import java.net.*;

public class Provider {
	public static void main(String args[]) {
		ServerSocket providerSocket;

		try {
			providerSocket = new ServerSocket(2004, 10);

			while (true) {
				// 2. Wait for connection
				System.out.println("Waiting for connection");

				Socket connection = providerSocket.accept();
				ServerThread T1 = new ServerThread(connection);
				T1.start();
			}

			// providerSocket.close();
		}

		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}