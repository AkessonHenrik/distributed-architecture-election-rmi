import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class Node {
    private static int numberOfNodes = 0;
    private final int id;
    private int apt;
    private RMIClient rmiClient;
    private RMIServer rmiServer;

    Node() throws RemoteException {
        this.id = numberOfNodes++;
        this.rmiServer = new RMIServer(this.id, this);
        this.rmiClient = new RMIClient(this);
        System.out.println("This id is " + id);
        System.out.println("Current number of Nodes: " + numberOfNodes);
    }
    public static int getNumberOfNodes() {
        return numberOfNodes;
    }
    public int getId() {
        return this.id;
    }
    void elect(int id, int apt) {
        System.out.println("Elect in node " + this.id);
        if(this.apt > apt) {
            // This node has a higher aptitude

            // Transmit to next node

            this.apt = 0;

        } else if (this.apt == apt) {
            // Equal aptitudes, choice will be made according to id difference
            if(this.id > id) {
                // this node is elected

                // Transmit to next node

                this.apt = 0;
            } else {
                // caller node is elected

                // Transmit to next node

                this.apt++;
            }

        } else { // this.apt < apt
            // Caller node has a higher aptitude

            // Transmit to next node

            this.apt++;
        }
        this.rmiClient.transmit();
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
                rmiClient.start();
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    int getApt() {
        return apt;
    }
}
