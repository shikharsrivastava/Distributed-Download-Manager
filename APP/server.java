
import java.util.*;
import java.io.*;
import java.lang.*;
import java.net.*;


/*
	This object Handles the Download for
	connected peer and diverts the traffic
*/
class PeerHandler implements Runnable {

	Thread t;
	Socket s;
	BufferedReader is;
	BufferedOutputStream os;
	int totalRead,size,offset;
	String url;
	boolean success;
	final int buffSize = 1000;
	public PeerHandler(Socket cl) {
		t = new Thread(this);
		s = cl;
		totalRead = 0;
		success = false;
		try {

			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new BufferedOutputStream(s.getOutputStream());
			t.start();	
		}
		catch (IOException e) {
			System.out.println(e);
			try {
				s.close();
			}
			catch (IOException ex) {
				System.out.println(ex);
			}
		}
		
	}

	public void run() {
		// Getting info
		System.out.println("getting info from peer");
		try {
			url = is.readLine();
			offset = Integer.parseInt(is.readLine());
			size = Integer.parseInt(is.readLine());

			/*Starting download*/
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			/*Setting desired location*/
			uc.setRequestProperty("Range","bytes=" + offset + "-");
			/*Starting download*/
			InputStream dis = uc.getInputStream();
			byte buff[] = new byte[buffSize];

			int left = size,rd = 0;
			do {
				if(left <= buffSize) {
					System.out.println("Downloading " + left +" bytes in 1st");
					rd = dis.read(buff,0,left);
				}
				else {
					System.out.println("Downloading " + buffSize+ " bytes in 2nd");
					rd = dis.read(buff,0,buffSize);
				}
				if(rd != -1) {
					System.out.println("Downloaded " + rd + " bytes");
					os.write(buff,0,rd);
					os.flush();
					totalRead += rd;
				}
			}
			while(rd != -1);

			if(totalRead == size)
				success = true;
			else
				success = false;
		}
		catch(IOException e) {
			System.out.println(e);
		}
		finally {
			try {
				s.close();
				System.out.println("closing the socket");
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
	}
}
/*
	This object runs the Main server to
	provide resource sharing to peers
*/

class Server implements Runnable{
	// The port on which service runs
	final int PEER_SERVER_PORT = 6010;
	// Tracker port
	final int TRACKER_PORT = 5000;
	// Tracker IP address
	final String TRACKER_IP = "127.0.0.1";
	ServerSocket s;
	Socket tracker;
	// Stream for Tracker input
	BufferedReader is;
	// Stream for Tracket output
	PrintWriter os;
	Thread t;
	public Server(){

		t = new Thread(this);
		try {
			s = new ServerSocket(PEER_SERVER_PORT);
			/* Registering the service on tracker*/
			tracker = new Socket(TRACKER_IP,TRACKER_PORT);
			is = new BufferedReader(new InputStreamReader(tracker.getInputStream()));
			os = new PrintWriter(tracker.getOutputStream(),true);
			os.println("Server");
			t.start();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	public void run(){
		System.out.println("The peer server is running on port "+ PEER_SERVER_PORT);
		while(true) {
			try {
				Socket cl = s.accept();
				new PeerHandler(cl);	
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}			
	}
}