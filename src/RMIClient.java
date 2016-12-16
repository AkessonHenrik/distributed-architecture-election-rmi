import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getId() + " transmits");
        rmiServer.elect(electedNode, electedNodeAptitude);
    }

    void result(int electedNode) {

    }

    void initialize() throws RemoteException, NotBoundException, MalformedURLException {
        this.rmiServer = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((parent.getId() + 1) % parent.getNumberOfNodes()));
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getId() +  ": This RMI Server is " + ((parent.getId() + 1) % parent.getNumberOfNodes()) );
    }
}