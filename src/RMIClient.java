import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class RMIClient {
    private final int id;

    RMIClient(int id) {
        this.id = id;
    }

    void start() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        RMIServerInterface obj = (RMIServerInterface) Naming.lookup("localhost/RMIServer" + ((id + 1) % 3));
        while(true) {
            System.out.println(obj.getMessage(id));
            Thread.sleep(3000);
        }
    }
}
