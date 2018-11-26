import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.parser.JSONParser;

public class Test_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
				
		try {
			Socket socket = new Socket("192.168.0.150", 6000);
			
			System.out.println("connection made");
			
			
			SimpleMessage message = new SimpleMessage(0, "", "user2");
			
			InputStream input = socket.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			JSONObject obj = message.toJSON();
			String s= obj.toJSONString();
			System.out.println(s);
			
			
			writer.println(s);
			message = new SimpleMessage(1, "BLABLA", "location");
			System.out.println("start sending message");
			writer.println(message.toJSON().toJSONString());
			System.out.println("send message");
			
			System.out.println("read message");
			System.out.println(reader.readLine());
			
			//logout
			message = new SimpleMessage(0, "logout", "");
			writer.println(message.toJSON().toJSONString());
			System.out.println("logged out");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
