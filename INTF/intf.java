package INTF;
import java.net.*;
import java.util.*;

interface MainApp {

	public void Download(URL url);
	public void getStatus();
}

interface Dispatcher {

	String[] getHostList();
	boolean Distribute();
}

interface Reciever {

	void writeToFile();

}
