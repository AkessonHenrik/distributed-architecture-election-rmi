import java.rmi.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 *         <p>
 *         Main interface implemented by the Used RMIServer
 */
public interface RMIServerInterface extends Remote {

    /**
     * The implementation of this method will contain the election steps, excluding result transmission and handling
     *
     * @param id  node id to run the election algorithm with
     * @param apt node aptitude to run the election algorithm with
     * @throws RemoteException
     * @throws InterruptedException
     */
    void elect(int id, int apt) throws RemoteException, InterruptedException;

    /**
     * The implementation of this method will contain the result processing
     *
     * @param electedNodeId the node that's elected
     * @throws RemoteException
     */
    void result(int electedNodeId) throws RemoteException;
}
