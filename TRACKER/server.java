import java.util.*
import java.net.*


class server {

	final TRACKER_PORT = 5000;
	public static void main() {

		try (ServerSocket s = new ServerSocket(TRACKER_PORT)) {
		System.out.println("The tracker server is running on port " + port);

		/* Accepting connection to take info*/

	}

	catch(Exception e) {
		System.out.println("Unable to open socket");
	}

	}
}