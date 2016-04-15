package Snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MenuPanel extends JPanel {

  private JButton btn;
  private JButton auto;

  MenuPanel() {

    btn = new JButton("��������"); // start the game
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startGame(); // if "��������" pressed
      }
    });
    add(btn); // add to frame

    auto = new JButton("����"); // auto mode
    auto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startAuto(); // if "����" pressed
      }
    });
    add(auto); // add to frame
  }
}

