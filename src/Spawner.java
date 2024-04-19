import java.io.Serializable;
import java.util.Random;
import java.util.Timer;

public class Spawner implements Serializable {
    private final GamePanel gp;
    private final Random random;
    private final Timer timer;
    private final int spawnInterval; // Canavar üretme aralığı (milisaniye cinsinden)
    private long lastSpawnTime; // Time of the last spawn

    public Spawner(GamePanel gp, int spawnInterval) {
        this.gp = gp;
        this.spawnInterval = spawnInterval;
        this.random = new Random();
        this.timer = new Timer();
        this.lastSpawnTime = System.currentTimeMillis(); // Initialize last spawn time
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSpawnTime;

        if (elapsedTime >= spawnInterval) {
            spawnMonster();
            lastSpawnTime = currentTime; // Reset the last spawn time
        }
    }

    private void spawnMonster() {
        // Rastgele bir konum seçimi yap
        int x = 15; // Ekran genişliği içinde rastgele x koordinatı
        int y = 50; // Ekran yüksekliği içinde rastgele y koordinatı

        // Rastgele seçilen renkte bir canavar oluştur ve oyun alanına ekle
        String color = getRandomColor();
        Monster monster = new Monster(gp, x, y, color);
        gp.monsters.add(monster);
    }

    private String getRandomColor() {
        int r = random.nextInt(3);
        if(r == 0)
            return "orange";
        else if(r == 1)
            return "blue";
        else
            return "red";
    }
}
