import java.net.MalformedURLException;
import java.rmi.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class Main {
    public static void main(String... args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {
        int numberOfElectionProcesses = Integer.parseInt(args[0]);

        Node n1 = new Node(numberOfElectionProcesses);
        Node n2 = new Node(numberOfElectionProcesses);
        Node n3 = new Node(numberOfElectionProcesses);

        n1.start();
        n2.start();
        n3.start();
    }
}