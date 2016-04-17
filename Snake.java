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

/** class Snake */
public class Snake implements ActionListener, KeyListener {

  public static Snake snake;
  public static JFrame jFrame;
  private final int SPEED = 100;
  private Timer timer = new Timer(SPEED, this);
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
  private final static int scalesCount = 20;
  public static int windowX = SCALE * scalesCount + 6;
  public static int windowY = SCALE * scalesCount - 1;
  private static boolean started = false;
  private boolean auto = false;
  private int height;
  private final int POINT = 10;

  /** Standard constructor*/
  public Snake() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    createFrame();
  }

  /** main function */
  public static void main(String[] args) {
    snake = new Snake();
  }

  /** this function create frame at the center of the screen with menu */
  public void createFrame() {
    dim = Toolkit.getDefaultToolkit().getScreenSize();
    jFrame = new JFrame("Snake");
    jFrame.setVisible(true);
    jFrame.setSize(windowX, windowY);
    jFrame.setResizable(false);
    jFrame.setLocation(dim.width / 2 - jFrame.getWidth() / 2,
        dim.height / 2 - jFrame.getHeight() / 2);
    jFrame.add(new MenuPanel());
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.addKeyListener(this);
  }

  /** startGame method make all variables at initial state and start the timer */
  public void startGame() {
    jFrame.getContentPane().removeAll();
    jFrame.add(new RenderPanel());
    directionUpdated = false;
    up = false;
    down = false;
    left = false;
    right = false;
    jFrame.repaint();
    jFrame.revalidate();
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
    jFrame.requestFocusInWindow();
  }

  /** startAuto method initial variables, make 'auto' true and start the timer */
  public void startAuto() {
    auto = true;
    jFrame.getContentPane().removeAll();
    jFrame.add(new RenderPanel());
    jFrame.removeKeyListener(this);
    directionUpdated = false;
    up = false;
    down = false;
    left = false;
    right = false;
    jFrame.repaint();
    jFrame.revalidate();
    started = true;
    over = false;
    paused = false;
    score = 0;
    tailLength = 10;
    direction = DOWN;
    head = new Point(0, 0);
    random = new Random();
    snakeParts.clear();
    cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    timer = new Timer(5, this);
    timer.start();
  }

  /** This method calls every timer tick */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (started) {
      if (!auto) {
        /** if non-auto mode we set the direction */
        if (up && direction != DOWN && !directionUpdated) {
          direction = UP;
          directionUpdated = true;
        }
        if (down && direction != UP && !directionUpdated) {
          direction = DOWN;
          directionUpdated = true;
        }
        if (left && direction != RIGHT && !directionUpdated) {
          direction = LEFT;
          directionUpdated = true;
        }
        if (right && direction != LEFT && !directionUpdated) {
          direction = RIGHT;
          directionUpdated = true;
        }
      }
      jFrame.repaint();

      /** check if the game is over */
      if (over) {
        paused = true;
        /** Create little window */
        Object[] options = {"Продолжим", "Давай меню"};
        int n = JOptionPane.showOptionDialog(null, "Что делаем?", "Игра окончена",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n > 0) {
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
          jFrame.getContentPane().removeAll();
          jFrame.add(new MenuPanel());
          jFrame.repaint();
          jFrame.revalidate();
        } else {
          snake.startGame();
        }
      }

      if (head != null && !over && !paused) {
        /** if auto mode - switch the direction */
        if (auto) {
          switch (direction) {
            case DOWN:
              if (head.y == windowY / SCALE - 1) {
                direction = RIGHT;
              }
              break;
            case RIGHT:
              if (head.x % 2 != 0) {
                direction = UP;
              }
              if (head.x % 2 == 0) {
                direction = DOWN;
              }
              height = random.nextInt(windowY / SCALE - 1);
              if (height == 0) {
                ++height;
              }
              break;
            case UP:
              if (head.y == height && head.x != windowX / SCALE - 1) {
                direction = RIGHT;
              }
              if (head.y == 0 && head.x == windowX / SCALE - 1) {
                direction = LEFT;
              }
              break;
            case LEFT:
              if (head.x == 0) {
                direction = DOWN;
              }
              break;
          }
        }

        directionUpdated = false;
        snakeParts.add(new Point(head.x, head.y));

        /** change location of the head. If head is on the tail - game over */
        if (direction == UP) {
          if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1)) {
            head = new Point(head.x, head.y - 1);
          } else {
            over = true;
          }
        }

        if (direction == DOWN) {
          if (head.y < windowY / SCALE - 1 && noTailAt(head.x, head.y + 1)) {
            head = new Point(head.x, head.y + 1);
          } else {
            over = true;
          }
        }

        if (direction == LEFT) {
          if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y)) {
            head = new Point(head.x - 1, head.y);
          } else
            over = true;
        }

        if (direction == RIGHT) {
          if (head.x < windowX / SCALE - 1 && noTailAt(head.x + 1, head.y)) {
            head = new Point(head.x + 1, head.y);
          } else {
            over = true;
          }
        }

        if (snakeParts.size() > tailLength) {
          snakeParts.remove(0);
        }

        if (cherry != null) {
          /** if snake eat the cherry */
          if (head.equals(cherry)) {
            score += POINT;
            tailLength++;
            if (tailLength < ((windowX / SCALE) * (windowY / SCALE) - 1)) {
              setCherryLocation();
            } else {
              cherry = null;
            }
          }
        }
      }
    }
  }

  /** if tail exist on (x,y) point - return false, else - true */
  public boolean noTailAt(int x, int y) {
    for (int i = 1; i < snakeParts.size() - 1; i++) {
      if (snakeParts.get(i).equals(new Point(x, y))) {
        return false;
      }
    }
    return true;
  }

  /** Set random cherry location */
  public void setCherryLocation() {
    cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    for (int i = 0; i < snakeParts.size(); i++) {
      /** if cherry on the head - change location */
      if (head.getLocation().equals(cherry.getLocation())) {
        cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
        i = 0;
      }
      /** if cherry on the snake part - change location */
      if (snakeParts.get(i).getLocation().equals(cherry.getLocation())) {
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

  /** this method change speed of timer ticks */
  public void changeTimer(int n) {
    timer = new Timer(n, this);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (started) {
      int i = e.getKeyCode();
      if (i == KeyEvent.VK_LEFT) {
        left = true;
      }
      if (i == KeyEvent.VK_RIGHT) {
        right = true;
      }
      if (i == KeyEvent.VK_UP) {
        up = true;
      }
      if (i == KeyEvent.VK_DOWN) {
        down = true;
      }
      if (i == KeyEvent.VK_SPACE) {
        paused = !paused;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (started) {
      int i = e.getKeyCode();
      if (i == KeyEvent.VK_LEFT) {
        left = false;
      }
      if (i == KeyEvent.VK_RIGHT) {
        right = false;
      }
      if (i == KeyEvent.VK_UP) {
        up = false;
      }
      if (i == KeyEvent.VK_DOWN) {
        down = false;
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent arg0) {}
}
