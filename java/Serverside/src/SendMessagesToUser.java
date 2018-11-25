import java.net.Socket;

public class SendMessagesToUser implements Runnable{

	
	private Socket socket;
	boolean run = true;
	private User user;
	
	
	public SendMessagesToUser(Socket s, User u) {
		
		this.socket=s;
		this.user=user;
	}
	
	public void stop() {
		
		run = false;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		while (run) {
			//send messages
			
			
			
		}
		
	}

}
