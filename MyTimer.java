import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyTimer extends JFrame {
    private JLabel display;
    private int time;

    public MyTimer(int initialTime) {
        this.time = initialTime;

        setTitle("Timer");
        Container cont = getContentPane();
        display = new JLabel();
        cont.add(display, BorderLayout.CENTER);
        
        Font font = display.getFont();
        Font newFont = font.deriveFont(40f);
        display.setFont(newFont);
        display.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트를 가운데 정렬

        setSize(300, 200);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cont.setBackground(Color.GRAY);	//배경색 변경
        display.setForeground(Color.YELLOW); // 글자색 변경
    }

    public void startTimer() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//타이머가 시작되면 게임 참여자들이 서로 의사소통하며 라이어를 특정할 수 있도록 채팅방 활성화
            	for(int i=0; i<Client.clientManagement.size(); i++) {
            		Client.clientManagement.get(i).textField.setEnabled(true);
            	}
                display.setText(Integer.toString(time));
                time--;

                if (time < 0) {
                    ((Timer) e.getSource()).stop();
                    //타이머가 종료되면 다시 게임 참여자들이 무분별하게 채팅을 하지 못하도록 채팅방 비활성화
                    for(int i=0; i<Client.clientManagement.size(); i++) {
                		Client.clientManagement.get(i).textField.setEnabled(false);
                	}
                    dispose(); // 타이머 종료 후 창 닫기
                    Vote vote = new Vote(); //투표 시작
                    vote.setLocation(1100, 50);
                }
                
            }
        });

        timer.start();
        setVisible(true);
    }

}
