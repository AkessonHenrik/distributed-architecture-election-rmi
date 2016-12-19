import java.net.MalformedURLException;
import java.rmi.*;

/**
 * Main class for the project. Runs a simulation with 3 Nodes.
 *
 * Command line arguments:
 *  - Number of election processes to run for each node
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