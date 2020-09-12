import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface JogadorInterface extends Remote {
    public void initializeGame() throws RemoteException;
    public void finalizeGame() throws RemoteException;
    public void sendHello() throws RemoteException;
   }