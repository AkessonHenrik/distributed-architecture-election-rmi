import java.rmi.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public interface RMIServerInterface extends Remote {
    void elect(int id, int apt) throws RemoteException, InterruptedException;
    void result(int electedNodeId) throws RemoteException;
}
