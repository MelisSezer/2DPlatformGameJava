import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class PinkDoor extends JFrame implements ActionListener, Serializable{
    GamePanel gp;
    Player p;
    boolean gameEnded = false;


    public PinkDoor(GamePanel gp, Player p) {
        this.gp = gp;
        this.p = p;
    }

    public void gameEnds(){
        if(p.x>0 && p.x<30 && p.y>30 && p.y<80 && !gameEnded){
            gameEnded = true;
            JFrame parent = new JFrame("Win");
            parent.setSize(400,400);
            parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel l=new JLabel("You Win! Your Score is: " + gp.score);
            parent.add(l);
            parent.setLocation(600,200);
            parent.setVisible(true);

        }
    }

    public void draw(Graphics2D g2){
      g2.setColor(Color.PINK);
      g2.fillRect(0, 30, 30, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //
    }
    public void update(){
        gameEnds();
    }
}
