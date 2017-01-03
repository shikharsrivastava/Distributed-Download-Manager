import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;



class MainApp {

	public static void main(String args[]) throws Exception {

		String url = args[0];
		ClientA c = new ClientA(url);
		c.t.join();
	}
}