import java.awt.*;
import java.io.Serializable;

public class Ground implements Serializable {
    int x;
    int y;
    int width;
    int height;
    String colorGround;
    Rectangle hitbox;
    GamePanel gp;

    public Ground(GamePanel gp, int x, int y,int width, int height,String colorGround){
        this.x=x;
        this.y=y;
        this.width= width;
        this.height=height;
        this.colorGround = colorGround;
        this.gp = gp;
        hitbox = new Rectangle(x,y,width,height);

    }

    public void draw(Graphics2D g2){
        if(this.colorGround.equals("red"))
            g2.setColor(Color.RED);
        else if(this.colorGround.equals("blue"))
            g2.setColor(Color.BLUE);
        else if (this.colorGround.equals(("green"))) {
            g2.setColor(Color.GREEN);
        } else
            g2.setColor(Color.GRAY);
        g2.fillRect(x+1,y+1,width-2,height-2); //ground
    }
}
