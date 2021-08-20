import java.util.*;
import java.net.*;
import java.io.*;

/*
	The tracker object is a shared object 
	responsible for saving information of
	peers providing downloading service

	Todo -
		Efficent Load Balancing
*/


class Tracker {
	// Set of online peers
	HashSet<String> ipList;
	public Tracker() {
		ipList = new HashSet<String>();
	}
	// Adds ip to the set of peers
	synchronized public void add(String ip) {

		System.out.println("Adding " + ip);
		ipList.add(ip);
	}
	// Feeds information of online peers to os (stream)
	synchronized public void refresh(PrintWriter os) {

		for(String s: ipList) {
			System.out.println("Written "+s);
			os.println(s);
		}
		System.out.println("Written .");

		os.println(".");
	}
	// Deletes the ip from the set of online peers
	synchronized public void delete(String ip) {
		if(ipList.contains(ip))
			ipList.remove(ip);
	}
}

/*
	This object handles the connection 
	with a connection a peer server or 
	peer client
*/

class Handler implements Runnable{

	Thread t;
	Socket s;
	// Stream for socket input
	BufferedReader is;
	// Stream for socket output
	PrintWriter os;
	// Tracker object storing information
	Tracker tracker;
	// Ip address of connected peer
	String clientIp;

	public Handler(Socket s,Tracker tracker) {
	try{
		t = new Thread(this);
		this.s = s;
		this.tracker = tracker;
		System.out.println(s);
		// Adding this client to tracker list
		clientIp = s.getInetAddress().getHostAddress();
		//this.tracker.add(clientIp);
		is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		os = new PrintWriter(s.getOutputStream(),true);
	}
	catch(Exception e) {
		System.out.println(e);
	}
	t.start();
	}


	public void run() {

		// Accepting requests and preforming actions
		try {

		while(!s.isInputShutdown()) {
			System.out.println("In thread");
			String request = is.readLine();
			/*
				Multiplex according to the request;
			*/
			System.out.println("request == " + request);
			/*Identifying the peer as server or client*/
			if(request.equals("Server")) tracker.add(clientIp);
			/*Request to get the list o peers*/
			else if(request.equals("refresh")) tracker.refresh(os);
			/*Requst to disconnect this peer*/
			else if(request.equals("disconnect")) break;

			else throw new Exception("Undefined request made");
		}

	}
		catch(Exception e) {
			System.out.println(e);
		} 
		finally {
			try {
				is.close();
				os.close();
				s.close();
			}
			catch(Exception e) {
				System.out.println(e);
			}
			finally {
			tracker.delete(clientIp);
			System.out.println(clientIp + " disconnected");
			}
		}
		return;
	}

}

/*
	Main Server class that manages
	the tracker server
*/

class Server {

	final static int TRACKER_PORT = 8000;
	public static void main(String args[]) {

		try (ServerSocket s = new ServerSocket(TRACKER_PORT)) {
		System.out.println("The tracker server is running on port " + TRACKER_PORT);

		/*
			Accepting connection to take info
				Connecting with every new user and saving its information in a shared object (Tracker)
				Handle and exchange messages for success or faliure
				
				Timely ping the all clients to check if someone has died and refresh								
		*/

		
		Tracker t = new Tracker();
		while(true) {
			Socket cl = s.accept();
			new Handler(cl,t);
		}

	}

	catch(Exception e) {
		System.out.println("Unable to open socket");
	}

	}
	/*
		Function to timely update the 
		list of	online peers 
	*/
	public void update(){

	}
}