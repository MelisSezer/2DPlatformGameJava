import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity  {
    public int x, y;
    public int speed;
    public BufferedImage fireboy, watergirl, magenta;
    public String direction;
    public String color;
    public Rectangle solidArea; //hitbox
    public boolean collisionOn = false;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
