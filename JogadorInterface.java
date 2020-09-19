package src;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface JogadorInterface extends Remote {
    public void initializeGame() throws RemoteException, InterruptedException;
    public void finalizeGame() throws RemoteException;
    public void sendHello() throws RemoteException;
   }
