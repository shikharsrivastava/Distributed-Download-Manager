import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;


class Register implements Runnable {

	Thread t;
	private Socket s;
	public ListeningThread(Socket s) {
		t = new Thread(this);
		this.s = s;
		t.start();
	}

	public void run() {
		System.out.println(s);
		return;
	}
}


/*
	Server class is the main thread, which is a service running and accepting connections
	from peers for download.
*/
class Server {

	private static final int SERVER_PORT = 4444;

	public static void main(String args[])throws Exception {
		ServerSocket server = new ServerSocket(SERVER_PORT);
		PrintWriter p = new PrintWriter(System.out,true);
		p.println("The server is listening on " + server);

		// Accepting connections
		while(true) {
			Socket s = server.accept();
			/*
				Starts the P2P connection between this machine and the client.
				and registers the client.
			*/
			new Register(s);
		}

	}
	
}