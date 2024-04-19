import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Monster extends Entity implements Serializable {
    GamePanel gp;
    int x;
    int y;
    final int width = 30;
    final int height = 30;
    int speed=1;
    int health;
    String color;
    String direction; //default direction
   // Rectangle solidArea; //hitbox
    final double GRAVITY = 2;

    public Monster(GamePanel gp, int x, int y, String color) {
        Random generator = new Random();
        direction = generator.nextInt(2)==1 ? "left": "right";
        System.out.println(direction + " " + color);
        this.gp = gp;
        this.x = x;
        this.y = y;
        health= 3;
        this.color = color;
        solidArea = new Rectangle(x, y,30,30);
    }
    public void decreaseHealth() {
        this.health--;
    }

    public void increaseHealth() {
        this.health++;
    }

    public int getHealth() {
        return health;
    }

    public void update() {
        // in Java upper left corner is 0,0
        gravity();
        if(direction.equals("right")) {
            x += speed;
            solidArea.x += speed;
            direction = "right";
            if(x==700)
                direction = "left";
        }
        if(direction.equals("left")) {
            x -= speed;
            solidArea.x -= speed;
            direction = "left";
            if(x==0)
                direction = "right";
        }
    }

    public void draw(Graphics2D g2) {
        if (color.equals("red"))
            g2.setColor(Color.RED);
        else if (color.equals("blue"))
            g2.setColor(Color.BLUE);
        else if (color.equals("orange"))
            g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, height);
    }
    public boolean onGround(){
        for (Ground wall: gp.walls) {
            if (solidArea.intersects(wall.hitbox)) {
                return true;
            }
        }
        return false;
    }

    public void gravity() {
        if (!onGround()) {
            solidArea.y += GRAVITY;
            y += GRAVITY;
        }
    }

}
