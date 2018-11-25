import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
	
	static ArrayList <User> users = new ArrayList<>();

	public static void main(String[] args) {
		// https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
		
		
		
		//add stuff to fill them
		
		try {
			users.get(0);
			
			ServerSocket serverSocket = new ServerSocket(6000);
			
			// receive data
			Socket socket = serverSocket.accept();
			System.out.println("connected");
			InputStream input = socket.getInputStream();

			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//convert to JSON
			String jsonObject = "";
			while(reader.ready()) {
				
				jsonObject = jsonObject+reader.readLine();
			}
			
			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(jsonObject);
				
				
				
				String name= (String)json.get("message");
				
				//find user
				int i=0;
				boolean found = false;
				while(!found && i < users.size()) {
					
					User u = users.get(i);
					if (u.getName().equals(name)) {
						
						//pass the socket + run in seperate thread
						u.setSocket(socket);
						
						Thread t = new Thread(u);
						t.start();
						
					}
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
