import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.logging.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private int id;
    private Node parent;

    RMIServer(int id, Node parent) throws RemoteException, MalformedURLException {
        this.id = id;
        this.parent = parent;
        try {
            // /special exception handler for registry creation
            LocateRegistry.createRegistry(1099 + id);
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.WARNING, "java RMI registry already exists.");
        }

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("localhost/RMIServer" + id, this);
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "PeerServer bound in registry " + "localhost/RMIServer" + id);
    }

    @Override
    public void elect(int id, int apt) throws RemoteException, InterruptedException {
        parent.elect(id, apt);
    }

    @Override
    public void result(int electedNodeId) throws RemoteException {
        this.parent.result(electedNodeId);
    }
}