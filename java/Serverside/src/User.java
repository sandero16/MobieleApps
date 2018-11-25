import java.net.Socket;
import java.util.ArrayList;

public class User implements Runnable{
	
	String name;
	
	ArrayList <Conversation> conversations;
	
	//contains all messages to be received by the user.
	ArrayList <SimpleMessage> toReceive;
	
	boolean online = false;

	private Socket socket = null;
	
	public User(String name) {
		
		this.name=name;
		
		conversations = new ArrayList<>();
		
		toReceive = new ArrayList<>();
	}

	public void addConversation(Conversation conversation) {
		// TODO Auto-generated method stub
		conversations.add(conversation);
		
	}
	
	public void addOtherUserToConversation(String conversation, User u) {
		
		boolean found = false;
		int i = 0;
		while(!found || i>conversations.size()) {
			Conversation c=conversations.get(i);
			
			if (c.getName().equals(conversation)) {
				found = true;
				c.addUser(u);
			}
			
			
			
			
			i++;
		}
		
		
		
	}
	
	public void addMessageToSend(Conversation c, SimpleMessage message) {
		
		c.addMessage(message);
		
	}
	
	public synchronized void addMessageToReceive(SimpleMessage message) {
		
		
		toReceive.add(message);
		
	}
	
	public synchronized SimpleMessage getMessageToSend(){
		SimpleMessage message = null;
		
	
		if(toReceive.size()>0)
		message = toReceive.get(0);
		
		
		
		return message;
	}
	
	public synchronized void messageIsSend(){
		
		
		toReceive.remove(0);
		
	}
	
	void setSocket(Socket s) {
		this.socket = s;
	}
	
	public boolean isOnline() {
		return online;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean running=true;
		
		synchronized(this){
		online = true;
		}
		//start thread to send messages to the user
		
		
		//fetch and process messages from the user.
		while (running) {
			
			
			
			
			
			
			
			
		}
		
		
		
		
		
		
		synchronized(this){
		online = false;
		}
		
	}
	
	
	
	
	
	
	
	
	

}
