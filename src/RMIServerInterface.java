import java.rmi.*;

public interface RMIServerInterface extends Remote {
    String getMessage(int clientId) throws RemoteException;
}
