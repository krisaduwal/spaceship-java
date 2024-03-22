import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Rocket {
    public int WIDTH = 100;
    public int HEIGHT = 100;

    public int velocityX = 0;
    public int x;
    public int y;

    Image image;
    URL imageURL;
    ImageIcon imageIcon;



    Rocket() throws IOException {
        this.x = 10;
        this.y = 300;
//        this.image = new ImageIcon("../Images/rocket.png").getImage();
         imageURL = Rocket.class.getClassLoader().getResource("rocket.png");
     image = new ImageIcon(imageURL).getImage();
    }


    public void draw(Graphics g)
    {
        g.drawImage(image,x,y,null);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
    public void move(String movement){
        switch (movement)
        {
            case "up":
                this.y = this.y - 5;
                break;

            case "down":
                this.y = this.y + 5;
                break;

            case "left":
                this.x = this.x -5 ;
                break;

            case "right":
                this.x = this.x + 5;
                break;
        }
    }

}
