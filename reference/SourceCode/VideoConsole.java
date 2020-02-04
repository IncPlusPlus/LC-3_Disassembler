import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

public class VideoConsole extends JPanel implements javax.swing.event.TableModelListener
{
  private static final long serialVersionUID = 106L;
  private BufferedImage image;
  private static final int START = 49152;
  private static final int NROWS = 128;
  private static final int NCOLS = 124;
  private static final int END = 65024;
  private static final int SCALING = 2;
  private static final int WIDTH = 256;
  private static final int HEIGHT = 248;
  private static Color CUR_COLOR = Color.white;
  private Machine mac;
  
  public VideoConsole(Machine paramMachine)
  {
    Dimension localDimension = new Dimension(256, 248);
    setPreferredSize(localDimension);
    setMinimumSize(localDimension);
    setMaximumSize(localDimension);
    mac = paramMachine;
    image = new BufferedImage(256, 248, 9);
    
    Graphics2D localGraphics2D = image.createGraphics();
    localGraphics2D.setColor(Color.black);
    localGraphics2D.fillRect(0, 0, 256, 248);
  }
  
  public void reset() {
    Graphics2D localGraphics2D = image.createGraphics();
    localGraphics2D.setColor(Color.black);
    localGraphics2D.fillRect(0, 0, 256, 248);
    repaint();
  }
  
  public void tableChanged(TableModelEvent paramTableModelEvent) {
    int i = paramTableModelEvent.getFirstRow();
    int j = paramTableModelEvent.getLastRow();
    if ((i == 0) && (j == 65534)) {
      reset();
    } else { if ((i < 49152) || (i > 65024))
      {
        return;
      }
      int k = 2;
      int m = i - 49152;
      int n = m / 128 * k;
      int i1 = m % 128 * k;
      int i2 = mac.readMemory(i).convertToRGB();
      for (int i3 = 0; i3 < k; i3++) {
        for (int i4 = 0; i4 < k; i4++) {
          image.setRGB(i1 + i4, n + i3, i2);
        }
      }
      repaint(i1, n, k, k);
    }
  }
  
  public void paintComponent(java.awt.Graphics paramGraphics) {
    super.paintComponent(paramGraphics);
    
    Graphics2D localGraphics2D1 = (Graphics2D)paramGraphics;
    if (image == null)
    {
      int i = getWidth();
      int j = getHeight();
      image = ((BufferedImage)createImage(i, j));
      Graphics2D localGraphics2D2 = image.createGraphics();
      localGraphics2D2.setColor(Color.white);
      localGraphics2D2.fillRect(0, 0, i, j);
    }
    localGraphics2D1.drawImage(image, null, 0, 0);
  }
}
