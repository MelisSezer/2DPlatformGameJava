import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

public class KeyHandler implements KeyListener, Serializable {
    public boolean upPressed, downPressed, leftPressed, rightPressed, onePressed, twoPressed, WPressed, threePressed, spacePressed;
    public boolean pausePressed,savePressed = false;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
       // if(code == KeyEvent.VK_W){
         //   WPressed = true;
       // }
        if(code == KeyEvent.VK_P) {
            pausePressed = !pausePressed;
        }
        if(code == KeyEvent.VK_K) {
            savePressed = true;
        }
        if(code == KeyEvent.VK_1){
            onePressed = true;
        }
        if(code == KeyEvent.VK_2){
            twoPressed = true;
        }
        if(code == KeyEvent.VK_3){
            threePressed = true;
        }
        if(code == KeyEvent.VK_UP){
            upPressed = true;
        }
        //if(code == KeyEvent.VK_DOWN){
        //    downPressed = true;
        //}
        if(code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        // if(code == KeyEvent.VK_W){
        //   WPressed = false;
        // }
        if (code == KeyEvent.VK_K) {
            savePressed = false;
        }
        if(code == KeyEvent.VK_1){
            onePressed = false;
        }
        if(code == KeyEvent.VK_2){
            twoPressed = false;
        }
        if(code == KeyEvent.VK_3){
            threePressed = false;
        }
        if(code == KeyEvent.VK_UP){
            upPressed = false;
        }
       // if(code == KeyEvent.VK_DOWN){
        //    downPressed = false;
       // }
        if(code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }
    }
}
