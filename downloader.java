//23:29

import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;

/*
	The thread which is responsible for downloading
	a file from an offset and writing it to file

	Constructor Param -
	fileName - Name of the file stored in HDD
	pos - Offset from which the file downloads and is written
	uc - URL connection Object to set up connection
*/
class Downloader implements Runnable {

	final int CHUNK = 401816;
	Thread t;
	String fileName;
	int pos;
	URLConnection uc;
	public Downloader(String fileName,int pos,URLConnection uc) {
		this.t = new Thread(this);
		this.fileName =  fileName;
		this.pos = pos;
		this.uc = uc;
		this.t.start();
	}

	public void run() {

		try (RandomAccessFile f = new RandomAccessFile(fileName,"rw")) {
			uc.setRequestProperty("Range","bytes=" + pos + "-");
			System.out.println("pos = " + pos);
			f.seek(pos);
			
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
	The Downloader class which makes
*/

class App {

	public static void main(String args[]) throws Exception {

		final int CHUNK = 401816;
		URL url = new URL(args[0]);
		final String fileName = "Syllabus.pdf";

		
		URLConnection connection = url.openConnection();
		
		final long size = connection.getContentLength();
		System.out.println(size);
		Downloader tarr[] = new Downloader[1000];
		int ind = 0, pos = 0;
		 while(pos < size)
		{
			Downloader t = new Downloader(fileName,pos,url.openConnection());
			tarr[ind++] = t;
			pos += CHUNK;
		}
		
		for(int i=0;i<ind;i++)
			tarr[i].t.join();

	}
}