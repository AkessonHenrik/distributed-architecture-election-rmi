import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Node {
    private int numberOfNodes = 0;
    private final int id;
    private int apt;
    private RMIClient rmiClient;
    private RMIServer rmiServer;

    Node(int id, int numberOfNodes) throws RemoteException, MalformedURLException, NotBoundException {
        this.id = id;
        this.numberOfNodes = numberOfNodes;
        this.rmiServer = new RMIServer(id, this);
        this.rmiClient = new RMIClient(this);
    }

    int getNumberOfNodes() {
        return numberOfNodes;
    }

    int getId() {
        return this.id;
    }

    void elect(int id, int apt) throws RemoteException, InterruptedException {

        int electedNode, electedNodeAptitude;

        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Elect in node " + this.id);
        // Check if this node has been elected by all other nodes, full circle
        if (this.id == id) {
            rmiClient.result(this.id);
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Full circle, node " + this.id + " has been elected");
        } else {
            if (this.apt > apt || this.apt == apt && this.id > id) {
                // This node has a higher aptitude
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "Node " + this.id + " is elected");
                electedNode = this.id;
                electedNodeAptitude = this.apt;
                this.apt = 0;
            } else {
                // Caller node has a higher aptitude
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, id + " is elected");
                electedNode = id;
                electedNodeAptitude = apt;
                this.apt++;
            }
            Thread.sleep(1000);
            this.rmiClient.transmit(electedNode, electedNodeAptitude);
        }
    }

    void startClient() {
        new Thread(() -> {
            try {
                rmiClient.initialize();
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
