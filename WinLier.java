import javax.swing.*;
import java.awt.*;

public class WinLier extends JFrame {

    private JLabel label;

    public WinLier() {
        setTitle("승리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 패널 생성
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.YELLOW); // 배경색 설정

        // 시민 승리 라벨
        label = new JLabel("🙃 라이어 승리 🙃");
        label.setForeground(Color.RED);
        Font font = label.getFont();
        Font newFont = font.deriveFont(Font.BOLD, 30f); 
        label.setFont(newFont);

        // 라벨을 중앙에 배치하기 위한 레이아웃 설정
        GridBagConstraints happy = new GridBagConstraints();
        happy.gridx = 0;
        happy.gridy = 0;
        happy.anchor = GridBagConstraints.CENTER;

        panel.add(label, happy);

        // 패널을 프레임에 추가
        add(panel);
        setVisible(true);
    }

}
