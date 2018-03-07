



import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import INTF.*;

/*
	The thread which is responsible for downloading
	a file from an offset and writing it to file
*/
class Downloader implements Runnable {

	Thread t;
	String fileName;
	int offset,CHUNK;
	URLConnection uc;
	/*
		Param -
		fileName - Name of the file stored in HDD
		pos - Offset from which the file downloads and is written
		CHUNK - Chunk size of each Download
		uc - URL connection Object to set up connection
	*/
	public Downloader(String fileName,int offset,int CHUNK,URLConnection uc) {
		this.t = new Thread(this);
		this.fileName =  fileName;
		this.offset = offset;
		this.uc = uc;
		this.CHUNK = CHUNK;
		this.t.start();
	}

	public void run() {

		try (RandomAccessFile f = new RandomAccessFile(fileName,"rw")) {
			uc.setRequestProperty("Range","bytes=" + offset + "-");
			System.out.println("offset = " + offset);
			/*Seeking to that position in the file*/
			f.seek(offset);
			
			InputStream is = uc.getInputStream();
			//BufferedInputStream is = new BufferedInputStream(inp,CHUNK);
			int left  = CHUNK,ind = 0;
			byte buff[] = new byte[CHUNK];
			int rd,dwn = 0;
			do {
				rd = is.read(buff,ind,left);
				if(rd != -1 && left > 0){
				dwn += rd;
				ind += rd;
				left -= rd;
			}
			}
			while(rd != -1 && left > 0);
			f.write(buff,0,dwn);
			System.out.println("Downloaded " + dwn);
		}
		catch (Exception e){
			System.out.println(e);
		}	
	}
	
}

/*
	The Client thread which manages the
	Download of a particular file
*/
class ClientA implements Runnable,Client {
	Thread t;
	final int TCOUNT = 4;
	final int TRACKER_PORT = 5000;
	final String TRACKER_IP = "127.0.0.1";
	final int NODE_CHUNK = 1000000;
	Socket tracker;
	URL url;
	int size;
	String fileName;
	BufferedReader is;
	PrintWriter os;
	HashSet<String> ipList;
	public ClientA(String url) {
		t = new Thread(this);
		try {
			System.out.println("Constructor");
			tracker = new Socket(TRACKER_IP,TRACKER_PORT);
			is = new BufferedReader(new InputStreamReader(tracker.getInputStream()));
			os = new PrintWriter(tracker.getOutputStream(),true);
			ipList = new HashSet<String>();
			this.url = new URL(url);
			URLConnection connection = this.url.openConnection();
			size = connection.getContentLength();
			System.out.println("Sizeof file = "+ size);
			fileName = getFileName(url);
			t.start();
		}

		catch(MalformedURLException e) {
			System.out.println(e);
		}
		catch(IOException ex) {
			System.out.println(ex);
		}
	}
	/*
		The function gets the list of
		Online peers from the Tracker
	*/
	public boolean checkPeers() {

		System.out.println("Checking peer availability");
		System.out.println("Connected to "+tracker);
		try {
			/*Requesting the clients*/
			os.println("refresh");
			while(true) {
				String ip = is.readLine();
				System.out.println("read "+ip);
				if(ip.equals(".")) break;
				else if(!ipList.contains(ip))
					ipList.add(ip);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
		finally {
			System.out.println("iplist size = " + ipList.size());
		if(ipList.size() > 0) return true; // Should be greater than 1
		return false;
		}
	}
	// Disconnect this machine from Tracker
	public void disconnect() {
		os.println("delete");
		os.println("disconnect");
	}

	public void run() {


	/*
		Set up all the parameters
			Check for avilable peers
				Dispatch the files using (Dispatch) object
				Contact the peers with file in new object (Handler) object for each peer
				Manage all file parts that are successfully downloaded in Handler
				For broken files manage downloading those broken parts in other object (ManageBroken)
			If No peer, Download as it is in Download function
	*/	
		System.out.println("Running in thread");	
		try {
			if(!checkPeers()){
				System.out.println("No peer found, Downloading on local machine");
				download();
				disconnect();
				return;
			}	

			Dispatch dId = new Dispatch(ipList,url,size,fileName);
			dId.Distribute();
			System.out.println("Distributed");
		}
		finally {
			try {
				tracker.close();
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}

	}


	/*
		The function downloads the file
		on local machine
	*/
	public void download() {

		System.out.println("Opening Connection and begining download");
		Downloader d[] = new Downloader[TCOUNT];
		int chunkSize = this.size/TCOUNT + 1;
		int offset = 0,ind = 0;
		/*
			Downloading the file parallely on local machine
			on TCOUNT number of threads
		*/
		while(offset < size) {
			try {
			d[ind++] = new Downloader(this.fileName,offset,chunkSize,this.url.openConnection());
			offset += chunkSize;
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		/* Waiting for downloading threads to finish*/
		try {
			
			for(int i=0;i<ind;i++)
				d[i].t.join();	
		}
		catch (InterruptedException e) {
			System.out.println(e);
		}
		
	}
	/*
		ToDo :)
	*/
	public void getStatus(){
		System.out.println("status");		
	}
	/*
		ToDo :)
	*/
	public String getFileName(String url) {
		System.out.println("get name");
		return new String("newfile");

	}
}