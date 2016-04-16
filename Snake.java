package Snake;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

public class Snake implements ActionListener, KeyListener {

  public static Snake snake;
  public static JFrame jframe;
  private static RenderPanel renderPanel;
  public static MenuPanel menuPanel;
  private Timer timer = new Timer(100, this);
  private ArrayList<Point> snakeParts = new ArrayList<Point>();
  private static final int UP = 0;
  private static final int DOWN = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;
  private static boolean up = false;
  private static boolean down = false;
  private static boolean left = false;
  private static boolean right = false;
  private static boolean directionUpdated = true;
  public static final int SCALE = 30;
  private int direction = DOWN;
  private int score;
  private int tailLength;
  private Point head;
  private Point cherry;
  private Random random;
  private boolean over = false;
  private Dimension dim;
  private boolean paused = false;
  public static int windowX = SCALE * 20 + 6;
  public static int windowY = SCALE * 20 - 1;
  private static boolean started = false;
  private boolean auto = false;
  private int height;

  public Snake() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    createFrame();
  }

  public static void main(String[] args) {
    snake = new Snake();
  }

  public void createFrame() { // create a frame at the center of the screen with menu
    dim = Toolkit.getDefaultToolkit().getScreenSize();
    jframe = new JFrame("Snake");
    jframe.setVisible(true);
    jframe.setSize(windowX, windowY);
    jframe.setResizable(false);
    jframe.setLocation(dim.width / 2 - jframe.getWidth() / 2,
        dim.height / 2 - jframe.getHeight() / 2);
    jframe.add(menuPanel = new MenuPanel());
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.addKeyListener(this);
  }

  public void startGame() {
    jframe.getContentPane().removeAll();
    jframe.add(renderPanel = new RenderPanel());// if the game started put renderPanel on frame
    directionUpdated = false;
    up = false;
    down = false;
    left = false;
    right = false;
    renderPanel.repaint();
    renderPanel.revalidate();
    started = true;
    over = false;
    paused = false;
    score = 0;
    tailLength = 7;
    direction = DOWN;
    head = new Point(0, 0);
    random = new Random();
    snakeParts.clear();
    cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    timer.start();
    jframe.requestFocusInWindow();
  }

  public void startAuto() { // auto mode
    auto = true;
    jframe.getContentPane().removeAll();
    jframe.add(renderPanel = new RenderPanel());
    jframe.removeKeyListener(this);
    directionUpdated = false;
    up = false;
    down = false;
    left = false;
    right = false;
    renderPanel.repaint();
    renderPanel.revalidate();
    started = true;
    over = false;
    paused = false;
    score = 0;
    tailLength = 100;
    direction = DOWN;
    head = new Point(0, 0);
    random = new Random();
    snakeParts.clear();
    cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    timer = new Timer(5, this);
    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (started) {
      if (!auto) {// if non-auto mode we set the direction
        if (up == true && direction != DOWN && directionUpdated == false) {
          direction = UP;
          directionUpdated = true;
        }
        if (down == true && direction != UP && directionUpdated == false) {
          direction = DOWN;
          directionUpdated = true;
        }
        if (left == true && direction != RIGHT && directionUpdated == false) {
          direction = LEFT;
          directionUpdated = true;
        }
        if (right == true && direction != LEFT && directionUpdated == false) {
          direction = RIGHT;
          directionUpdated = true;
        }
      }
      renderPanel.repaint();

      if (over) { // check if game is over
        paused = true; // pause the game
        Object[] options = {"Продолжим", "Давай меню"}; // create a little window
        int n = JOptionPane.showOptionDialog(null, "Что делаем?", "Игра окончена",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, // the titles of
                                                                                    // buttons
            options[0]); // default button title
        if (n > 0) {// if pressed "Давай меню"
          timer.stop();
          n = 0;
          over = false;
          snakeParts = new ArrayList<Point>();
          up = false;
          down = false;
          left = false;
          right = false;
          directionUpdated = true;
          direction = DOWN;
          over = false;
          paused = false;
          started = false;
          jframe.getContentPane().removeAll();
          jframe.add(menuPanel = new MenuPanel()); // menu panel
          jframe.repaint();
          jframe.revalidate();
        } else { // if pressed "Продолжим"
          snake.startGame(); // start new game
        }
      }

      if (head != null && !over && !paused) {
        if (auto) { // if auto mode - switch the direction
          switch (direction) {
            case DOWN:
              if (head.y == windowY / SCALE - 1)
                direction = RIGHT;
              break;
            case RIGHT:
              if (head.x % 2 != 0)
                direction = UP;
              if (head.x % 2 == 0)
                direction = DOWN;
              height = random.nextInt(windowY / SCALE - 1);
              if (height == 0)
                ++height;
              break;
            case UP:
              if (head.y == height && head.x != windowX / SCALE - 1) {
                direction = RIGHT;
              }
              if (head.y == 0 && head.x == windowX / SCALE - 1)
                direction = LEFT;
              break;
            case LEFT:
              if (head.x == 0)
                direction = DOWN;
              break;
          }
        }

        directionUpdated = false;
        snakeParts.add(new Point(head.x, head.y));

        // change location of the head. If head is on the tail - game over
        if (direction == UP) {
          if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1))
            head = new Point(head.x, head.y - 1);
          else
            over = true;
        }

        if (direction == DOWN) {
          if (head.y < windowY / SCALE - 1 && noTailAt(head.x, head.y + 1))
            head = new Point(head.x, head.y + 1);
          else
            over = true;
        }

        if (direction == LEFT) {
          if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y))
            head = new Point(head.x - 1, head.y);
          else
            over = true;
        }

        if (direction == RIGHT) {
          if (head.x < windowX / SCALE - 1 && noTailAt(head.x + 1, head.y))
            head = new Point(head.x + 1, head.y);
          else
            over = true;
        }

        if (snakeParts.size() > tailLength) // remove the last snake part
          snakeParts.remove(0);

        if (cherry != null) {
          if (head.equals(cherry)) { // if snake eat the cherry
            score += 10;
            tailLength++;
            if (tailLength < ((windowX / SCALE) * (windowY / SCALE) - 1)) // if we have space for
                                                                          // cherry
              setCherryLocation();
            else
              cherry = null;
          }
        }
      }
    }
  }

  public boolean noTailAt(int x, int y) { // if tail exist on (x,y) point - return false, else -
                                          // true
    for (int i = 1; i < snakeParts.size() - 1; i++) {
      if (snakeParts.get(i).equals(new Point(x, y)))
        return false;
    }
    return true;
  }

  public void setCherryLocation() {
    cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));// set
                                                                                         // random
                                                                                         // cherry
                                                                                         // location
    for (int i = 0; i < snakeParts.size(); i++) {
      if (head.getLocation().equals(cherry.getLocation())) { // if cherry on the head - change
                                                             // location
        cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
        i = 0; // restart checking
      }
      if (snakeParts.get(i).getLocation().equals(cherry.getLocation())) { // if cherry on the snake
                                                                          // part - change location
        cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
        i = 0;
      }
    }
  }

  public ArrayList<Point> getSnakeParts() {
    return snakeParts;
  }

  public int getCherryX() {
    return cherry.x;
  }

  public int getCherryY() {
    return cherry.y;
  }

  public int getHeadX() {
    return head.x;
  }

  public int getHeadY() {
    return head.y;
  }

  public int getTailLength() {
    return tailLength;
  }

  public int getScore() {
    return score;
  }

  public boolean started() {
    return started;
  }

  public void changeTimer(int n) {
    timer = new Timer(n, this);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (started) {
      int i = e.getKeyCode();
      if (i == KeyEvent.VK_LEFT)
        left = true;
      if (i == KeyEvent.VK_RIGHT)
        right = true;
      if (i == KeyEvent.VK_UP)
        up = true;
      if (i == KeyEvent.VK_DOWN)
        down = true;
      if (i == KeyEvent.VK_SPACE)
        paused = !paused;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (started) {
      int i = e.getKeyCode();
      if (i == KeyEvent.VK_LEFT)
        left = false;
      if (i == KeyEvent.VK_RIGHT)
        right = false;
      if (i == KeyEvent.VK_UP)
        up = false;
      if (i == KeyEvent.VK_DOWN)
        down = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent arg0) {}

}
