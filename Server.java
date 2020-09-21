import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server extends UnicastRemoteObject implements JogoInterface {

    class Player {
        private int id;
        public int plays;
        private String playerIp;

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

    private static volatile List<Player> playersList = new ArrayList<Player>();
    private static volatile int maxPlayers;
    private static volatile int maxPlays;
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
            System.out.println("Registro RMI criado!");
        } catch (RemoteException e) {
            System.out.println("Registro RMI já exitste!");
        }

        try {
            String server = "rmi://" + args[0] + ":3000/server_if";
            Naming.rebind(server, new Server());
            System.out.println("Servidor rodando!");
        } catch (Exception e) {
            System.out.println("Falha ao instanciar servidor: " + e);
        }

        while (playersList.size() < maxPlayers) {
            Thread.sleep(500);
        }

        run();

    }

    public int register(String playerIp) {
        if (playersList.size() <= maxPlayers) {
            int id = playerId.nextInt();
            Player player = new Player(id, 0, playerIp);
            playersList.add(player);
            System.out.println("Jogador " + id + " adicionado");
            return id;
        }
        return -1;
    }

    public static void run() {
        for (Player player : playersList) {

            String connectLocation = "rmi://" + player.getIp() + ":3001/client_if";
            JogadorInterface client_if = null;
            System.out.println("Iniciando jogo do jogador " + player.getId());
            try {
                client_if = (JogadorInterface) Naming.lookup(connectLocation);
                client_if.initializeGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            wakeUpPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int play(int playerId) {
        Iterator<Player> iterator = playersList.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getId() == playerId) {
                p.plays++;
                System.out.println("Jogador " + p.getId() + " jogou!");
                if (p.plays >= maxPlays) {
                    String connectLocation = "rmi://" + p.getIp() + ":3001/client_if";
                    JogadorInterface client_if = null;
                    try {
                        client_if = (JogadorInterface) Naming.lookup(connectLocation);
                        client_if.finalizeGame();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int probability = (int) (Math.random() * (100 - 1)) + 1;
                if (probability == 1) {
                    System.out.println("Jogador " + p.getId() + " caiu no 1%");
                    String connectLocation = "rmi://" + p.getIp() + ":3001/client_if";
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
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getId() == playerId) {
                iterator.remove();
            }
        }
        System.out.println("Jogador " + playerId + " desconectado");
        return 0;
    }

    public static void wakeUpPlayer() throws InterruptedException {
        while (!playersList.isEmpty()) {
            TimeUnit.SECONDS.sleep(3);
            for (Player player : playersList) {
                String connectLocation = "rmi://" + player.getIp() + ":3001/client_if";
                JogadorInterface client_if = null;
                try {
                    client_if = (JogadorInterface) Naming.lookup(connectLocation);
                    client_if.sendHello();
                    System.out.println("Jogador " + player.getId() + " está online!");
                } catch (Exception e) {
                    System.out.println("Falha ao verificar o jogador " + player.getId());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Jogo encerrado!");
        System.out.println("Desligando servidor ...");
        System.exit(1);
    }

}