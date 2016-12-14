import java.rmi.*;

public interface RMIServerInterface extends Remote {
    String getMessage(int clientId) throws RemoteException;

    void elect(int id, int apt) throws RemoteException;
}
