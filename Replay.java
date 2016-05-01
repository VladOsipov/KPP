package Snake;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Replay {

  private ArrayList<Integer> savedDirections = new ArrayList<Integer>();
  private ArrayList<Point> savedCherry = new ArrayList<Point>();

  /**Save direction and cherry location to file*/
  public void save() {
    try {
      PrintWriter dirReader = new PrintWriter(new FileOutputStream("1.txt"));
      PrintWriter chReader = new PrintWriter(new FileOutputStream("2.txt"));

      /**Write to file directions*/
      for (Integer direction : savedDirections) {
        dirReader.println(direction);
      }

      /**Write to file cherry locations*/
      for (Point location : savedCherry) {
        chReader.println((int) location.getX());
        chReader.println((int) location.getY());
      }

      dirReader.close();
      chReader.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  /**Fill lists with direction and cherry location from file*/
  public void fillList() {
    savedDirections.clear();
    savedCherry.clear();
    try {
      BufferedReader dirReader = new BufferedReader(new FileReader("1.txt"));
      BufferedReader chReader = new BufferedReader(new FileReader("2.txt"));
      String direction = dirReader.readLine();
      while (direction != null) {
        savedDirections.add(Integer.parseInt(direction));
        direction = dirReader.readLine();
      }
      String location = chReader.readLine();
      while (location != null) {
        Point point = new Point();
        point.x = Integer.parseInt(location);
        location = chReader.readLine();
        point.y = Integer.parseInt(location);
        savedCherry.add(point);
        location = chReader.readLine();
      }
      dirReader.close();
      chReader.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void addDirection(int dir) {
    savedDirections.add(dir);
  }

  public void addCherry(int x, int y) {
    savedCherry.add(new Point(x, y));
  }

  public int getDirection(int index) {
    return savedDirections.get(index);
  }

  public Point getCherry(int index) {
    return savedCherry.get(index);
  }
}
