import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private int id;

    RMIServer(int id) throws RemoteException {
        this.id = id;
    }

    @Override
    public String getMessage(int clientId) throws RemoteException {
        return "Client " + clientId + " has called Server " + id;
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