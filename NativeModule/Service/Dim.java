import com.sun.javafx.scene.paint.GradientUtils.Point;

public class Dim {
  DisplayMetrics displayMetrics = new DisplayMetrics();

  public Window() {
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
  }

  private int GetHeight() {
    return displayMetrics.getWidth();
  }

  private int GetWidth() {
    return displayMetrics.getHeight();
  }

  public Point getDimensions() {
    int width = GetWidth();
    int height = GetHeight();
    Point p = new Point(width, height);
    return dim;
  }
}
