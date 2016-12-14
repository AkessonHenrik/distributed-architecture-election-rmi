import java.rmi.RemoteException;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class Main {
    public static void main(String... args) throws RemoteException {

        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();

        n1.startServer();
        n2.startServer();
        n3.startServer();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        n1.startClient();
        n2.startClient();
        n3.startClient();
    }
}