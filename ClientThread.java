import java.rmi.Naming;
import java.rmi.RemoteException;

/*Aqui falta arrumar pra chamar só o método que é passado */
public class ClientThread extends Thread {
	protected String[] thread_args;
	private int method;
	public ClientThread(String[] args, int method) {
		thread_args = args;
		this.method = method;
	}

	public void run() {

		String remoteHostName = thread_args[0];
		String connectLocation = "rmi://" + remoteHostName + ":3000/server_if";

		JogoInterface server_if = null;
		try {
			System.out.println("Connecting to server at : " + connectLocation);
			server_if = (JogoInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println ("AdditionClient failed: ");
			e.printStackTrace();
		}

		
		switch (method) {
			case 0:
				try {
					int result = server_if.register(Integer.parseInt(thread_args[2]), thread_args[1]);
					System.out.println("register() successful, id: " + result);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					server_if.play(Integer.parseInt(thread_args[2]));
					System.out.println("Jogador " + thread_args[2] + " jogou!");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case 3: 
				try {
					server_if.quitGame(Integer.parseInt(thread_args[2]));
					System.out.println("Jogo Encerrado");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
		}
	}
}
