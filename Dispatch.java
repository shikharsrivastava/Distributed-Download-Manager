import java.net.*;
import java.util.*;
import java.io.*;
import INTF.*;


class HandlePeer implements Runnable {
	final int DISPATCH_PORT = 6000;
	String ip;
	URL url;
	String fileName;
	int offset,size,totalRead;
	Thread t;
	Socket s;
	RandomAccessFile f;
	BufferedInputStream is;
	boolean success;
	PrintWriter os;
	public HandlePeer(String ip,URL url,String fileName,int offset,int size) {
		this.t = new Thread(this);
		this.ip = ip;
		this.url = url;
		this.success = false;
		this.fileName = fileName;
		this.offset = offset;
		this.size = size;
		this.totalRead = 0;
		t.start();
	}

	public void run() {

		try {
			s = new Socket(ip,DISPATCH_PORT);
			f = new RandomAccessFile(fileName,"rw");	
		
			is = new BufferedInputStream(s.getInputStream());
			os = new PrintWriter(s.getOutputStream(),true);
			/*
				Giving it the information
			*/
			System.out.println("offset = " + offset + "size = "+size);
			os.println(url.toExternalForm());
			os.println(offset);
			os.println(size);

			/*Taking input from socket*/
			f.seek(offset);
			
			int rd = 0, left = size, buffSize = 1000;
			byte buff[] = new byte[buffSize]; 

			do {

				if(left >= buffSize) {
					rd = is.read(buff,0,buffSize);
				}
				else {
					rd = is.read(buff,0,left);
				}
				if(rd != -1)
					{
						f.write(buff,0,rd);
						totalRead += rd;
					}
			}
			while(rd != -1);
			if(totalRead == size)
				success = true;
			else
				success = false;

		}
		catch (Exception e) {
			System.out.println(e);
		}

		finally {
			if(!success)
				System.out.println("peer failed");
			else
				System.out.println("Peer success");
			try {
				s.close();
				f.close();
			}
			catch(IOException e){
				System.out.println(e);
			}
		}
	}
}

class Dispatch implements Dispatcher {

	final int MAX_DISPATCH = 10;
	URL url;
	int size,NODE_CHUNK;
	String fileName;
	HashSet<String> ipList;
	public Dispatch(HashSet<String> ipList,URL url,int size,String fileName) {
		System.out.println("In Dispatch constructor");
		this.ipList = ipList;
		this.url = url;
		this.size = size;
		this.fileName = fileName;
	}

	public boolean Distribute() {
		System.out.println("Distributing");

		int count = 0;
		int offset = 0;
		int chunk = (size / ipList.size()) + 1;
		System.out.println("size = " + size + " offset = "+ offset + " chunk = " + chunk);
		HandlePeer peers[] = new HandlePeer[MAX_DISPATCH];
		for (String ip: ipList) {
			System.out.println("Dispatching to " + ip + " offset = "+offset);
			if(size - offset < chunk){
				peers[count++] = new HandlePeer(ip,url,fileName,offset,size-offset);
			}
			else{
				peers[count++] = new HandlePeer(ip,url,fileName,offset,chunk);
			}
			offset += chunk;
		}
		try {
			for(int i=0;i<count;i++) {
				peers[i].t.join();
			}
		}
		catch(InterruptedException e) {
			System.out.println(e);
		}
		return true;

	}


}