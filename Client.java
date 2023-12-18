import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;




//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 
public class Client {
   //새로 만든 변수
	
   public static int x = 0;
   public static int y = 50;
   public static List<Client> clients = new ArrayList<>();
   public static Vector<Client> clientManagement = new Vector(); 
   final static int ServerPort = 5000;
   protected JTextField textField;
   protected JTextArea textArea;
   DataInputStream is;
   DataOutputStream os;
   static String clientLier = null; //라이어에 해당하는 클라이언트의 번호(client N)
   String clientNum; //client 1, client 2, client 3, ..., client 6
   static int count = 0;   
   
   public static int voteClient;
   
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

      thread2.start();
   }


   // 내부 클래스 정의
   class MyFrame extends JFrame implements ActionListener, FocusListener {
      private boolean isFirstClick = true;

      public MyFrame(String s) {
         super(s);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setLocation(x, y);
         
         
         textField = new JTextField(30);
         
         //채팅 창 비활성화
         //textField.setEnabled(false);
         
         //라이어 여부에 대해 서로 다른 주제어 설정
         if(s.equals("client "+ clientLier)) {
            textField.setForeground(Color.GRAY);
            textField.setText(" 라이어 : 사과"); //lier's word
         }
         else {
            textField.setForeground(Color.GRAY);
            textField.setText(" 시민 : 오렌지 "); //no-lier's word
         }
         
         
         textField.addFocusListener(this);
         textField.addActionListener(this);

         textArea = new JTextArea(10, 30);
         //textArea.setEditable(false);

         add(textField, BorderLayout.PAGE_END);
         add(textArea, BorderLayout.CENTER);
         pack();
         setVisible(true);
         
         
      }      
      
      private void openVotingWindowAfterTimer() {
           // 타이머 종료 후에 투표 창을 열도록 코드를 작성하세요
           JFrame votingFrame = new JFrame("투표 창");
           // ... (투표 창 UI 및 로직 설정)
       }
      
      public void focusGained(FocusEvent e) {
            // 사용자가 텍스트 필드에 포커스를 받으면, 기존 텍스트 지우고 사용자 입력 시작
          if (isFirstClick) {
                   textField.setText("");
                   textField.setForeground(Color.BLACK); // 포커스 받으면 텍스트 색상을 검정색으로 변경
                   isFirstClick = false;
               }
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

      @Override
      public void focusLost(FocusEvent e) {
         // TODO Auto-generated method stub
         
      }
   }
   
   

   public void closeWindow() {
	   //System.out.println(voteClient);
       SwingUtilities.invokeLater(() -> {
           JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(clients.get(voteClient).textField);
           frame.dispose(); // 창을 닫습니다.
       });
   }
   
    
   public static void main(String[] args) throws IOException {
	   
	   
	   for(int i=0; i<6; i++) {
         Client m = new Client();
         clientManagement.add(m);
         clients.add(m);
         //m.setLocation(300, 200); // 오류뜸
         x += 350; // x 위치를 조정하여 다른 위치에 배치
         if (i == 2) {
        	 y += 230;
        	 x = 0;
         }
         else
         y += 0; // y 위치를 조정하여 다른 위치에 배치
         
      }
      MyTimer mt = new MyTimer(10);
      mt.startTimer();
      mt.setLocation(1100,300);

      //
//      WinClient wn = new WinClient();
//      WinLier wn2 = new WinLier();
//      //위치
//	  wn.setLocation(1100, 300);
//	  wn2.setLocation(1100, 300);
      
	  
   }
}
