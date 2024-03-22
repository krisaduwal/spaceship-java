import javax.swing.*;
import java.io.IOException;

public class GameFrame  extends JFrame {

    GameFrame() throws IOException {
        this.add(new GamePanel());
        this.pack();
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Spaceship");
        this.setVisible(true);
    }
}
