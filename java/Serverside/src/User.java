import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class User implements Runnable {

	String name;

	ArrayList<Conversation> conversations;

	// contains all messages to be received by the user.
	ArrayList<SimpleMessage> toReceive;

	boolean online = false;

	private Socket socket = null;

	public User(String name) {

		this.name = name;

		conversations = new ArrayList<>();

		toReceive = new ArrayList<>();
	}

	public void addConversation(Conversation conversation) {
		// TODO Auto-generated method stub
		conversations.add(conversation);

	}

	public void addOtherUserToConversation(String conversation, String username) {

		boolean found = false;
		int i = 0;
		while (!found || i > conversations.size()) {
			Conversation c = conversations.get(i);

			if (c.getName().equals(conversation)) {
				found = true;
				
				for(int j = 0; j< Main.users.size(); j++) {
					User u = Main.users.get(j);
					
					if (u.getName().equals(username)) {
						
						c.addUser(u);
						break;
					}
					
					
				}
				
			}

			i++;
		}

	}

	public void addMessageToSend(Conversation c, SimpleMessage message) {

		c.addMessage(message);

	}

	public synchronized void addMessageToReceive(SimpleMessage message) {

		toReceive.add(message);
		notify();
	}

	public synchronized SimpleMessage getMessageToSend() throws InterruptedException {
		SimpleMessage message = null;

		while (toReceive.size() > 0) wait();
		
		message = toReceive.get(0);

		return message;
	}

	public synchronized void messageIsSend() {

		toReceive.remove(0);

	}

	void setSocket(Socket s) {
		this.socket = s;
	}

	public boolean isOnline() {
		return online;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		boolean running = true;
		try {
			InputStream input = socket.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			synchronized (this) {
				online = true;
			}
			// start thread to send messages to the user
			SendMessagesToUser runnable=new SendMessagesToUser(socket, this);
			Thread t = new Thread(runnable);
			t.start();
			
			JSONParser parser = new JSONParser();

			// fetch and process messages from the user.
			while (running) {
				
				//read incoming message
				
				String jsonObject = "";
				while(reader.ready()) {
					
					jsonObject = jsonObject+reader.readLine();
				}
				
				try {
					JSONObject json = (JSONObject) parser.parse(jsonObject);
					
					SimpleMessage message= new SimpleMessage(json);
					
					int type = message.getType();
					
					switch(type) {
					//stop running
					case 0: running=false;
							runnable.stop();
							break;
					//add msg to conversation
					case 1:
						String conversation = (String)json.get("conversation");
						boolean found = false;
						int i = 0;
						while (!found || i > conversations.size()) {
							Conversation c = conversations.get(i);

							if (c.getName().equals(conversation)) {
								found = true;
								addMessageToSend(c,message);
							}

							i++;
						}
						break;
					
					//add user to conversation
					case 2:
						addOtherUserToConversation(message.getConversation(), message.getExtra());
						break;
					
					//start new conversation
					case 3:
						
						new Conversation(message.getConversation(), this);
						
						break;
						
					default:
						System.out.println("invalid message received.");
					
					}
					
					
					
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//handle message

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		synchronized (this) {
			online = false;
		}

	}

}
