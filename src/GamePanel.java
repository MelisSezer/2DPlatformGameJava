import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable, Serializable {
    //Game screen
    //Screen settings
    int score=0;
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight =750; //tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    transient Thread gameThread;
    Player player = new Player(this,keyH);
    //Monster monster = new Monster(this);
    //Ground ground = new Ground(this);
    PinkDoor pinkDoor = new PinkDoor(this,player);
    int[] pathX = {0, 80, 0,80,0}; // x coordinates of waypoints
    int[] pathY = {730, 650, 570,650,730}; // y coordinates of waypoints

    MovingPlatform movingPlatform = new MovingPlatform(pathX, pathY, 1);
    Spawner spawner = new Spawner(this, 15000);
    ArrayList<Ground> walls = new ArrayList<>();
    ArrayList<Monster> monsters = new ArrayList<>();
    ArrayList<Coin> coins = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 10));
        scoreLabel.setBounds(10, 10, 30, 30);

        this.add(scoreLabel);

        makeWalls();
        makeMonsters();
        makeCoins();
    }

    public void startGameThread(){
        //Basically passing this class (GamePanel to this constructor)
        gameThread = new Thread(this);
        //Automatically calls run method
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS; // draw the screen every 0.016 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {
            // 1) update the game
            if(!keyH.pausePressed)
                update();
            else{
                if (keyH.savePressed) {
                    saveGameState();
                    JOptionPane.showMessageDialog(null, "Game saved successfully!");
                    keyH.savePressed = false;
                }

            }
            // 2) draw the screen with updated info
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime<0)
                    remainingTime = 0;

                Thread.sleep((long)remainingTime);

                nextDrawTime +=drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void saveGameState() {
        try {
            FileOutputStream fileOut = new FileOutputStream("game_state.sav");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            // Example of saving the player and monsters; adapt as necessary
            out.writeObject(player);
            out.writeObject(monsters);
            out.writeObject(coins);
            out.writeObject(bullets);
            out.writeObject(score);
            out.writeObject(walls);
            out.writeObject(movingPlatform);
            out.writeObject(pinkDoor);
            out.writeObject(spawner);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void collisionCheckForMonsters() {
        int hitboxOffset = 5; // Reduce the height of the hitbox by this amount from the top and bottom
        for (Monster monster : monsters) {
            // Create a smaller hitbox for horizontal collision detection
            Rectangle smallerHitbox = new Rectangle(
                    monster.x,
                    monster.y + hitboxOffset, // Start the hitbox slightly lower than the monster's top
                    monster.width,
                    monster.height - (2 * hitboxOffset) // Reduce the height from top and bottom
            );

            // Prepare to check the next position based on current direction
            Rectangle nextPosition = new Rectangle(smallerHitbox);
            if (monster.direction.equals("right")) {
                nextPosition.x += monster.speed;
            } else if (monster.direction.equals("left")) {
                nextPosition.x -= monster.speed;
            }

            boolean collisionDetected = false;
            for (Ground wall : walls) {
                if (nextPosition.intersects(wall.hitbox)) {
                    // A horizontal collision is detected, reverse direction
                    collisionDetected = true;
                    break;
                }
            }

            if (collisionDetected) {
                if (monster.direction.equals("right")) {
                    monster.direction = "left";
                } else {
                    monster.direction = "right";
                }
            }
        }
    }



    public void killMonster() {
            Iterator<Monster> monsterIterator = monsters.iterator();

            while (monsterIterator.hasNext()) {
                Monster m = monsterIterator.next();
                Iterator<Bullet> bulletIterator = bullets.iterator(); // Separate iterator for bullets

                while (bulletIterator.hasNext()) {
                    Bullet b = bulletIterator.next();
                    if (m.solidArea.intersects(b.solidArea) && !m.color.equals("orange") ) {
                        b.visible=false;
                        if (b.color.equals(Color.CYAN) && m.color.equals("red")) {
                            m.decreaseHealth();
                            bulletIterator.remove();
                            System.out.println(m.color + "Monster health decreased by " + (b.color.equals(Color.RED) ? "red" : "blue") + "bullet to: " + m.getHealth());
                        } else if (b.color.equals(Color.RED) && m.color.equals("red")) {
                            m.increaseHealth();
                            bulletIterator.remove();
                            System.out.println(m.color + "Monster health increased by " + (b.color.equals(Color.RED) ? "red" : "blue") + "bullet to: " + m.getHealth());
                        } else if (b.color.equals(Color.CYAN) && m.color.equals("blue")) {
                            m.increaseHealth();
                            bulletIterator.remove();
                            System.out.println(m.color + "Monster health increased by " + (b.color.equals(Color.RED) ? "red" : "blue") + "bullet to: " + m.getHealth());
                        } else if (b.color.equals(Color.RED) && m.color.equals("blue")){
                            m.decreaseHealth();
                            bulletIterator.remove();
                            System.out.println(m.color + "Monster health decreased by " + (b.color.equals(Color.RED) ? "red" : "blue") + "bullet to: " + m.getHealth());
                        }
                        if (m.getHealth() <= 0) {
                            monsterIterator.remove();
                            score += 10;
                            break;
                        }
                    }
                }
            }

        for (Ground wall : walls) {
            for (Bullet b : bullets) {
                if (b.solidArea.intersects(wall.hitbox)) {
                    bullets.remove(b);
                    break;

                }
            }
        }
    }
    public void makeWalls(){
        walls.add(new Ground(this,0,730,800,20, "green"));
        walls.add(new Ground(this,160, 650, 700, 20, "gray"));
        walls.add(new Ground(this,180,550,20,40, "gray"));
        walls.add(new Ground(this,100,570,800,20, "gray"));
        walls.add(new Ground(this,700,510,100,70, "gray"));
        walls.add(new Ground(this,0, 460, 250, 20, "gray"));
        walls.add(new Ground(this,320,460,300, 20, "gray"));
        walls.add(new Ground(this,100, 365, 700, 20, "gray"));
        walls.add(new Ground(this,600, 280, 500, 20, "gray"));
        walls.add(new Ground(this,245, 460, 80, 20, "red"));
        walls.add(new Ground(this, 0, 430, 80, 50, "gray"));
        walls.add(new Ground(this, 0, 400, 40, 70, "gray"));
        walls.add(new Ground(this,500, 280, 110, 20, "blue"));
        walls.add(new Ground(this, 300, 280, 230, 20, "gray"));
        walls.add(new Ground(this,550, 230, 40, 20, "gray"));
        walls.add(new Ground(this, 150, 170, 40, 20, "gray")); // First step
        walls.add(new Ground(this, 100, 130, 40, 20, "gray")); // Second step
        walls.add(new Ground(this, 50, 90, 40, 20, "gray")); // Third step
        walls.add(new Ground(this, 200, 210, 40, 20, "gray")); // Fourth step

    }
    public void makeMonsters(){
       // monsters.add(new Monster(this, 0, 510, "red"));
        monsters.add(new Monster(this, 300, 220, "blue"));
        monsters.add(new Monster(this, 150, 400, "red"));
        monsters.add(new Monster(this, 200, 320, "red"));
        monsters.add(new Monster(this, 100, 100, "orange"));
    }
    public void makeCoins() {
        coins.add(new Coin(this, 120, 530,player));
        coins.add(new Coin(this, 240, 530,player));
        coins.add(new Coin(this, 360, 530,player));
        coins.add(new Coin(this, 480, 530,player));
        coins.add(new Coin(this, 730, 480,player));

        coins.add(new Coin(this, 20, 370,player));
        coins.add(new Coin(this, 120, 430,player));
        coins.add(new Coin(this, 240, 430,player));
        coins.add(new Coin(this, 360, 430,player));
        coins.add(new Coin(this, 480, 430,player));

        coins.add(new Coin(this, 120, 330,player));
        coins.add(new Coin(this, 240, 330,player));
        coins.add(new Coin(this, 360, 330,player));
        coins.add(new Coin(this, 480, 330,player));
        coins.add(new Coin(this, 600, 330,player));
        coins.add(new Coin(this, 720, 330,player));

        coins.add(new Coin(this, 220, 180, player));
        coins.add(new Coin(this, 170, 140, player));
        coins.add(new Coin(this, 120, 100,player));
        coins.add(new Coin(this, 70, 60,player));
        coins.add(new Coin(this, 360, 240,player));
        coins.add(new Coin(this, 480, 240,player));
        coins.add(new Coin(this, 565, 200,player));
        coins.add(new Coin(this, 690, 240,player));
    }
    public void setDefaultValuesCoin() {
        for (Coin coin:coins) {
            coin.isVisible = true;
        }
        coins.get(0).x = 120;
        coins.get(0).y = 530;
        coins.get(1).x = 240;
        coins.get(1).y = 530;
        coins.get(2).x = 360;
        coins.get(2).y = 530;
        coins.get(3).x = 480;
        coins.get(3).y = 530;
        coins.get(4).x = 730;
        coins.get(4).y = 480;
        coins.get(5).x = 20;
        coins.get(5).y = 370;
        coins.get(6).x = 120;
        coins.get(6).y = 430;
        coins.get(7).x = 240;
        coins.get(7).y = 430;
        coins.get(8).x = 360;
        coins.get(8).y = 430;
        coins.get(9).x = 480;
        coins.get(9).y = 430;
        coins.get(10).x = 120;
        coins.get(10).y = 330;
        coins.get(11).x = 240;
        coins.get(11).y = 330;
        coins.get(12).x = 360;
        coins.get(12).y = 330;
        coins.get(13).x = 480;
        coins.get(13).y = 330;
        coins.get(14).x = 600;
        coins.get(14).y = 330;
        coins.get(15).x = 720;
        coins.get(15).y = 330;
        coins.get(16).x = 220;
        coins.get(16).y = 180;
        coins.get(17).x = 170;
        coins.get(17).y = 140;
        coins.get(18).x = 120;
        coins.get(18).y = 100;
        coins.get(19).x = 70;
        coins.get(19).y = 60;
        coins.get(20).x = 360;
        coins.get(20).y = 240;
        coins.get(21).x = 480;
        coins.get(21).y = 240;
        coins.get(22).x = 565;
        coins.get(22).y = 200;
        coins.get(23).x = 690;
        coins.get(23).y = 240;
    }
    public void setDefaultValuesMonster() {
        // Clear the list to remove all monsters, including spawned ones
        monsters.clear();
        // Repopulate with the initial set of monsters
        Object[][] positions = {{300, 220, "blue"}, {150, 400, "orange"}, {200, 320, "red"}, {100, 100, "red"}};
        for (Object[] pos : positions) {
            monsters.add(new Monster(this, (int)pos[0], (int)pos[1], (String)pos[2]));
        }
    }

    public void crashMonster() {
        for (Monster monster : monsters) {
            if (player.solidArea.intersects(monster.solidArea)) {
                score = 0;
                player.setDefaultValues();
                setDefaultValuesMonster();
                setDefaultValuesCoin();
                break;
            }
        }
    }
    public void update(){
        movingPlatform.beginFrame();
        movingPlatform.update();
        spawner.update();
        player.update();
        collisionCheckForMonsters();
        for (Monster monster : monsters)
            monster.update();
        for (Coin coin : coins)
            coin.update();
        Bullet bullet = player.fireBullet();
        killMonster();
        if (bullet != null) {
            bullets.add(bullet); // Store the newly created bullet
        }
        for (Bullet b : bullets) {
            b.update(); // Update each bullet's position
        }
        crashMonster();
       // platformAndPlayer();
        pinkDoor.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //Graphics2D extends Graphics
        pinkDoor.draw(g2);
        //g2.setColor(Color.gray);
        //ground.draw(g2);
        for (Coin coin : coins)
            coin.draw(g2);
        for(Ground gr: walls){
            gr.draw(g2);
        }
        player.draw(g2);
        for (Monster monster : monsters)
            monster.draw(g2);
        g2.drawString("Score: " + score, 10, 20);
        for (Bullet b : bullets) {
            b.draw(g2);
        }
        movingPlatform.draw(g2);
        g2.dispose();
    }
}
