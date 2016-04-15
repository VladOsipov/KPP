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
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

//
// cherry can be on snake
//
//
public class Snake implements ActionListener, KeyListener {

  public static Snake snake;
  public static JFrame jframe; // was non static
  public static RenderPanel renderPanel; // type was renderpanel and non-static\
  public static MenuPanel menuPanel;
  public Timer timer = new Timer(100, this);
  public ArrayList<Point> snakeParts = new ArrayList<Point>();
  public static final int UP = 0;
  public static final int DOWN = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;
  public static boolean up = false;
  public static boolean down = false;
  public static boolean left = false;
  public static boolean right = false;
  public static boolean directionUpdated = true;

  public static final int SCALE = 30;
  public int ticks = 0;
  public int direction = DOWN;
  public int score;
  public int tailLength;
  public Point head;
  public Point cherry;
  public Random random;
  public boolean over = false;
  public Dimension dim;
  public boolean paused = false;
  public static int windowX = SCALE * 20 + 6;
  public static int windowY = SCALE * 20 - 1;
  public static boolean started = false;
  public JPanel menu;
  public boolean auto = false;
  public int height;

  public Snake() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

    createFrame();
  }

  public void createFrame() {
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
    jframe.add(renderPanel = new RenderPanel());

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
    ticks = 0;
    direction = DOWN;
    head = new Point(0, 0);
    random = new Random();
    snakeParts.clear();
    cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    timer.start();
    jframe.requestFocusInWindow();
  }

  public void startAuto() {
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
    tailLength = 7;
    ticks = 0;
    direction = DOWN;
    head = new Point(0, 0);
    random = new Random();
    snakeParts.clear();
    cherry = new Point(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    timer = new Timer(1, this);
    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (started) {


      if (!auto) {
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
      ticks++;
      if (over) {
        paused = true;
        Object[] options = {"Продолжим", "Давай меню"};
        int n = JOptionPane.showOptionDialog(null, "Что делаем?", "Игра окончена",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // do not use a custom
                                                                           // Icon
            options, // the titles of buttons
            options[0]); // default button title
        if (n > 0) {

          timer.stop();
          started = !started;
          n = 0;
          over = false;

          snakeParts = new ArrayList<Point>();

          up = false;
          down = false;
          left = false;
          right = false;
          directionUpdated = true;

          ticks = 0;
          direction = DOWN;
          over = false;
          paused = false;
          started = false;
          // snake = new Snake();
          jframe.getContentPane().removeAll();

          jframe.add(menuPanel = new MenuPanel());
          jframe.repaint();
          jframe.revalidate();
        } else {
          snake.startGame();
        }
      }

      if (/* ticks % 5 == 0 && */ head != null && !over && !paused) { ////////////
        if (auto) {
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
              break;
            case UP:
              if (/* (head.y==1 && head.x!=windowX/SCALE-1) || */
              head.y == height && head.x != windowX / SCALE - 1) {
                direction = RIGHT;;;
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

        ticks++;
        snakeParts.add(new Point(head.x, head.y));
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
          if (head.x < windowX / SCALE - 1 && noTailAt(head.x + 1, head.y)) // ?
            head = new Point(head.x + 1, head.y);
          else
            over = true;
        }

        if (snakeParts.size() > tailLength)
          snakeParts.remove(0);
        if (cherry != null) {
          if (head.equals(cherry)) {
            score += 10;
            tailLength++;
            if (tailLength < ((windowX / SCALE) * (windowY / SCALE) - 1))
              setCherryLocation();
            else
              cherry = null;
          }

        }
      }
    }
  }


  public boolean noTailAt(int x, int y) {
    for (int i = 1; i < snakeParts.size() - 1; i++) {
      if (snakeParts.get(i).equals(new Point(x, y)))
        return false;
    }
    return true;
  }

  public static void main(String[] args) {


    snake = new Snake();


  }

  public void setCherryLocation() {
    cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
    for (int i = 0; i < snakeParts.size(); i++) {
      if (head.getLocation().equals(cherry.getLocation())) {
        cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
        i = 0;
      }
      if (snakeParts.get(i).getLocation().equals(cherry.getLocation())) {
        cherry.setLocation(random.nextInt(windowX / SCALE), random.nextInt(windowY / SCALE));
        i = 0;
      }
    }
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

  public void changeTimer(int n) {
    timer = new Timer(n, this);
  }
}
