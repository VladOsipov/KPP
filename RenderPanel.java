package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel {

  private Color YELLOW = new Color(16646040);
  private Color GREEN = new Color(10026908);

  public void paintComponent(Graphics gComponent) {
    if (Snake.snake.started()) {

      super.paintComponent(gComponent);
      gComponent.setColor(YELLOW);
      gComponent.fillRect(0, 0, Snake.windowX, Snake.windowY);// fill all the window
      Snake snake = Snake.snake;
      gComponent.setColor(GREEN);

      for (Point point : snake.getSnakeParts()) { // fill all snake parts
        gComponent.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
        gComponent.setColor(YELLOW); // fill a little yellow rectangle inside the snake part
        gComponent.fillRect(point.x * Snake.SCALE + 5, point.y * Snake.SCALE + 5, Snake.SCALE - 10,
            Snake.SCALE - 10);
        gComponent.setColor(GREEN);
      }

      gComponent.fillRect(snake.getHeadX() * Snake.SCALE, snake.getHeadY() * Snake.SCALE,
          Snake.SCALE, Snake.SCALE); // fill the head
      gComponent.setColor(Color.RED); // fill the cherry
      if (Snake.snake.getTailLength() < 379)
        gComponent.fillRect(snake.getCherryX() * Snake.SCALE, snake.getCherryY() * Snake.SCALE,
            Snake.SCALE, Snake.SCALE);
      String string = "Score " + snake.getScore() + ", Length " + snake.getTailLength();
      gComponent.setColor(Color.BLACK);
      gComponent.drawString(string, (int) getWidth() / 2 - string.length() / 2, 30); // print the
                                                                                     // score

    }
  }
}
