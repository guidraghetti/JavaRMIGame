import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

/*Conexão com server pronta. FALTA MUITA COISA AQUI */
/*Falta implementar os métodos do cliente e descobrir como usar a thread para chamar um método especifico */ 
public class Client implements JogadorInterface {
    private static String player;
    private static String [] clientArgs;
    private static boolean condition = true;
    public Client() throws RemoteException {

    }

	public static void main(String[] args) {
        
		if (args.length != 3) {
			System.out.println("Para executar digite:");
            System.out.println("java Server <server ip> <your ip> <seu nº de id>");
			System.exit(1);
        }
        clientArgs = args;
        player = args[2];
		try {
			System.setProperty("java.rmi.server.hostname", args[1]);
			LocateRegistry.createRegistry(8000);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		 new ClientThread(args, 0).start();
    }
    public void initializeGame() throws InterruptedException {
        int r = (int) (Math.random() * (1500 - 500)) + 500;
        System.out.println("Jogo Iniciado");
        while (condition) {
            TimeUnit.MILLISECONDS.sleep(r);
            new ClientThread(clientArgs, 1).start();  
        }
    }
    public void finalizeGame() {
        condition = false;
        System.out.println("Finalizando jogo");
        new ClientThread(clientArgs, 2).start();

    }
    public String sendHello() {
        String hello = "Jogador " + player + ": I'm Alive! Thank for care about me"; 
        return hello;
    }

}
