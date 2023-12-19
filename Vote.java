import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Vote extends JFrame {
    int maxVotes = 0;
    public int maxIndex = 0;
    private static int clientCount = 0;
    private JButton[] buttons = new JButton[6];
    private int[] voteCount = new int[6];
    private JLabel resultLabel;
    private JLabel resultLabel1;
    private JLabel resultLabel2;


    public Vote() {     
        setTitle("투표하기");
        setPreferredSize(new Dimension(400, 300));
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // 버튼 그리드를 담을 패널 설정
        JPanel gridPanel = new JPanel(new GridLayout(2, 3));

        // 버튼 생성 및 액션 리스너 연결
        for (int i = 0; i < 6; i++) {
            buttons[i] = new JButton("client " + (i + 1));
            buttons[i].addActionListener(new VoteButtonListener());
            gridPanel.add(buttons[i]);
            buttons[i].setBackground(Color.PINK);
            voteCount[i] = 0;
        }

        container.add(gridPanel, BorderLayout.CENTER);

        // 상단 공간 설정
        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setPreferredSize(new Dimension(10, 30));
        resultLabel = new JLabel("[ client " + (clientCount + 1) + " ]"); // client1, client2, ...
        resultLabel.setForeground(Color.RED); // 색상 변경 예시 (파란색)
        topSpacePanel.add(resultLabel);
        resultLabel1 = new JLabel("라이어를 지목해주세요 😶‍🌫️");
        topSpacePanel.add(Box.createRigidArea(new Dimension(10, 5))); // 공백 추가
        topSpacePanel.add(resultLabel1);
        container.add(topSpacePanel, BorderLayout.NORTH);

        // 하단 공간 설정
        JPanel bottomSpacePanel = new JPanel();
        bottomSpacePanel.setPreferredSize(new Dimension(10, 30));
        resultLabel2 = new JLabel("투표 결과가 여기에 표시됩니다.");
        bottomSpacePanel.add(resultLabel2);
        container.add(bottomSpacePanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    
    
    class VoteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            // 투표된 버튼 확인 후 투표수 증가
            for (int i = 0; i < 6; i++) {
                if (buttons[i] == clickedButton) {
                    voteCount[i]++;
                    break;
                }
            }

            clientCount++;
            
            // 버튼 클릭마다 resultLabel 업데이트
            if (clientCount < 6) {
                resultLabel.setText("[ client " + (clientCount + 1) + " ]"); // client1, client2, ...
                resultLabel.repaint(); // 화면 갱신
            }

            // 모든 클라이언트가 투표를 마쳤을 때 결과 확인
            if (clientCount == 6) {
               resultLabel.setText(" ");
               resultLabel1.setText(" 결과 ");
              findMostVotedButton();       
            }
        }
    }
    

    
    // 가장 많은 투표를 받은 버튼 찾기
    public void findMostVotedButton() {
    	Server.round += 1; //라운드 수 증가, 서버에 저장되게 됨
    	for (int i = 0; i < 6; i++) {
            if (voteCount[i] > maxVotes) {
                maxVotes = voteCount[i];
                maxIndex = i;
                System.out.println("제일 많이 클릭된 클라이언트는 "+(i+1)+"번 클라이언트");
            }
        }

        String buttonText = buttons[maxIndex].getText();
        resultLabel2.setText("가장 많이 투표된 버튼 : " + buttonText);
        
        
        Client.voteClient = maxIndex;
        
        System.out.println(maxIndex);
        for(int i=0; i<Client.clientManagement.size(); i++) {
        	Client.clientManagement.get(i).textArea.append("[System] : "+(maxIndex+1)+"번째 참가자가 탈락하였습니다.\n");
        	if(i == maxIndex) {
        		Client.clientManagement.get(maxIndex).closeWindow(); 
        		buttons[maxIndex].setEnabled(false);
        		//System.out.println("["+maxIndex+"] 참가자 탈락됨"); //3
        		System.out.println("[System] 1 라운드 결과 : "+(i+1)+"번 탈락");
        		if((i+1) == Integer.parseInt(Client.clientLier)) {
        			System.out.println("[System] 라이어 탈락");
        			dispose();
        			WinClient wc = new WinClient();
        		}
        		else {
        			System.out.println("[System] 라이어 아님");
        			dispose();
        	        
        			maxVotes = 0;
        			clientCount = 0;
        			for(int j=0; j<6; j++) {
        				voteCount[j] = 0;
        			}
        			if(Server.round == 3) {
        				System.out.println("시민의 패배");
        				dispose();
        				WinLier wl = new WinLier();
        			}
        			else {
        				MyTimer mt2 = new MyTimer(10);
            	        mt2.startTimer();
            	        mt2.setLocation(1100, 300);
        			}
        	        
        		}
        	}
        } 
        
    }


}
