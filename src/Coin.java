import java.awt.*;
import java.io.Serializable;

public class Coin implements Serializable {
    boolean isVisible = true;
    int x,y;
    GamePanel gp;
    Player p;
    Color color = Color.YELLOW;
    public Coin(GamePanel gp, int x, int y, Player p) {
        this.x = x;
        this.y = y;
        this.gp = gp;
        this.p = p;
    }
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        if(isVisible)
            g2.fillOval(x, y, 5, 5);
        else {
            g2.setColor(Color.BLACK);
            g2.fillOval(x, y, 5, 5);
        }

    }
    public void update() {
        if(isVisible && p.getX()<x && p.getX()+16>x  && !(p.getY()+16<y || p.getY()>y+5)) {
            gp.score+=5;
            isVisible = false;
        }

    }
}
