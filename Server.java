
import java.awt.Container;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
public class Server {

	static ArrayList<ServerThread> list = new ArrayList<>();
	static int clientCount = 0; //클라이언트의 수 count, 클라이언트 생성 시 타이틀 정하는데 사용 됨
	static Random random = new Random();
	static int lier = random.nextInt(6)+1; //라이어에 해당하는 클라이언트 번호
	static int round = 0;
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket ssocket = new ServerSocket(5000);
		
		Socket s;

		System.out.println("[System] 라이어는 "+lier+"번 참가자");
		System.out.println("[System] 6명이 모이면 게임이 시작됩니다.");
		//Vote.fixedLier = lier;
		//System.out.println("fixedLier = "+lier);
		for(int i=0; i<6; i++) {
			s = ssocket.accept();

			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			
			//라이어에 해당하는 클라이언트 번호(1~6)
			os.writeUTF(Integer.toString(lier));
			
			ServerThread thread = new ServerThread(s, (clientCount+1)+"번 참가자", is, os);
			list.add(thread);
			thread.start();
			
			if(i == 6) {
				//cleint 6
				os.writeUTF("client " + (clientCount+1));
				os.writeInt(clientCount);
				System.out.println(clientCount);
				
				break;
			}
			else {
				//cleint 1 ~ client 5
				os.writeUTF("client " + (clientCount+1));
				//os.writeInt(clientCount);
				clientCount++;
			}
		}
		System.out.println("[System] "+clientCount+"명이 모여 게임이 시작됩니다.");
	}
	
}

class ServerThread extends Thread {
	Scanner scn = new Scanner(System.in);
	private String name;
	final DataInputStream is;
	final DataOutputStream os;
	Socket s;
	boolean active;

	public ServerThread(Socket s, String name, DataInputStream is, DataOutputStream os) {
		this.is = is;
		this.os = os;
		this.name = name;
		this.s = s;
		this.active = true;
	}

	@Override
	public void run() {
		String whoIsTalking;
		String message;
		while (true) {
			try {
				whoIsTalking = is.readUTF();
				message = is.readUTF();
				System.out.println(whoIsTalking+" : "+message);
				if (message.equals("logout")) {
					this.active = false;
					this.s.close();
					break;
				}
				for (ServerThread t : Server.list) {
					t.os.writeUTF(this.name + " : " + message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			this.is.close();
			this.os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

