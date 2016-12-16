import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class Main {
    public static void main(String... args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {

        Node n1 = new Node(0,3);
        Node n2 = new Node(1,3);
        Node n3 = new Node(2,3);

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
        n1.elect(2,0);
    }
}