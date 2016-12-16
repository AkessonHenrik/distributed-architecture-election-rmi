import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
class RMIClient {
    private final Node parent;
    private RMIServerInterface rmiServer;
    RMIClient(Node parent) throws RemoteException, NotBoundException, MalformedURLException {
        this.parent = parent;

    }

    void transmit(int electedNode, int electedNodeAptitude) throws RemoteException, InterruptedException {
        System.out.println("Client " + parent.getId() + " transmits");
        rmiServer.elect(electedNode, electedNodeAptitude);
    }
    void initialize() throws RemoteException, NotBoundException, MalformedURLException {
        this.rmiServer = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((parent.getId() + 1) % parent.getNumberOfNodes()));
        System.out.println("CLIENT " + parent.getId() +  ": This RMI Server is " + ((parent.getId() + 1) % parent.getNumberOfNodes()) );
    }
    void start() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
    }
}