import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.*;

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

    void announce(int electedNode, int electedNodeAptitude) throws RemoteException, InterruptedException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getNodeId() + " transmits");
        rmiServer.elect(electedNode, electedNodeAptitude);
    }



    void result(int electedNode) throws RemoteException {
        this.rmiServer.result(electedNode);
    }

    void initialize() throws RemoteException, NotBoundException, MalformedURLException {
        this.rmiServer = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((parent.getNodeId() + 1) % parent.getNumberOfNodes()));
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getNodeId() +  ": This RMI Server is " + ((parent.getNodeId() + 1) % parent.getNumberOfNodes()) );
    }

    void start() throws RemoteException, InterruptedException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getNodeId() + " starts election process");
        this.rmiServer.elect(parent.getNodeId(), parent.getAptitude());
    }
}