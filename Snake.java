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
public class Snake implements ActionListener, KeyListener, Runnable {

  public static Snake snake;
  public static JFrame jFrame;
  private static int SPEED, count;
  public static int windowX, windowY;
  private Timer timer;
  private ArrayList<Point> snakeParts;
  private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, scalesCount = 20;
  public static final int SCALE = 30;
  private static boolean up, down, left, right, directionUpdated, over, paused, started, auto,
      replayFl;
  private int direction, score, tailLength, height;
  private Point head, cherry;
  private Random random;
  private Dimension dim;
  private final int POINT = 10;
  private Replay replay;

  /** Standard constructor */
  public Snake() {
    SPEED = 100;
    replay = new Replay();
    snakeParts = new ArrayList<Point>();
    up = false;
    down = false;
    left = false;
    right = false;
    directionUpdated = true;
    direction = DOWN;
    over = false;
    paused = false;
    windowX = SCALE * scalesCount + 6;
    windowY = SCALE * scalesCount - 1;
    started = false;
    auto = false;
    timer = new Timer(SPEED, this);
  }

  /** main function */
  public static void main(String[] args) {
    snake = new Snake();
    snake.createFrame();
  }

  /** this function create frame at the center of the screen with menu */
  public void createFrame() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    dim = Toolkit.getDefaultToolkit().getScreenSize();
    jFrame = new JFrame("Snake");
    jFrame.setVisible(true);
    jFrame.setSize(windowX, windowY);
    jFrame.setResizable(false);
    jFrame.setLocation(dim.width / 2 - jFrame.getWidth() / 2,
        dim.height / 2 - jFrame.getHeight() / 2);
    jFrame.add(new MenuPanel());
    jFrame.repaint();
    jFrame.revalidate();
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /** startGame method make all variables at initial state and start the timer */
  public void startGame(String mode) {
    if (mode.equals("auto")) {
      auto = true;
      jFrame.removeKeyListener(this);
      timer = new Timer(5, this);
    } else {
      auto = false;
    }
    if (mode.equals("replay")) {
      replayFl = true;
      count = 2;
      cherry = replay.getCherry(count);
    } else {
      replayFl = false;
    }
    if (!mode.equals("auto") && !mode.equals("replay")) {
      jFrame.addKeyListener(this);
    }
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
    if (!replayFl) {
      cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    }
    timer.start();
    jFrame.requestFocusInWindow();
  }

  /** This method calls every timer tick */
  @Override
  public void actionPerformed(ActionEvent arg0) {

    if (started) {
      /**Add direction to directions list and cherry to cherries list*/
      replay.addDirection(direction);
      replay.addCherry(cherry.x, cherry.y);

      if (!auto) {
        /** if non-auto mode we set the direction */
        changeDirection();
      } else {
        autoSwitch();
      }

      Thread childThread = new Thread(this);
      childThread.start();
      if (replayFl) {
        /**Get the direction and cherry location if replay mode*/
        direction = replay.getDirection(count);
        cherry = replay.getCherry(count - 1);
        count++;
      }

      /** check if the game is over */
      if (over) {
        gameOver();
      }

      if (head != null && !over && !paused) {

        directionUpdated = false;
        snakeParts.add(new Point(head.x, head.y));

        /** change location of the head. If head is on the tail - game over */
        switch (direction) {
          case UP:
            if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1)) {
              head = new Point(head.x, head.y - 1);
            } else {
              over = true;
            }
            break;
          case DOWN:
            if (head.y < windowY / SCALE - 1 && noTailAt(head.x, head.y + 1)) {
              head = new Point(head.x, head.y + 1);
            } else {
              over = true;
            }
            break;
          case LEFT:
            if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y)) {
              head = new Point(head.x - 1, head.y);
            } else
              over = true;
            break;
          case RIGHT:
            if (head.x < windowX / SCALE - 1 && noTailAt(head.x + 1, head.y)) {
              head = new Point(head.x + 1, head.y);
            } else {
              over = true;
            }
            break;
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
              if (!replayFl) {
                setCherryLocation();
              }
            } else {
              cherry = null;
            }
          }
        }
      }
      try {
        childThread.join();
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
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

  public void gameOver() {
    replay.save();
    paused = true;
    /** Create little window */
    Object[] options = {"Продолжим", "Давай меню", "Replay"};
    int n = JOptionPane.showOptionDialog(null, "Что делаем?", "Игра окончена",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    switch (n) {
      case 1:
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
        break;
      case 0:
        snake.startGame("");
        break;
      case 2:
        replay.fillList();
        startGame("replay");
        break;
    }

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

 
  /**Switch the direction if auto-mode*/
  public void autoSwitch() {
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

  
  /**Change the direction if key pressed*/
  public void changeDirection() {
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

  @Override
  public void keyTyped(KeyEvent arg0) {}

  @Override
  public void run() {
    jFrame.repaint();
  }

}
