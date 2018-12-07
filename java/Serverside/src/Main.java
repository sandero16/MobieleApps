import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Main {
	
	static ArrayList <User> users = new ArrayList<>();
	private static String CLIENT_ID;

	public static void main(String[] args) {
		// https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
		
		CLIENT_ID = "693179558720-r2i61d5b3ps30brbclr8vn88027foneu.apps.googleusercontent.com";
		
		//init verifier
		JsonFactory jsonFactory=new JacksonFactory();
		HttpTransport transport=new NetHttpTransport();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			    // Specify the CLIENT_ID of the app that accesses the backend:
			    .setAudience(Collections.singletonList(CLIENT_ID))
			    // Or, if multiple clients access the backend:
			    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
			    .build();
		
		//add stuff to create users
		
		users.add(new User("user1"));
		users.add(new User("user2"));
		users.add(new User("user"));
		users.add(new User("106949242187358004744"));
		
		JSONParser parser = new JSONParser();
		
		try {
			
			System.out.println("socket made");
			ServerSocket serverSocket = new ServerSocket(6000);
			
			// receive data
			while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("connection accepted");
			
			//get first message from user;
			InputStream input = socket.getInputStream();

			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//convert to JSON
			String jsonObject = "";
			
			
				
				jsonObject = reader.readLine();
			
				System.out.println(jsonObject);
			
			try {
				JSONObject json = (JSONObject) parser.parse(jsonObject);
				
				
				
				String token= (String)json.get("message");
				System.out.println("token : " + token);
				
				//get userid from token
				

					// (Receive idTokenString by HTTPS POST)

					GoogleIdToken idToken = verifier.verify(token);
					if (idToken != null) {
					  Payload payload = idToken.getPayload();

					  // Print user identifier
					  String user = payload.getSubject();
					  System.out.println("User ID: " + user);
				
						//find user
						int i=0;
						boolean found = false;
						while(!found && i < users.size()) {
							
							User u = users.get(i);
							System.out.println(user +"|"+u.getName());
							if (u.getName().equals(user)) {
								
								//pass the socket + run in seperate thread
								u.setSocket(socket);
								
								Thread t = new Thread(u);
								t.start();
								
							}
							i++;
					
				}}
			} catch (ParseException | GeneralSecurityException e) {
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
