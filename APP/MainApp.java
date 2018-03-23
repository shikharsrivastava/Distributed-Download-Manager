import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;

// Dependencies downloader.java and server.java


/*

	Developement stage
*/

class MainApp {

	public static void main(String args[]) throws Exception {
	
		/*Starting Peer Server for all other clients*/
		Server s = new Server();

		/*Now asking users for download links*/
		System.out.println("Enter the download link");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String link = br.readLine();
		ClientA cl = new ClientA(link);
		s.t.join();
		cl.t.join();
	}
}