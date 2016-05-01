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
  static final int MIN = 0;
  static final int MAX = 3;
  static final int INIT = 0;
  private int value;
  private int counter = 0;
  private final int easySpeed = 90;
  private final int mediumSpeed = 70;
  private final int hardSpeed = 40;
  private final int unrealSpeed = 20;
  private UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();

  MenuPanel() {
    lookAndFeel = new JButton("LookAndFeel");
    lookAndFeel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (counter == lafs.length - 1) {
          counter = 0;
        }
        try {
          UIManager.setLookAndFeel(lafs[++counter].getClassName());
          SwingUtilities.updateComponentTreeUI(Snake.jFrame);
          repaint();
        } catch (Exception ee) {
          ee.printStackTrace();
        }
      }
    });
    add(lookAndFeel);

    btn = new JButton("Старт");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startGame("standart");
      }
    });
    add(btn);

    auto = new JButton("Авто");
    auto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Snake.snake.startGame("auto");
      }
    });
    add(auto);

    speed = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INIT);
    speed.addChangeListener(this);
    speed.setMajorTickSpacing(1);
    speed.setPaintTicks(true);
    add(speed);
  }

  /** paintComponent method */
  public void paintComponent(Graphics gComponent) {
    super.paintComponent(gComponent);
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
    gComponent.setColor(Color.BLACK);
    gComponent.drawString(string, (int) getWidth() / 2 - string.length(), 100);
  }

  /** Calls when we do smth with slider */
  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    value = (int) (source.getValue());
    switch (value) {
      case 3:
        Snake.snake.changeTimer(unrealSpeed);
        break;
      case 2:
        Snake.snake.changeTimer(hardSpeed);
        break;
      case 1:
        Snake.snake.changeTimer(mediumSpeed);
        break;
      case 0:
        Snake.snake.changeTimer(easySpeed);
        break;
    }
    this.repaint();
  }
}

