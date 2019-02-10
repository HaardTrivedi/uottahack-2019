import Dim;
import GetColor;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.fasterxml.jackson.databind.ObjectMapper;

protected class JavaService {
  Dim DimensionService = Dim;

  GetColor GetColour = GetColor;

  public int[] getDimension() {
    return Dimension.getDimensions();
  }

  public int[] getColour(String img) {
    GetColor color = new GetColour(img);
    Color[] colours = color.getColour();
    int i = 75;
    Color green = new Color(253, 39, 30);
    Point p = color.predictLaser(colours, color.width, color.height, green2, i);
    return new int[] { p.x, p.y };
  }

  public int[] getDimensions() {
    Dim dim = new DimensionService();
    Point p = dim.getDimensions();
    return new int[] { p.x, p.y };
  }
}

public class CatService extends ReactContextBaseJavaModule {
  JavaService service = new JavaService();

  public CatService(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "CatService";
  }

  @ReactMethod
  public void getDimension(Promise promise) {
    try {
      int[] dim = service.getDimension();
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(dim);
      promise.resolve(json);
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getColour(String img, Promise promise) {
    try {
      int[] colors = service.getColour(img);
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(colors);
      promise.resolve(json);
    } catch (Exception e) {
      promise.reject(e);
    }
  }
}
