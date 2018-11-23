import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		// https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
		// voorbeelden
		
		try {
			
			
			ServerSocket serverSocket = new ServerSocket(6000);
			
			// receive data
			Socket socket = serverSocket.accept();
			System.out.println("connected");
			InputStream input = socket.getInputStream();

			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			System.out.println(reader.readLine());
			
			// send data
			OutputStream output = socket.getOutputStream();
			
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("This is a message sent to the server");
			System.out.println("Message send.");
			
			serverSocket.close();
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
