import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

/*Conexão com server pronta. FALTA MUITA COISA AQUI */
/*Falta implementar os métodos do cliente e descobrir como usar a thread para chamar um método especifico */ 
public class Client implements JogadorInterface {

    public Client() throws RemoteException {

    }

	public static void main(String[] args) {
        int result = 0;
        
		if (args.length != 3) {
			System.out.println("Para executar digite:");
            System.out.println("java Server <server ip> <your ip> <seu nº de id>");
			System.exit(1);
		}
	
		try {
			System.setProperty("java.rmi.server.hostname", args[1]);
			LocateRegistry.createRegistry(8000);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		 new ClientThread(args).start();
    }
    public void initializeGame() {
        while (true) {
            
        }

        }
    }
    public void finalizeGame() {

    }
    
    public void sendHello() {
        return "I'm Alive! Thank for care about me"
    }
}
