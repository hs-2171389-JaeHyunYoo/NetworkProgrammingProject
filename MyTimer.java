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

        setSize(300, 200);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void startTimer() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setText(Integer.toString(time));
                time--;

                if (time < 0) {
                    ((Timer) e.getSource()).stop();
                    dispose(); // 타이머 종료 후 창 닫기
                }
            }
        });

        timer.start();
        setVisible(true);
    }

}
