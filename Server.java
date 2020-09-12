import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

/*Falta terminar de implementar os métodos e chamar o client */
/*Descobrir como fazer Two way communication com RMI, pois o exemplo passado só comunica do cliente para o servidor */


public class Server extends UnicastRemoteObject implements JogoInterface {
    public class Player {
        public int id;
        public int plays;
        public String playerIp;        
        public Player (int id, int plays, String playerIp) {
            this.id = id;
            this.plays = plays;
            this.playerIP = playerIp;
        }

        public int getId () {
            return this.id;
        }
    }

    private static volatile int result;
    private static volatile String remoteHostName;
    private Random rnd = new Random ();
    private static volatile List<Player> playersList = new ArrayList<>();
    private static int maxPlayers;

    public Server() throws RemoteException {

    }

    public static void main (String [] args) throws RemoteException {
        if (args.length != 2) {
            System.out.println("Para executar digite:");
            System.out.println("java Server <server ip> <nº de jogadores>");
            System.exit(1);

        }
        maxPlayers = args[1];

        try {
			System.setProperty("java.rmi.server.hostname", args[0]);
			LocateRegistry.createRegistry(8000);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }
        
        try {
			String server = "rmi://" + args[0] + ":8000/server_if";
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}
    }

    public int register(int playerId, String playerIp) {
		if (!Arrays.asList(playersList).getId().contains(playerId)) {
            player = new Player();
            player.id = playerId
            player.playerIp = playerIp;
            playersList.add(player);
		    System.out.println("Jogador " + playerId + " adicionado" );
		    return playerId;
        }
        else {
            System.out.println("Jogador já cadastrado! Tente Novamente");
            System.exit(1);
        }
    }
    
    public int play (int playerId) {
        for (Player player : playersList) {
            if (player.getId().equals(playerId)) {
                player.plays ++;
                if (player.plays > 50) {
                    try {

                    } catch() {

                    }
                }
            }
        }
    }

    public int quitGame(int playerId) {

    }
}