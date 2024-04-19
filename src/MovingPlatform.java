import java.awt.*;
import java.io.Serializable;

public class MovingPlatform implements Serializable {
    private int[] pathX;
    private int[] pathY;
    private int currentWaypointIndex = 0;
    private int nextWaypointIndex = 1;
    private int posX;
    private int posY;
    private int prevX;
    private int prevY;
    private float speed; // Adjust speed as needed
    private Rectangle hitbox;

    public MovingPlatform(int[] pathX, int[] pathY,float speed) {
        this.pathX = pathX;
        this.pathY = pathY;
        this.speed = speed;
        this.posX = this.prevX=pathX[0]; // Start at the first waypoint
        this.posY = this.prevY=pathY[0];
        hitbox = new Rectangle(posX, posY, 80, 20);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
    public void beginFrame() {
        prevX = posX;
        prevY = posY;
    }

    public void update() {
            // Move the platform towards the next waypoint
            float deltaX = pathX[nextWaypointIndex] - posX;
            float deltaY = pathY[nextWaypointIndex] - posY;

            // Determine the direction of movement
            float directionX = deltaX > 0 ? 1 : -1; // Move right if deltaX is positive, left otherwise
            float directionY = deltaY > 0 ? 1 : -1; // Move down if deltaY is positive, up otherwise

            // Move the platform in both x and y directions
            posX += directionX * speed;
            posY += directionY * speed;

            // Check if reached the next waypoint
            if (Math.abs(posX - pathX[nextWaypointIndex]) < speed && Math.abs(posY - pathY[nextWaypointIndex]) < speed) {
                currentWaypointIndex = nextWaypointIndex;
                nextWaypointIndex = (nextWaypointIndex + 1) % pathX.length;
            }
            hitbox.x= posX;
            hitbox.y = posY;
        }

        public void draw(Graphics g) {
        // Render the platform sprite or model at its current position
        g.setColor(Color.GRAY);
        g.fillRect(posX, posY, 80, 20);
    }
    public int getMovementDeltaX() {
        return posX - prevX;
    }
    public int getMovementDeltaY() {
        return posY - prevY;
    }

    // Implement collision detection with the player character
    // Add methods to handle player interaction with the platform
}
