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
	
		/*Starting Peer Server*/
		Server s = new Server();
		ClientA cl = new ClientA("http://www.nith.ac.in/ece/syll_2015.pdf");
		s.t.join();
		cl.t.join();
	}
}