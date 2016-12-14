import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class Node {
    private static int numberOfNodes = 0;
    private int id;

    Node() {
        this.id = numberOfNodes++;
        System.out.println("This id is " + id);
    }

    void startServer() {
        new Thread(() -> {
            try {
                new RMIServer(id).start();
            } catch (RemoteException | MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void startClient() {
        new Thread(() -> {
            try {
                new RMIClient(id).start();
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
