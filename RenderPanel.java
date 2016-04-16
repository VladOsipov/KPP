package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class RenderPanel extends JPanel {

  private Color YELLOW = new Color(16646040);
  private Color GREEN = new Color(10026908);

  public void paintComponent(Graphics g) {
    if (Snake.snake.started()) {

      super.paintComponent(g);
      g.setColor(YELLOW);
      g.fillRect(0, 0, Snake.windowX, Snake.windowY);// fill all the window
      Snake snake = Snake.snake;
      g.setColor(GREEN);

      for (Point point : snake.getSnakeParts()) { // fill all snake parts
        g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
        g.setColor(YELLOW); // fill a little yellow rectangle inside the snake part
        g.fillRect(point.x * Snake.SCALE + 5, point.y * Snake.SCALE + 5, Snake.SCALE - 10,
            Snake.SCALE - 10); 
        g.setColor(GREEN);
      }

      g.fillRect(snake.getHeadX() * Snake.SCALE, snake.getHeadY() * Snake.SCALE, Snake.SCALE,
          Snake.SCALE); // fill the head
      g.setColor(Color.RED); // fill the cherry
      if (Snake.snake.getTailLength() < 379)
        g.fillRect(snake.getCherryX() * Snake.SCALE, snake.getCherryY() * Snake.SCALE, Snake.SCALE,
            Snake.SCALE);
      String string = "Score " + snake.getScore() + ", Length " + snake.getTailLength();
      g.setColor(Color.BLACK);
      g.drawString(string, (int) getWidth() / 2 - string.length() / 2, 30); // print the score

    }
  }
}
