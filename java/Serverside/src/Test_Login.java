import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Test_Login {



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//https://developers.google.com/identity/sign-in/android/backend-auth
		
try {
			String CLIENT_ID = "693179558720-r2i61d5b3ps30brbclr8vn88027foneu.apps.googleusercontent.com";
			System.out.println("socket made");
			ServerSocket serverSocket = new ServerSocket(6000);
			
			// receive data
			Socket socket = serverSocket.accept();
			System.out.println("connection accepted");
			
			//get first message from user;
			InputStream input = socket.getInputStream();

			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//convert to JSON
			String jsonObject = "";
			
			
				
				jsonObject = reader.readLine();
			
				System.out.println(jsonObject);
			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(jsonObject);
				
				
				String token =  (String)json.get("message");
				String username= (String)json.get("extra");
				System.out.println(username+ " tries to log in with token "+ token);
				
				JsonFactory jsonFactory=new JacksonFactory();
				HttpTransport transport=new NetHttpTransport();
				
				GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					    // Specify the CLIENT_ID of the app that accesses the backend:
					    .setAudience(Collections.singletonList(CLIENT_ID))
					    // Or, if multiple clients access the backend:
					    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
					    .build();

					// (Receive idTokenString by HTTPS POST)

					String idTokenString = token;
					GoogleIdToken idToken = verifier.verify(idTokenString);
					if (idToken != null) {
					  Payload payload = idToken.getPayload();

					  // Print user identifier
					  String userId = payload.getSubject();
					  System.out.println("User ID: " + userId);

					  // Get profile information from payload
					  String email = payload.getEmail();
					  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
					  String name = (String) payload.get("name");
					  String pictureUrl = (String) payload.get("picture");
					  String locale = (String) payload.get("locale");
					  String familyName = (String) payload.get("family_name");
					  String givenName = (String) payload.get("given_name");
					  
					  System.out.println(email+"\n"+emailVerified+"\n"+name+"\n"+pictureUrl+"\n"+locale+"\n"+familyName+"\n"+givenName);

					  // Use or store profile information
					  // ...

					} else {
					  System.out.println("Invalid ID token.");
					}
				
				
			} catch (ParseException | GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
