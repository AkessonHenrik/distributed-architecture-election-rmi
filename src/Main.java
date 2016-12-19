import java.net.MalformedURLException;
import java.rmi.*;

/**
 * Main class for the project. Runs a simulation with 3 Nodes.
 *
 * Command line arguments:
 *  - Number of election processes to run for each node
 *
 * The program works the following way:
 *
 *  A Node launches an RMIServer and an RMIClient
 *  Regularly (between 0 and 2 seconds), the client starts an Election process
 *      - The number of election processes is given to the Node at instantiation time
 *      - The Client signals to the next Node's RMI Server that it should elect between itself and this Node
 *      - The Server then calls it's Node's elect method, which in turn will send it's result (through a new thread)
 *          to it's client who calls the next node's server's elect method
 *  When a Node sees that the elected Node is itself, it stops the election process and starts the result announcement (through a new Thread)
 *  Each node forwards the result until the elected node receives it's result again, then sets it's announcement flag as FALSE
 *
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class Main {
    public static void main(String... args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
        int numberOfElectionProcesses = Integer.parseInt(args[0]);
        System.out.println("MAIN NUMBER OF ELECTION PROCESSES = " + numberOfElectionProcesses);
        // Create Nodes, which start the RMI Server
        Node n1 = new Node(numberOfElectionProcesses);
        Node n2 = new Node(numberOfElectionProcesses);
        Node n3 = new Node(numberOfElectionProcesses);

        // Start clients and thus the election processes
        n1.startClient();
        n2.startClient();
        n3.startClient();
    }
}