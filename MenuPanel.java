package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements ChangeListener {

  private JButton btn;
  private JButton auto;
  private JSlider speed;
  static final int FPS_MIN = 0;
  static final int FPS_MAX = 100;
  static final int FPS_INIT = 10;
  private int fps = 50;

  MenuPanel() {
    btn = new JButton("Кнопочка");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.changeTimer(fps);
        Snake.snake.startGame();
      }
    });
    add(btn);

    auto = new JButton("Авто");
    auto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startAuto();
      }
    });
    add(auto);


    speed = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);

    speed.addChangeListener(this);
    speed.setMajorTickSpacing(10);
    speed.setPaintTicks(true);

    add(speed);


  }


  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    String str = "";
    if (fps < 50)
      str = "HARD";
    if (fps >= 50 && fps < 65)
      str = "medium";
    if (fps >= 65 && fps < 75)
      str = "easy";
    if (fps >= 75)
      str = "extra easy";
    String string = "SPEED " + str;
    g.setColor(Color.BLACK);
    g.drawString(string, (int) getWidth() / 2 - string.length() / 2, 100);
    // g.setColor(Color.red);
    // g.fillRect(0, 0, Snake.windowX, Snake.windowY);
  }


  @Override
  public void stateChanged(ChangeEvent e) {

    JSlider source = (JSlider) e.getSource();
    fps = (int) (source.getValue());
    fps -= FPS_MAX;
    fps = Math.abs(fps);
    this.repaint();


  }

}

