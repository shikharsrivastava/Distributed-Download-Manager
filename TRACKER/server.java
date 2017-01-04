import java.util.*
import java.net.*


class server {

	final TRACKER_PORT = 5000;
	public static void main() {

		try (ServerSocket s = new ServerSocket(TRACKER_PORT)) {
		System.out.println("The tracker server is running on port " + port);

		/*
			Accepting connection to take info
				Connect with every new user and save its information in a global object (Registration)
				Make seperate thread for each client
				Handle and exchange messages for success or faliure

				Timely ping the all clients to check if someone has died
				Make a Request object to manage a any sort of request from the corresponding client (Online User List, check if die loist,etc)
								
		*/



	}

	catch(Exception e) {
		System.out.println("Unable to open socket");
	}

	}
}