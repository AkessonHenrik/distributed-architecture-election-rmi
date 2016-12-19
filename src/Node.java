import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.*;

class Node extends Thread {
    private static int numberOfNodes = 0;
    private final int id;
    private int numberOfElectionProcesses;
    private int aptitude;
    private RMIClient rmiClient;
    private boolean announcing;

    Node(int numberOfElectionProcesses) throws RemoteException, MalformedURLException, NotBoundException {
        this.id = numberOfNodes++;
        this.announcing = false;
        new RMIServer(id, this);
        this.rmiClient = new RMIClient(this);
        this.numberOfElectionProcesses = numberOfElectionProcesses;
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

    void elect(int id, int apt) throws RemoteException, InterruptedException {

        int electedNode, electedNodeAptitude;

        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Elect in node " + this.id);

        if (this.aptitude > apt || this.aptitude == apt && this.id > id) {
            // This node has a higher aptitude
            if (!this.announcing) {
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Node " + this.id + " is elected");
                electedNode = this.id;
                electedNodeAptitude = this.aptitude;
                this.announcing = true;
                this.aptitude = 0;
                this.rmiClient.announce(electedNode, electedNodeAptitude);
            }
            // Check if this node has been elected by all other nodes, full circle
        } else if (this.id == id) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Elect in Node " + this.id + " full circle, sending result");
            rmiClient.result(this.id);
        } else {
            // Caller node has a higher aptitude
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, id + " is elected");
            electedNode = id;
            electedNodeAptitude = apt;
            this.announcing = true;
            this.aptitude++;
            this.rmiClient.announce(electedNode, electedNodeAptitude);
        }
    }

    void result(int resultNodeId) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Node " + this.id + " has received result: Node " + resultNodeId + " was elected");
        this.announcing = false;
        if (this.id != resultNodeId) {
            rmiClient.result(resultNodeId);
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Received result, I (Node " + this.id + ")have been elected");
        }
    }

    @Override
    public void run() {
        try {
            rmiClient.initialize();
            while (this.numberOfElectionProcesses-- > 0) {
                Thread.sleep((long) (Math.random() * 2000));
                if (Math.random() < 0.3) {
                    this.rmiClient.start();
                }
            }
        } catch (InterruptedException | RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
