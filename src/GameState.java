import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private Player player;
    private Spawner spawner;
    private ArrayList<Bullet> bullets;
    private MovingPlatform movingPlatform;
    private PinkDoor pinkDoor;
    private ArrayList<Monster> monsters;
    private ArrayList<Coin> coins;
    private int score;
    // Other game state variables...

    public GameState(Player player, ArrayList<Monster> monsters, ArrayList<Coin> coins, int score, Spawner spawner, MovingPlatform movingPlatform, PinkDoor pinkDoor,ArrayList<Bullet> bullets) {
        this.player = player;
        this.monsters = monsters;
        this.coins = coins;
        this.score = score;
        this.spawner = spawner;
        this.movingPlatform =movingPlatform;
        this.pinkDoor = pinkDoor;
        this.bullets = bullets;
        // Initialize other variables...
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public MovingPlatform getMovingPlatform() {
        return movingPlatform;
    }

    public void setMovingPlatform(MovingPlatform movingPlatform) {
        this.movingPlatform = movingPlatform;
    }

    public PinkDoor getPinkDoor() {
        return pinkDoor;
    }

    public void setPinkDoor(PinkDoor pinkDoor) {
        this.pinkDoor = pinkDoor;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public void setCoins(ArrayList<Coin> coins) {
        this.coins = coins;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    // Getters and setters for player, monsters, coins, score...
}
