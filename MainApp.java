import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;

// Dependency downloader.java adn server.java

class MainApp {

	public static void main(String args[]) throws Exception {
	
		/*Starting Peer Server*/
		//Server s = new Server();
		ClientA cl = new ClientA("http://www.nith.ac.in/ece/syll_2015.pdf");
		cl.t.join();
	}
}