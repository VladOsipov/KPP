package Snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class RenderPanel extends JPanel {

  static int i = 0, flag = 0;
  private ArrayList<Color> colors;


  public void paintComponent(Graphics g) {
    if (Snake.started) {


      createColorList();

      super.paintComponent(g);
      g.setColor(new Color(16646040));
      g.fillRect(0, 0, Snake.windowX, Snake.windowY);
      Snake snake = Snake.snake;


      g.setColor(new Color(10026908));
      for (Point point : snake.snakeParts) {
        g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
        g.setColor(new Color(16646040));
        g.fillRect(point.x * Snake.SCALE + 5, point.y * Snake.SCALE + 5, Snake.SCALE - 10,
            Snake.SCALE - 10);
        g.setColor(new Color(10026908));

      }

      g.fillRect(snake.head.x * Snake.SCALE, snake.head.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
      g.setColor(getColor());

      if (Snake.snake.tailLength < 379)
        g.fillRect(snake.cherry.x * Snake.SCALE, snake.cherry.y * Snake.SCALE, Snake.SCALE,
            Snake.SCALE);
      String string = "Score " + snake.score + ", Length " + snake.tailLength;
      g.setColor(Color.BLACK);
      g.drawString(string, (int) getWidth() / 2 - string.length() / 2, 30);
    }
  }

  public void createColorList() {
    colors = new ArrayList<Color>();
    for (int i = 16750700; i < 16750831; i += 5)
      colors.add(new Color(i));
  }

  public Color getColor() {
    if (flag == 0)
      i++;
    else
      i--;
    if (i == colors.size() - 1)
      flag = 1;
    if (i == 0)
      flag = 0;
    return colors.get(i);
  }
}
