import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
		
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
		
		
		while (run) {
			//send messages
			
			SimpleMessage sm;
			try {
				sm = user.getMessageToSend();
			
			
			writer.println(sm.toJSON().toJSONString());
			
			user.messageIsSend();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
