import java.util.*;
import java.net.*;
import java.io.*;

class Tracker {

	HashSet<String> ipList;
	public Tracker() {
		ipList = new HashSet<String>();
	}

	synchronized public void add(String ip) {

		System.out.println("Adding " + ip);
		ipList.add(ip);
	}

	synchronized public void refresh(PrintWriter os) {

		for(String s: ipList) {
			os.println(s);
		}
		os.println(".");
	}

	synchronized public void delete(String ip) {
		if(ipList.contains(ip))
			ipList.remove(ip);
	}
}

class Handler implements Runnable{

	Thread t;
	Socket s;
	BufferedReader is;
	PrintWriter os;
	Tracker tracker;
	String clientIp;

	public Handler(Socket s,Tracker tracker) {
	try{
		t = new Thread(this);
		this.s = s;
		this.tracker = tracker;
		System.out.println(s);
		// Adding this client to tracker list
		clientIp = s.getInetAddress().getHostAddress();
		this.tracker.add(clientIp);
		is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		os = new PrintWriter(s.getOutputStream(),true);
	}
	catch(Exception e) {
		System.out.println(e);
	}
	t.start();
	}


	public void run() {

		// Accepting requests and replying
		try {

		while(!s.isInputShutdown()) {
			System.out.println("In thread");
			String request = is.readLine();
			/*
				Multiplex according to the request;
			*/

			if(request == "refresh") tracker.refresh(os);
			else if(request == "disconnect") break;
			else throw new Exception("Undefined request");
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



class server {

	final static int TRACKER_PORT = 5000;
	public static void main(String args[]) {

		try (ServerSocket s = new ServerSocket(TRACKER_PORT)) {
		System.out.println("The tracker server is running on port " + TRACKER_PORT);

		/*
			Accepting connection to take info
				Connect with every new user and save its information in a global object (Registration)
				Make seperate thread for each client
				Handle and exchange messages for success or faliure

				Timely ping the all clients to check if someone has died
				Make a Request object to manage a any sort of request from the corresponding client (Online User List, check if die loist,etc)
								
		*/

		/**/
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
}