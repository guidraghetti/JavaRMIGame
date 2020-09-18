import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoInterface extends Remote {
    public int register(int playerId, String playerIp) throws RemoteException;
    public int play(int playerId) throws RemoteException;
    public int quitGame(int playerId) throws RemoteException;
   }