package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements ChangeListener {

  private JButton btn;
  private JButton auto;
  private JButton lookAndFeel;
  private JSlider speed;
  static final int FPS_MIN = 0;
  static final int FPS_MAX = 3;
  static final int FPS_INIT = 0;
  private int value;
  private int counter = 0;
  private UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();

  MenuPanel() {

    lookAndFeel = new JButton("LookAndFeel"); // start the game
    lookAndFeel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (counter == lafs.length - 1)
            counter = 0;
          UIManager.setLookAndFeel(lafs[++counter].getClassName());
          SwingUtilities.updateComponentTreeUI(Snake.jframe);
        } catch (Exception ee) {
          ee.printStackTrace();
        }
      }
    });
    add(lookAndFeel); // add to frame

    btn = new JButton("Старт"); // start the game
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startGame(); // if "Старт" pressed
      }
    });
    add(btn); // add to frame

    auto = new JButton("Авто"); // auto mode
    auto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startAuto(); // if "Авто" pressed
      }
    });
    add(auto); // add to frame

    speed = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);

    speed.addChangeListener(this);
    speed.setMajorTickSpacing(10);
    speed.setPaintTicks(true);

    add(speed);


  }


  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    String str = "";
    switch (value) {
      case 0:
        str = "EAYSY";
        break;
      case 1:
        str = "MEDIUM";
        break;
      case 2:
        str = "HARD";
        break;
      case 3:
        str = "UNREAL";
        break;
    }
    String string = "SPEED " + str;
    g.setColor(Color.BLACK);
    g.drawString(string, (int) getWidth() / 2 - string.length(), 100);


  }


  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    value = (int) (source.getValue());
    switch (value) {
      case 3:
        Snake.snake.changeTimer(20);
        break;
      case 2:
        Snake.snake.changeTimer(40);
        break;
      case 1:
        Snake.snake.changeTimer(70);
        break;
      case 0:
        Snake.snake.changeTimer(90);
        break;
    }
    this.repaint();
  }
}

