import java.net.MalformedURLException;
import java.rmi.*;

/**
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class Main {
    public static void main(String... args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException {

        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();

        // Let time for Servers to start correctly
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        n1.start();
        n2.start();
        n3.start();

    }
}