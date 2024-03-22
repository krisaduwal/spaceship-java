import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Meteor {
    public static int Width = 100;
    public static int Height = 100;
    public int x;
    public int y;

    public float speed;
    Image image;
    URL url;
    Meteor(int x, int y, float speed)
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
        url = Meteor.class.getClassLoader().getResource("meteor.png");
        image = new ImageIcon(url).getImage();
    }

    public void update() {
        x -= speed; // Move the meteor horizontally to the left

    }

    public void draw(Graphics g)
    {

        g.drawImage(image,x,y,null);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
