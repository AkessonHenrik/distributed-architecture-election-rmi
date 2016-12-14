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

    RMIClient(Node parent) {
        this.parent = parent;
    }

    void transmit() {
        System.out.println("Client " + parent.getId() + " should transmit");
    }

    void start() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        RMIServerInterface rmiServer = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((parent.getId() + 1) % parent.getNumberOfNodes()));
        while(true) {
            System.out.println("Client " + parent.getId() + " is gonna call elect");
            rmiServer.elect(parent.getId(), parent.getApt());
            Thread.sleep(2000);
        }
    }
}