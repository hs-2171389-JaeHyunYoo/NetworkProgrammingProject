import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 
public class Client {
	final static int ServerPort = 5000;
	protected JTextField textField;
	protected JTextArea textArea;
	DataInputStream is;
	DataOutputStream os;
	String clientLier = null; //라이어에 해당하는 클라이언트의 번호(client N)
	static int time = 60; //타이머 시간
	String clientNum; //client 1, client 2, client 3, ..., client 6
	static int count = 0;	
	
	public Client() throws IOException {
		InetAddress ip = InetAddress.getByName("localhost");
		Socket s = new Socket(ip, ServerPort);
		is = new DataInputStream(s.getInputStream());
		os = new DataOutputStream(s.getOutputStream());
		
		//라이어에 해당하는 클라이언트 번호
		clientLier = is.readUTF();
		
		//ex. "client 4"
		clientNum = is.readUTF();
		//System.out.println(clientNum);
		
		count += 1;
		
		MyFrame f = new MyFrame(clientNum);
		
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String msg = is.readUTF();
						// 받은 패킷을 텍스트 영역에 표시한다.
						textArea.append(new String(msg) + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		Thread vote = new Thread(new Runnable() {
			public void run() {
				
			}
		});

		thread2.start();
		//System.out.println(count);
	}

	// 내부 클래스 정의
	class MyFrame extends JFrame implements ActionListener {
		public MyFrame(String s) {
			super(s);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			textField = new JTextField(30);
			
			//채팅 창 비활성화
			//textField.setEnabled(false);
			
			//라이어 여부에 대해 서로 다른 주제어 설정
			if(s.equals("client "+clientLier)) {
				textField.setText("APPLE"); //lier's word
			}
			else {
				textField.setText("ORANGE"); //no-lier's word
			}
			
			textField.addActionListener(this);

			textArea = new JTextArea(10, 30);
			//textArea.setEditable(false);

			add(textField, BorderLayout.PAGE_END);
			add(textArea, BorderLayout.CENTER);
			pack();
			setVisible(true);
			
			
		}

		public void actionPerformed(ActionEvent evt) {
			String s = textField.getText();
			try {
				os.writeUTF(clientNum);
				os.writeUTF(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			textField.selectAll();
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	public static void main(String[] args) throws IOException {
		for(int i=0; i<6; i++) {
			Client m = new Client();
		}
		MyTimer mt = new MyTimer(60);
		mt.startTimer();
	}
}