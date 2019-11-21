package JavaTeamProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatThread extends Thread {

	static List<PrintWriter> chatList = Collections.synchronizedList(new ArrayList<PrintWriter>());
	Socket socket = null;
	PrintWriter printWriter = null;
	
	public ChatThread() {
		// TODO Auto-generated constructor stub
	}
	
	public ChatThread(Socket socket) {
		this.socket = socket;
		
		try {
			printWriter = new PrintWriter(socket.getOutputStream());
			chatList.add(printWriter);
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		String name = "";
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			name = reader.readLine(); 
			for(PrintWriter pw : chatList) {
				pw.write(name + " 님 입장\r\n");
				pw.flush();		
			}
			
			while(true) {
				String str = "";
				try {
					str = reader.readLine();
				} catch (SocketException e) {
					// TODO: handle exception
					break;
				}
				if(str == null) {
					break;
				}
				for(PrintWriter pw : chatList) {
					pw.write(name + " : " + str + "\r\n");
					pw.flush();
				}
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			chatList.remove(printWriter);
			for(PrintWriter pw : chatList) {
				pw.write(name + "님 나감\r\n");
				pw.flush();
			}
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
	}

}
