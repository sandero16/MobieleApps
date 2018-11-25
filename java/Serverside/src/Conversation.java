import java.util.ArrayList;

public class Conversation implements Runnable {

	String name;

	ArrayList<User> users;
	// remembers the last message received for each user.
	// the indices for the user are the same as in users.
	ArrayList<Integer> lastMessageReceived;

	ArrayList<SimpleMessage> messages;

	public Conversation(String name, User u) {

		this.name = name;
		users = new ArrayList<>();
		messages = new ArrayList<>();
		lastMessageReceived = new ArrayList<>();

		u.addConversation(this);
		users.add(u);
		lastMessageReceived.add(0);

	}

	public String getName() {
		return name;
	}

	public synchronized void addUser(User u) {
		users.add(u);
		lastMessageReceived.add(0);
		notify();

	}

	public synchronized void addMessage(SimpleMessage message) {

		messages.add(message);
		notify();

	}


	@Override
	public synchronized void run() {
		// TODO distribute the messages to the online users
		
		while (true) {
			try {
			//check all users
			for (int i = 0; i < users.size(); i++) {

				// get a user
				User u = users.get(i);

				// give the user object all the messages it needs to receive
				for (int j = lastMessageReceived.get(i); u.isOnline() && j < messages.size(); j++) {

					u.addMessageToReceive(messages.get(j));
					lastMessageReceived.set(i, j);

				}

			}
			
			
				wait();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}
