import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;
import java.net.UnknownHostException;



/*Falta terminar de implementar os métodos e chamar o client */
/*Descobrir como fazer Two way communication com RMI, pois o exemplo passado só comunica do cliente para o servidor */

public class Server extends UnicastRemoteObject implements JogoInterface {

    class Player {
        public int id;
        public int plays;
        public String playerIp;

        public Player(int id, int plays, String playerIp) {
            this.id = id;
            this.plays = plays;
            this.playerIp = playerIp;
        }

        public int getId() {
            return this.id;
        }

        public int getPlays() {
            return this.plays;
        }

        public String getIp() {
            return this.playerIp;
        }
    }

    private static volatile String remoteHostName;
    private static volatile List<Player> playersList = new ArrayList<Player>();
    public static volatile int maxPlayers;
    public static volatile int maxPlays;
    private static Random playerId = new Random();

    public Server() throws RemoteException {

    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        if (args.length != 3) {
            System.out.println("Para executar digite:");
            System.out.println("java Server <ip do servidor> <nº de jogadores> <nº de jogadas>");
            System.exit(1);

        }

        maxPlayers = Integer.parseInt(args[1]);
        maxPlays = Integer.parseInt(args[2]);

        try {
            System.setProperty("java.rmi.server.hostname", args[0]);
            LocateRegistry.createRegistry(3000);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            String server = "rmi://" + args[0] + ":3000/server_if";
            Naming.rebind(server, new Server());
            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.out.println("Serverfailed: " + e);
        }
        
        while(playersList.size() < maxPlayers)
        {
        	Thread.sleep(500);
        }
        
        run();
        
    }

    public int register(String playerIp) {
    	int id = playerId.nextInt();
        Player player = new Player(id, 0, playerIp);
        playersList.add(player);
        System.out.println("Jogador " + id + " adicionado");            
        return id;       
    }

    public static void run() {
        for (Player player : playersList) {
            
            remoteHostName = player.playerIp;
            System.out.println(player.playerIp);
            String connectLocation = "rmi://" + remoteHostName + ":3001/client_if";
            JogadorInterface client_if = null;
            System.out.println("Iniciando jogo jogador " + player.id);
            try {
                client_if = (JogadorInterface) Naming.lookup(connectLocation);
                client_if.initializeGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        

        System.out.println("saiu");
        try {
            wakeUpPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

    public int play(int playerId) {

        Iterator<Player> iterator = playersList.iterator();
        while(iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getId() == playerId) {
                p.plays++;
                if (p.plays >= maxPlays) {
                    remoteHostName = p.playerIp;
                    String connectLocation = "rmi://" + remoteHostName + ":3001/client_if";
                    JogadorInterface client_if = null;
                    try {
                        client_if = (JogadorInterface) Naming.lookup(connectLocation);
                        client_if.finalizeGame();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return playerId;
    }

    public int quitGame(int playerId) {
        Iterator<Player> iterator = playersList.iterator();
        while(iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getId() == playerId) {
                iterator.remove();
            }
        }

        System.out.println("Jogador " + playerId + " desconectado");
        return 0;
    }

    public static void wakeUpPlayer() throws InterruptedException {        
        while(!playersList.isEmpty()) {            
            TimeUnit.SECONDS.sleep(3);
            for (Player player : playersList) {
                remoteHostName = player.playerIp;
                String connectLocation = "rmi://" + remoteHostName + ":3001/client_if";
                JogadorInterface client_if = null;
                try {
                    client_if = (JogadorInterface) Naming.lookup(connectLocation);
                    client_if.sendHello();
                    System.out.println("id running: " + player.id);
                } catch (Exception e) {
                    System.out.println("Verify client failed: ");
                    e.printStackTrace();
                }
            }
        }
    }

}