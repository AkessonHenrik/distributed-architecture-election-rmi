import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private int id;
    private Node parent;

    RMIServer(int id, Node parent) throws RemoteException {
        this.id = id;
        this.parent = parent;
    }

    @Override
    public String getMessage(int clientId) throws RemoteException {
        return "Client " + clientId + " has called Server " + id;
    }

    @Override
    public void elect(int id, int apt) throws RemoteException {
        parent.elect(id, apt);
    }

    void start() throws MalformedURLException, RemoteException {
        System.out.println("RMI server started");

        try {
            // /special exception handler for registry creation
            LocateRegistry.createRegistry(1099 + id);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("localhost/RMIServer" + id, this);
        System.out.println("PeerServer bound in registry");
    }
}