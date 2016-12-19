import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.*;

/**
 * The Node class contains all election logic and handles the RMIServer and RMIClient
 *
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
class Node {
    // Number of active nodes. Static so that all Nodes can know how many there are
    private static int numberOfNodes = 0;
    // Id associated to this Node
    private final int id;
    // Current aptitude of this Node
    private int aptitude;
    // This Node's RMIClient
    private final RMIClient rmiClient;
    // Contains whether or not this Node is announcing
    private boolean announcing;

    Node(int numberOfElectionProcesses) throws RemoteException, MalformedURLException, NotBoundException {
        this.id = numberOfNodes++;
        this.announcing = false;
        new RMIServer(this);
        this.rmiClient = new RMIClient(this, numberOfElectionProcesses);
    }

    int getNumberOfNodes() {
        return numberOfNodes;
    }

    int getNodeId() {
        return this.id;
    }

    int getAptitude() {
        return this.aptitude;
    }

    /**
     * Handles the election choice and forwards the result to the RMIClient.
     * Based on the provided Ring election algorithm
     * @param id Caller Node's forwarded Node id
     * @param apt Caller Node's forwarded Node aptitude
     * @throws RemoteException
     * @throws InterruptedException
     */
    void elect(int id, int apt) throws RemoteException, InterruptedException {

        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Elect in node " + this.id);

        // This node has a higher aptitude
        if (this.aptitude > apt || this.aptitude == apt && this.id > id) {

            if (!this.announcing) {
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Node " + this.id + " is elected");
                this.announcing = true;
                this.aptitude = 0;
                this.rmiClient.announce(this.id, this.aptitude);
            }

        // Check if this node has been elected by all other nodes, full circle
        } else if (this.id == id) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Elect in Node " + this.id + " full circle, sending result");
            rmiClient.result(this.id);
        // Caller node has a higher aptitude
        } else {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, id + " is elected");
            this.announcing = true;
            this.aptitude++;
            this.rmiClient.announce(id, apt);
        }
    }

    /**
     * Result handling
     *
     * @param resultNodeId Forwarded result Node's id
     * @throws RemoteException
     */
    void result(int resultNodeId) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Node " + this.id + " has received result: Node " + resultNodeId + " was elected");
        this.announcing = false;
        if (this.id != resultNodeId) {
            rmiClient.result(resultNodeId);
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Received result, I (Node " + this.id + "), have been elected");
        }
    }

    /**
     * Starts the RMIClient thread
     */
    public void startClient() {
        new Thread(rmiClient).start();
    }
}
