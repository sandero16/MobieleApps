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
		this.user=u;
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
			System.out.println("sm:"+sm.toString());
			System.out.println(user.name + " --- message received");
			writer.println(sm.toJSON().toJSONString());
			
			user.messageIsSend();
			System.out.println(user.name + " --- message sent to app");
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
