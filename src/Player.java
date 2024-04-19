import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public class Player extends Entity implements Serializable {
    private long lastBulletTime = 0;
    private long bulletDelay = 200;
    GamePanel gp;
    KeyHandler keyH;
    double GRAVITY = 2;
    int jumpspeed;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        solidArea = new Rectangle(x, y, 40, 42);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 250;
        y = 600; //512
        solidArea.x = x;
        solidArea.y = y;
        speed = 4;
        color = "red";
        direction = "right";
        jumpspeed = 15;
    }

    public void getPlayerImage() {
        try {
            fireboy = ImageIO.read(getClass().getResourceAsStream("New Piskel-1.png (1).png"));
            watergirl = ImageIO.read(getClass().getResourceAsStream("New Piskel-1.png.png"));
            magenta = ImageIO.read(getClass().getResourceAsStream("New Piskel-3.png.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        // in Java upper left corner is 0,0
        touchRed();
        touchBlue();
        touchGreen();
        CollisionLeftCheck();
        CollisionRightCheck();
        playerOnMovingPlatform();
        //if collesion yoksa gravity çağır
        gravity();
        updatePositionWithPlatform(gp.movingPlatform);
        // ışınlandığı için harta oluyor ışınlanma yolunds obstacle var  mı diye bbak
        if (keyH.upPressed)
            jump();

        if (keyH.onePressed) {
            color = "red";
        }
        if (keyH.twoPressed) {
            color = "blue";
        }
        if (keyH.threePressed) {
            color = "magenta";
        }
        if (keyH.leftPressed && x > 0 - 10) {
            direction = "left";
            //speed = 1;
            x -= speed;
            solidArea.x -= speed;
            // }
            // }
        }
        if (keyH.rightPressed && x < 800 - gp.tileSize - 20) {
            direction = "right";
            x += speed;
            // solidArea.x += speed;
        }
        solidArea.x = x;
        solidArea.y = y;
        collisionOn = false;
    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.BLUE);
        //g2.fillRect(x,y,gp.tileSize,gp.tileSize);
        BufferedImage image = null;
        switch (color) {
            case "red":
                image = fireboy;
                break;
            case "blue":
                image = watergirl;
                break;
            case "magenta":
                image = magenta;
                break;
        }
        g2.drawImage(image, x, y, 42, 42, null);
        g2.drawRect(x, y, 40, 42); // Draw player with 16x16 pixels

    }
    public void gravity() {

        if(!onGround() && !onMovingPlatform(gp.movingPlatform)) {
            solidArea.y += GRAVITY;
            y += GRAVITY;
        }

    }

    public boolean onGround() {
        // if((y>510 && y<515 && x<680)  || (y>465 && y<470 && x>680 && x<800) || (y>415 && y<418 && x<600 && x>65) || (x>75 && y<330 && y>315) || (x>230 && y>230 && y<235) || (x>20 && x<65 && y>380 && y<385) || (x>-20 && x<20 && y>350 && y<355) || x>515 && x<560 && y<200 && y>190)
        int playerBottomY = this.y + this.solidArea.height; // Bottom Y position of the player
        for (Ground wall : gp.walls) {
            // Top Y position of the wall
            int wallTopY = wall.hitbox.y;

            // Check if the player is above the wall and within a small threshold
            final int threshold = 2; // Example threshold value for "close enough to the ground"
            if (playerBottomY > wallTopY - threshold && playerBottomY <= wallTopY) {
                // Horizontal overlap check
                int playerLeftX = this.x;
                int playerRightX = this.x + this.solidArea.width;
                int wallLeftX = wall.hitbox.x;
                int wallRightX = wall.hitbox.x + wall.hitbox.width;

                // Check if any part of the player's bottom edge is above any part of the wall's top edge
                if ((playerLeftX < wallRightX && playerRightX > wallLeftX)) {
                    return true; // The player is considered to be on the ground
                }
            }
        }
        return false;
    }
    public void playerOnMovingPlatform() {
        // This method assumes that 'movingPlatform' is accessible within this context
        MovingPlatform platform = gp.movingPlatform; // Example, adjust according to your structure

        // Calculate the player's bottom position
        int playerBottomY = y + solidArea.height;
        // Calculate the platform's top position
        int platformTopY = platform.getHitbox().y;

        // Check if the player is right above the platform
        if (playerBottomY <= platformTopY && playerBottomY > platformTopY - GRAVITY &&
                x + solidArea.width > platform.getHitbox().x &&
                x < platform.getHitbox().x + platform.getHitbox().width) {

            // Stick the player to the platform
            y = platformTopY - solidArea.height; // Adjust player's Y to be exactly on top of the platform
            solidArea.y = y;

            // Make the player move with the platform
            x += platform.getMovementDeltaX();
            solidArea.x = x;
        }
    }

    private boolean onMovingPlatform(MovingPlatform movingPlatform) {
        Rectangle potentialPosition = new Rectangle(solidArea.x, solidArea.y + 1, solidArea.width, solidArea.height);
        return movingPlatform.getHitbox().intersects(potentialPosition);
    }
    public void updatePositionWithPlatform(MovingPlatform platform) {
        // Check if the player is on the platform
        if (onMovingPlatform(platform)) {
            // If on the platform, adjust the player's position according to the platform's movement
            if(platform.getMovementDeltaX()>0)
                this.x += platform.getMovementDeltaX()-1;
            else
                this.x += platform.getMovementDeltaX()+1;
            this.y += platform.getMovementDeltaY();
            System.out.print("X:"+platform.getMovementDeltaX() + "X ccor:"+x);
            System.out.print("Y:"+platform.getMovementDeltaY() + "Y ccor:"+y);
            System.out.println("Platform deltaX: " + platform.getMovementDeltaX() + " deltaY: " + platform.getMovementDeltaY());
            System.out.println("Platform x: " + platform.getHitbox().x + " y: " + platform.getHitbox().y);
            // Update the player's solidArea position
            this.solidArea.x = this.x;
            this.solidArea.y = this.y;
        }
    }


    public void jump() {
        if (keyH.upPressed && (onGround()) || onMovingPlatform(gp.movingPlatform)){
            int baseJumpHeight = 80; // Base jump speed
            int jumpHeight = baseJumpHeight; // Initialize jumpHeight to the base jump speed

            // Apply the color effect before checking for obstacles
            if (color.equals("magenta")) {
                jumpHeight *= 2; // If magenta, attempt to double the jump height
            }

            // Simulate the area the player will move through during the jump
            Rectangle jumpArea = new Rectangle(x, y - jumpHeight, solidArea.width, jumpHeight);

            // Check for ceilings or obstacles above before finalizing the jump height
            for (Ground wall : gp.walls) {
                if (jumpArea.intersects(wall.hitbox)) {
                    // Adjust jumpHeight based on the first obstacle encountered
                    int ceiling = wall.hitbox.y + wall.hitbox.height;
                    int availableJumpHeight = y - ceiling;

                    // Ensure the jump doesn't exceed available space and doesn't become negative
                    jumpHeight = Math.max(0, Math.min(jumpHeight, availableJumpHeight));
                    break; // No need to check further obstacles since one ceiling blocks the jump
                }
            }

            // Apply the adjusted jump
            y -= jumpHeight; // Move the player up by the adjusted jumpHeight
            solidArea.y -= jumpHeight; // Adjust the collision area accordingly

            direction = "up"; // Set the direction to up
        }
    }

    public void CollisionRightCheck() {
        // Assuming 'dx' is the desired change in position (from key input) and 'speed' is your movement speed
        int dx = 0;
        if (keyH.leftPressed) dx += speed;
        // Predict the player's next position based on current movement direction
        Rectangle nextPosition = new Rectangle(solidArea.x + dx, solidArea.y, solidArea.width, solidArea.height);

        // Initially assume the player can move both directions
        boolean canMove = true;

        // Check for collisions with all walls
        for (Ground wall : gp.walls) {
            if (nextPosition.intersects(wall.hitbox)) {
                canMove = false; // If any collision is detected, set canMove to false
                break; // No need to check further if we've already found a collision
            }
        }

        // If no collision is detected, update the player's position
        if (canMove) {
            solidArea.x += dx; // Apply the movement
        } else if(!keyH.upPressed) {
            // If there's a collision, ensure dx is set to 0 to prevent further movement in the collision direction
            dx = 0;
            speed = 0;
        }
        if (keyH.leftPressed || canMove) {
            speed = 4;
        }

        // Update the player's position based on the solidArea
        this.x = solidArea.x;
        // Optionally, if you have logic that moves the player based on 'dx' elsewhere, ensure 'dx' is used correctly
    }

    public void CollisionLeftCheck() {
        // Assuming 'dx' is the desired change in position (from key input) and 'speed' is your movement speed
        int dx = 0;
        if (keyH.leftPressed) dx -= speed;
        // Predict the player's next position based on current movement direction
        Rectangle nextPosition = new Rectangle(solidArea.x + dx, solidArea.y, solidArea.width, solidArea.height);

        // Initially assume the player can move both directions
        boolean canMove = true;

        // Check for collisions with all walls
        for (Ground wall : gp.walls) {
            if (nextPosition.intersects(wall.hitbox)) {
                canMove = false; // If any collision is detected, set canMove to false
                break; // No need to check further if we've already found a collision
            }
        }

        // If no collision is detected, update the player's position
        if (canMove) {
            solidArea.x += dx; // Apply the movement
        } else if(!keyH.upPressed) {
            // If there's a collision, ensure dx is set to 0 to prevent further movement in the collision direction
            dx = 0;
            speed = 0;
        }
        if (keyH.leftPressed || canMove) {
            speed = 4;
        }

        // Update the player's position based on the solidArea
        this.x = solidArea.x;
        // Optionally, if you have logic that moves the player based on 'dx' elsewhere, ensure 'dx' is used correctly
    }


    public Bullet fireBullet() {
        long currentTime = System.currentTimeMillis();
        if (keyH.spacePressed && !color.equals("magenta") && (currentTime - lastBulletTime) >= bulletDelay) {
            lastBulletTime = currentTime;
            return new Bullet(gp, this, keyH, direction=="right"?1:-1);
        }
        return null; // Return null if space key is not pressed
    }
    public void touchRed() {
        if (color.equals("blue") && x+16 > 245 && x < 325 && y+16 > 420 && y+16 < 460) {
            gp.score = 0;
            setDefaultValues();
            gp.setDefaultValuesMonster();
            gp.setDefaultValuesCoin();
        }
    }
    public void touchBlue() {
        if (color.equals("red") && x+16 > 500 && x < 610 && y+16 > 250 && y+16 < 300) {
            gp.score = 0;
            setDefaultValues();
            gp.setDefaultValuesMonster();
            gp.setDefaultValuesCoin();
        }
    }
    public void touchGreen() {
        if (x + 16 > 0 && x < 800 && y + 16 > 700 && y + 16 < 730) {
            gp.score = 0;
            setDefaultValues();
            gp.setDefaultValuesMonster();
            gp.setDefaultValuesCoin();
        }
    }

}
