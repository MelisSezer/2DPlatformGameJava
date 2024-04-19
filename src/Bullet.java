import java.awt.*;
import java.io.Serializable;

public class Bullet implements Serializable {
    KeyHandler keyH;
    GamePanel gp;
    Player p;
    int x;
    int y;
    Color color ;
    int dire;
    boolean visible = true;
    Rectangle solidArea;
    public Bullet(GamePanel gp, Player p, KeyHandler keyH, int dire){
        this.gp = gp;
        this.p = p;
        this.x = dire ==1 ? 30 + p.x : p.x + 15;
        solidArea = new Rectangle(x, y+30, 10, 10);
        this.y = p.y;
        this.keyH = keyH;
        this.dire = dire;
    }
    public void draw(Graphics2D g2) {
        if(visible) {
            g2.setColor(p.color.equals("red") ? Color.RED : Color.CYAN);
            g2.fillOval(x, y + 20, 4, 4);
        }

    }
    public void update() {
        x += (p.speed+5) * dire ;
        if(p.color.equals("blue"))
            color = Color.CYAN;
        if (p.color.equals("red"))
            color = Color.RED;
        solidArea = new Rectangle(x, y+30, 10, 10);
    }
}

