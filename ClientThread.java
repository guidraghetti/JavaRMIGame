import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

/*Aqui falta arrumar pra chamar só o método que é passado */
public class ClientThread extends Thread {
	protected String[] thread_args;
	
	public ClientThread(String[] args) {
		thread_args = args;
	}

	public void run() {
		int result = 0;

		String remoteHostName = thread_args[0];
		String connectLocation = "rmi://" + remoteHostName + ":8000/server_if";

		JogoInterface server_if = null;
		try {
			System.out.println("Connecting to server at : " + connectLocation);
			server_if = (JogoInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println ("AdditionClient failed: ");
			e.printStackTrace();
		}

		// try {
			
		// 	result = server_if.register(thread_args[2], args[1]);
		// 	System.out.println("register() successful, id: " + result);
		// } catch (RemoteException e) {
		// 	e.printStackTrace();
		// }


		// List<Integer> ids = new ArrayList<>();
		// try {
		// 	ids = server_if.list_query(result);
		// } catch (RemoteException e) {
		// 	e.printStackTrace();
		// }
			
		// for (int i = 0; i < ids.size(); i++)
		// 	System.out.println("user id: " + ids.get(i));

		// try {
		// 	String nick = server_if.id_query(result, ids.get(0));
		// 	System.out.println("First: " + nick);
		// } catch (RemoteException e) {
		// 	e.printStackTrace();
		// }
	}
}
