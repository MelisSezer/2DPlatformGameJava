import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        if (args.length > 0) {
            // Kaydedilmiş oyunu yükle
            try {
                FileInputStream fis = new FileInputStream(args[0]);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // Kaydedilmiş durumu yükle
                GameState gameState = (GameState) ois.readObject();

                ois.close();
                fis.close();

                // Oyun paneline durumu uygula
               // gamePanel.loadGameState(gameState);
                //gamePanel.setPaused(true); // Oyunu duraklatılmış olarak başlat

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("AtesveSu");

        //GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
    }
}