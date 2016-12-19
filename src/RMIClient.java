import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.*;

/**
 * RMIClient is the class that takes care of calling by RMI the Server methods
 *
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
class RMIClient implements Runnable {
    /**
     * "Parent" Node, whose getters are called
     */
    private final Node parent;

    /**
     * Next Node's RMI Server
     */
    private RMIServerInterface rmiServer;

    /**
     * Number of election processes to run
     */
    private int numberOfElectionProcesses;

    /**
     * Only constructor, needs a parent Node for information and a number
     * of election processes to run
     *
     * @param parent
     * @param numberOfElectionProcesses
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    RMIClient(Node parent, int numberOfElectionProcesses) throws RemoteException, NotBoundException, MalformedURLException {
        this.parent = parent;
        this.numberOfElectionProcesses = numberOfElectionProcesses;
    }

    /**
     * Method used to announce an election to the next node
     *
     * @param electedNode
     * @param electedNodeAptitude
     * @throws RemoteException
     * @throws InterruptedException
     */
    void announce(int electedNode, int electedNodeAptitude) throws RemoteException, InterruptedException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Node " + parent.getNodeId() + " transmits");
        rmiServer.elect(electedNode, electedNodeAptitude);
    }

    /**
     * Method used to announce a result to the next node
     *
     * @param electedNode
     * @throws RemoteException
     */
    void result(int electedNode) throws RemoteException {
        this.rmiServer.result(electedNode);
    }

    /**
     * Initialization method, links to the RMI
     *
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    void initialize() throws RemoteException, NotBoundException, MalformedURLException {
        this.rmiServer = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((parent.getNodeId() + 1) % parent.getNumberOfNodes()));
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getNodeId() + ": This RMI Server is " + ((parent.getNodeId() + 1) % parent.getNumberOfNodes()));
    }

    /**
     * Starts an election by calling the RMIServer
     *
     * @throws RemoteException
     * @throws InterruptedException
     */
    void start() throws RemoteException, InterruptedException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, parent.getNodeId() + " starts election process");
        this.rmiServer.elect(parent.getNodeId(), parent.getAptitude());
    }

    /**
     * Main thread execution method.
     * Counts down the number of election processes to run and pauses for an amount of time between 0 and .99*2 seconds
     */
    public void run() {
        try {
            // Initialize the RMIServer
            initialize();
            while (this.numberOfElectionProcesses-- > 0) {
                Thread.sleep((long) (Math.random() * 2000));
                // Start an election
                this.start();
            }
        } catch (InterruptedException | RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}