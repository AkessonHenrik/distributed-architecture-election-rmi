import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
        System.out.println("This id is " + id);
        System.out.println("Current number of Nodes: " + numberOfNodes);
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getId() {
        return this.id;
    }

    void elect(int id, int apt) throws RemoteException, InterruptedException {

        int electedNode, electedNodeAptitude;

        System.out.println("Elect in node " + this.id);

        // Check if this node has been elected by all other nodes, full circle
        if (this.id == id) {
            electedNode = -1;
            electedNodeAptitude = -1;
            System.out.println("Full circle, node " + this.id + " has been elected");
        } else {
            if (this.apt > apt) {
                System.out.println("NODE " + this.id + ":This apt = " + this.apt + " is > " + apt + " of node " + id);
                System.out.println("Node " + this.id + " is elected");
                electedNode = this.id;
                electedNodeAptitude = this.apt;
                // This node has a higher aptitude

                // Transmit to next node

                this.apt = 0;

            } else if (this.apt == apt) {
                System.out.println("NODE " + this.id + ": This apt = " + this.apt + " is == " + apt + " of node " + id);
                // Equal aptitudes, choice will be made according to id difference
                if (this.id > id) {
                    // this node is elected
                    System.out.println("Node " + this.id + " is elected");

                    // Transmit to next node
                    electedNode = this.id;
                    electedNodeAptitude = this.apt;

                    this.apt = 0;
                } else {
                    // caller node is elected
                    System.out.println("Node " + id + " is elected");
                    electedNode = id;
                    electedNodeAptitude = apt;

                    // Transmit to next node

                    this.apt++;
                }

            } else { // this.apt < apt
                System.out.println("NODE " + this.id + ":This apt = " + this.apt + " is < " + apt + " of node " + id);
                // Caller node has a higher aptitude
                System.out.println("Node " + id + " is elected");
                electedNode = id;
                electedNodeAptitude = apt;

                // Transmit to next node

                this.apt++;
            }
            Thread.sleep(1000);
            this.rmiClient.transmit(electedNode, electedNodeAptitude);
        }
    }

    void startServer() {
        new Thread(() -> {
            try {
                rmiServer.start();
            } catch (RemoteException | MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void startClient() {
        new Thread(() -> {
            try {
                rmiClient.initialize();
//                rmiClient.start();
            } catch (RemoteException | NotBoundException | MalformedURLException /*| InterruptedException*/ e) {
                e.printStackTrace();
            }
        }).start();
    }

    int getApt() {
        return apt;
    }
}
