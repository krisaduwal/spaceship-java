import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public final int GAME_WIDTH =1000;
    public final int GAME_HEIGHT = 600;

    private float SPEED;
    Rocket rocket;
    Timer timer;

    public String movement = "";
    boolean gameStarted = false;
    boolean gameOver = false;
    JButton startBtn = new JButton("START");
    Image image;

    int score;

    int highscore;
    Timer scoreTimer;

    File scoreFile;
    FileReader fileReader;
    FileWriter fileWriter;
    JLabel gameName;
    private ArrayList<Meteor> meteors; // ArrayList to store multiple Meteor objects
    private Timer meteorTimer; // Timer to trigger meteor creation

    private int meteorDelay;
    private Random random; // Random number generator



    public void getHighScore() throws IOException {
        scoreFile = new File("score.txt");
        BufferedReader reader =new BufferedReader(new FileReader(scoreFile));
        String Int_line;
        Int_line = reader.readLine();
        int value = Integer.parseInt(Int_line);
        highscore = value;
        System.out.println(highscore);
    }
    GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setVisible(true);
        this.setLayout(null);

        gameName = new JLabel("SPACE SHIP CRASHER");
        gameName.setForeground(Color.WHITE);
        gameName.setFont(new Font("Times New Roman",Font.PLAIN,50));
        gameName.setBounds(250,200,600,30);
        this.add(gameName);
        this.setFocusable(true);
        this.setBackground(Color.black);
        startBtn.setBounds(400,300,200,30);
        startBtn.setBackground(Color.GRAY);
        startBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        this.add(startBtn);


    }

    private void createMeteor() {
        //Increase the speed of meteor
        if(SPEED < 15)
        {
            SPEED += 0.1;
        }
        int x = GAME_WIDTH; // Set the initial x-coordinate at the right edge of the panel
        int y = random.nextInt(GAME_HEIGHT - Meteor.Height); // Set the initial y-coordinate randomly within the panel height
        float speed = this.SPEED; // Set the speed for the meteor
        Meteor meteor = new Meteor(x, y, speed); // Create a new Meteor object
        meteors.add(meteor); // Add the meteor to the ArrayList
        y = random.nextInt(GAME_HEIGHT - Meteor.Height);
        meteor = new Meteor(x, y, speed);
        meteors.add(meteor);
        y = random.nextInt(GAME_HEIGHT - Meteor.Height);
        meteor = new Meteor(x, y, speed);
        meteors.add(meteor);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        image = new ImageIcon("../Images/rocket.png").getImage();
        if(gameStarted)
        {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Times New Roman",Font.PLAIN,20));
            g.drawString("High Score = " + highscore,GAME_WIDTH - 150,30);
            g.drawString("Score = " + score,GAME_WIDTH - 150,60);
            rocket.draw(g);
            for (Meteor meteor : meteors) {
                meteor.draw(g);
            }

        }else if(gameOver){
            System.out.println("game over");
            g.setFont(new Font("Times New Roman",Font.PLAIN,50));
            g.setColor(Color.RED);
//            g.drawString("Score : " + score, width-150, 30);
            g.drawString("Game Over! ",GAME_WIDTH/3 + 50,GAME_HEIGHT/2 - 150);
            g.drawString("HIGH SCORE: " + highscore, GAME_WIDTH/3 , GAME_HEIGHT/2 -100);
            g.drawString("SCORE: " + score, GAME_WIDTH/3 +50, GAME_HEIGHT/2 -50);
            startBtn.setLabel("PLAY AGAIN");
            startBtn.setVisible(true);
        }

    }
    public void startGame() throws IOException {
        getHighScore();
        meteorDelay = 2000;
        score = 0;
        scoreTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score++;
                System.out.println("score:" + score);
            }
        });
        scoreTimer.start();
        this.SPEED = 3f;
        gameName.setVisible(false);
        startBtn.setVisible(false);
        timer = new Timer(10,this);
        timer.start();
        rocket = new Rocket();
        gameStarted = true;
        gameOver = false;
        meteors = new ArrayList<>(); // Create an ArrayList to store Meteor objects
        random = new Random();
        meteorTimer = new Timer(meteorDelay, e -> createMeteor());
        meteorTimer.start();
        this.repaint();
        this.addKeyListener(this);


    }

    public boolean collisionDetection(){
        if(rocket.x < 0)
        {
            System.out.println("x < 0");

            return true;
        }
        if(rocket.x > GAME_WIDTH)
        {
            System.out.println("x > 0");

            return true;

        }
        if(rocket.y < 0)
        {
            System.out.println("y < 0");

            return true;

        }
        if(rocket.y > GAME_HEIGHT)
        {
            System.out.println("y > 0");

            return true;

        }
        return  false;
    }
    public void update() {

        // Check collision between rocket and meteors
        for (Meteor meteor : meteors) {
            if (rocket.getBounds().intersects(meteor.getBounds())) {

                handleCollision();
                break; // If there's a collision, no need to check for further collisions
            }
        }

        // Existing code...
    }

    private void handleCollision() {
        gameOver();
//        System.out.println("Rocket collided with a meteor!");
    }

    public void gameOver()
    {
        gameStarted = false;
        gameOver = true;
        timer.stop();
        scoreTimer.stop();
        meteorTimer.stop();
        setHighscore();

    }

    public  void setHighscore(){
        if(score > highscore)
        {
            try {
                scoreFile.delete();
                scoreFile.createNewFile();
                fileWriter = new FileWriter(scoreFile);
                fileWriter.write(score + "");
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(gameStarted)
        {
            // Update the position of each meteor in the ArrayList
            for (Meteor meteor : meteors) {
                meteor.update();
            }
            // Remove meteors that have gone off the screen
            meteors.removeIf(meteor -> meteor.x + Meteor.Width< 0);
            update();
//            if(meteorDelay>500){
//                meteorDelay--;
//                meteorTimer.setDelay(meteorDelay);
//            }
        }






        if(movement == "up")
        {
            if(rocket.y - 10 < 0)
            {
                rocket.y = rocket.y;
            }else{
                rocket.y -=10;

            }
        }

        if(movement == "down")
        {
            if(rocket.y + rocket.HEIGHT + 10 > GAME_HEIGHT)
            {

                rocket.y = rocket.y;
            }else{
                rocket.y +=10;

            }
        }

        if(movement == "left")
        {
            if(rocket.x - 10 < 0)
            {
                rocket.x = rocket.x;
            }else{
                rocket.x -=10;
            }
        }

        if(movement == "right")
        {
            if(rocket.x + rocket.WIDTH+ 10 > GAME_WIDTH)
            {
                rocket.x = rocket.x;
            }else{
                rocket.x += 10;

            }
        }
        this.repaint();


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            movement = "up";


        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {

            movement = "down";

        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {

            movement = "left";

        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {

            movement = "right";


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        movement = "";
    }
}
