package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel {

  private final int YELLOW_RGB = 16646040;
  private final int GREEN_RGB = 10026908;
  private Color YELLOW;
  private Color GREEN;
  private final int screenStartX = 0;
  private final int screenStartY = 0;

  RenderPanel() {
    YELLOW = new Color(YELLOW_RGB);
    GREEN = new Color(GREEN_RGB);
  }

  /** this method paint the game */
  public void paintComponent(Graphics gComponent) {
    if (Snake.snake.started()) {
      super.paintComponent(gComponent);
      gComponent.setColor(YELLOW);
      /** Fill all window */
      gComponent.fillRect(screenStartX, screenStartY, Snake.windowX, Snake.windowY);
      Snake snake = Snake.snake;
      gComponent.setColor(GREEN);

      /** fill all snake parts */
      for (Point point : snake.getSnakeParts()) {
        gComponent.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
        gComponent.setColor(YELLOW);
        gComponent.fillRect(point.x * Snake.SCALE + 5, point.y * Snake.SCALE + 5, Snake.SCALE - 10,
            Snake.SCALE - 10);
        gComponent.setColor(GREEN);
      }

      /** fill head */
      gComponent.fillRect(snake.getHeadX() * Snake.SCALE, snake.getHeadY() * Snake.SCALE,
          Snake.SCALE, Snake.SCALE);
      /** fill the cherry */
      gComponent.setColor(Color.RED);
      if (Snake.snake.getTailLength() < 379)
        gComponent.fillRect(snake.getCherryX() * Snake.SCALE, snake.getCherryY() * Snake.SCALE,
            Snake.SCALE, Snake.SCALE);
      String string = "Score " + snake.getScore() + ", Length " + snake.getTailLength();
      gComponent.setColor(Color.BLACK);
      gComponent.drawString(string, (int) getWidth() / 2 - string.length() / 2 -1, 30);
    }
  }

}
