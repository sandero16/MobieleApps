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


	// contains all messages to be received by the user.
	ArrayList<SimpleMessage> toReceive;


	private Socket socket = null;

	public User(String name) {

		this.name = name;


		toReceive = new ArrayList<>();
	}



	//send message to other users
	public void sendMessage(SimpleMessage message) {

		for (User u : Main.users) {
			
			u.addMessageToReceive(message);
			
		}

	}

	public synchronized void addMessageToReceive(SimpleMessage message) {

		toReceive.add(message);
		notify();
	}

	public synchronized SimpleMessage getMessageToSend() throws InterruptedException {
		SimpleMessage message = null;

		while (toReceive.size() < 1) {wait();}
		
		message = toReceive.get(0);

		return message;
	}

	public synchronized void messageIsSend() {

		toReceive.remove(0);

	}

	void setSocket(Socket s) {
		this.socket = s;
	}


	public String getName() {
		return name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println(name + " logged in");

		boolean running = true;
		try {
			InputStream input = socket.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//start thread to send messages to the user
			SendMessagesToUser runnable=new SendMessagesToUser(socket, this);
			Thread t = new Thread(runnable);
			t.start();
			
			JSONParser parser = new JSONParser();

			// fetch and process messages from the user.
			while (running) {
				
				//read incoming message
				
				String jsonObject = "";
				System.out.println(name + " start reading");
				
					
					jsonObject = reader.readLine();
				
				
				System.out.println(name + " object read");
				try {
					JSONObject json = (JSONObject) parser.parse(jsonObject);
					
					System.out.println(name + "\n" + json);
					
					SimpleMessage message= new SimpleMessage(json);
					
					System.out.println(name + "\n" + message);
					
					int type = message.getType();
					
					switch(type) {
					//stop running
					case 0: running=false;
							runnable.stop();
							socket.close();
							socket = null;
							System.out.println(name + "\n" + "STOPPED");
							break;
					//send msg
					case 1:
						sendMessage(message);
						System.out.println(name + "\n" + "message send");
						break;
						
					default:
						System.out.println("invalid message received.");
					
					}
					
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//handle message

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
