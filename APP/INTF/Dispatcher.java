package INTF;

/*
	The dispatcher is the part that divides the large file among nodes

	functions - 
	getHostList() - returns an array of alive ip address running the server
	Distribute() - Performs actual distribution among the nodes
*/
public interface Dispatcher {

	boolean Distribute();
}