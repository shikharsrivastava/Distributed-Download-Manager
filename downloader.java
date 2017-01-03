//23:29

import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import INTF.Client;

/*
	The thread which is responsible for downloading
	a file from an offset and writing it to file

	Constructor Param -
	fileName - Name of the file stored in HDD
	pos - Offset from which the file downloads and is written
	uc - URL connection Object to set up connection
*/
class Downloader implements Runnable {

	Thread t;
	String fileName;
	int offset,CHUNK;
	URLConnection uc;
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


class ClientA implements Runnable,Client {
	Thread t;
	final int TCOUNT = 4;
	URL url;
	int size;
	String fileName;	
	public ClientA(String url) {
		t = new Thread(this);
		try {
			this.url = new URL(url);
			URLConnection connection = this.url.openConnection();
			this.size = connection.getContentLength();
			this.fileName = getFileName(url);
			t.start();
		}
		catch(MalformedURLException e) {
			System.out.println("URL is malformed");
		}
		catch(IOException e) {
			System.out.println("cannot open connection or connect");
		}
	}

	public boolean checkPeers() {

		System.out.println("Checking peer availability");
		return false;
	}


	public void run() {
	/*
		Set up all the parameters
			Check for avilable peers
				Dispatch the files using (Dispatcher) object
				Contact the peers with file in new object (Assembler) object for each peer
				Manage all file parts that are successfully downloaded in assembler
				For broken files manage downloading those broken parts in other object (ManageBroken)
			If No peer, Download as it is in Download function
	*/	

		if(!checkPeers()){
			download();
			return;
		}

	}



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
	public void getStatus(){
		System.out.println("status");		
	}

	public String getFileName(String url) {
		System.out.println("get name");
		return new String("newfile");

	}
}