
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements JogadorInterface {

    private static boolean condition = false;
    private static int playerId;
    private static String connectLocation;
    private static JogoInterface server_if = null;
    
    public Client() throws RemoteException {}

	public static void main(String[] args) throws InterruptedException {
		//0= server  1=client
		if (args.length != 2) {
			System.out.println("Para executar digite:");
            System.out.println("java Server <server ip> <your ip>");
			System.exit(1);
        }

		connectLocation = "rmi://" + args[0] + ":3000/server_if";

		try {
			System.setProperty("java.rmi.server.hostname", args[1]);
			LocateRegistry.createRegistry(3001);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
        }
        try {
            String client = "rmi://" + args[1] + ":3001/client_if";
            Naming.rebind(client, new Client());
            System.out.println("Client is ready.");
        } catch (Exception e) {
            System.out.println("ClientFailed: " + e);
        }
		try {
			System.out.println("Connecting to server at : " + connectLocation);
			server_if = (JogoInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println ("AdditionClient failed: ");
			e.printStackTrace();
		}
		try {
			playerId = server_if.register(args[1]);
			System.out.println("register() successful, id: " + playerId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		while(condition == false)
        {
        	Thread.sleep(500);
        }

		play();
    }

	public void initializeGame() {        
		condition = true;
        System.out.println("Jogo Iniciado");
    }

	public static void play() throws InterruptedException {
		int r = (int) (Math.random() * (1500 - 500)) + 500;
		while (condition) {
            TimeUnit.MILLISECONDS.sleep(r);
            try {
        		server_if.play(playerId);
        		System.out.println("Jogador "+ playerId +" jogou!");
        	} catch (RemoteException e) {
        		e.printStackTrace();
        	}
        }
	}

    public void finalizeGame() {
        condition = false;

        System.out.println("Finalizando jogo");

        try {
    		server_if.quitGame(playerId);
    		System.out.println("Jogo Encerrado");
    	} catch (RemoteException e) {
    		e.printStackTrace();
    	}
        
    }
    
    public void sendHello() {}
}