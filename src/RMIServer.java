import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.logging.*;

/**
 * RMIServer exposes methods that clients can call through RMI.
 * intercepts calls and forwards them to the parent Node
 *
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    private Node parent;

    /**
     * RMI Host address
     */
    private final String host = "localhost";

    /**
     * RMIServer constructor
     *
     * @param parent parent node
     * @throws RemoteException
     * @throws MalformedURLException
     */
    RMIServer(Node parent) throws RemoteException, MalformedURLException {

        this.parent = parent;
        int id = parent.getNodeId();
        try {
            // /special exception handler for registry creation
            LocateRegistry.createRegistry(1099 + id);
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        // Bind this object instance to the name "RmiServer"
        Naming.rebind(this.host + "/RMIServer" + id, this);
        System.out.println("PeerServer bound in registry " + "localhost/RMIServer" + id);
    }

    /**
     * Exposed method for RMI election
     *
     * @param id  forwarded elected node
     * @param apt forwarded elected node aptitude
     * @throws RemoteException
     * @throws InterruptedException
     */
    @Override
    public void elect(int id, int apt) throws RemoteException, InterruptedException {
        // Signal to parent that an election announcement has been made
        parent.elect(id, apt);
    }

    /**
     * Exposed method for result announcing
     *
     * @param electedNodeId Result Node
     * @throws RemoteException
     */
    @Override
    public void result(int electedNodeId) throws RemoteException {
        // Signal to parent that a result announcement has been made
        this.parent.result(electedNodeId);
    }
}