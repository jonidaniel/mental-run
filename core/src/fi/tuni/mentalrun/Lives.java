package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Lives is for implementing the three hearts which mean the lives the player has left.
 * <p>
 * Lives stores all variables which affect the lives display.
 * Extends GameProject.
 *
 * @author Joni Mäkinen
 */
public class Lives extends GameProject {
    fi.tuni.mentalrun.Player player = new Player();

    Texture redHeart;
    Texture blackHeart;

    Rectangle leftHeartRectangle;
    Rectangle centerHeartRectangle;
    Rectangle rightHeartRectangle;

    int HEART_WIDTH = 27;
    int HEART_HEIGHT = 26;

    int LEFT_HEART_POSITION_X = 5;
    int CENTER_HEART_POSITION_X = 5 + HEART_WIDTH;
    int RIGHT_HEART_POSITION_X = 5 + HEART_WIDTH / 2;

    int counter = 3;

    boolean gameOver;

    /**
     * A constructor for creating textures and rectangles for the hearts.
     */
    public Lives() {
        redHeart = new Texture("lives/red_heart.png");
        blackHeart = new Texture("lives/black_heart.png");

        leftHeartRectangle = new Rectangle(LEFT_HEART_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - HEART_HEIGHT, HEART_WIDTH, HEART_HEIGHT);
        centerHeartRectangle = new Rectangle(CENTER_HEART_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - HEART_HEIGHT, HEART_WIDTH, HEART_HEIGHT);
        rightHeartRectangle = new Rectangle(RIGHT_HEART_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - HEART_HEIGHT, HEART_WIDTH, HEART_HEIGHT);
    }

    /**
     * Draws the lives display.
     * <p>
     * Boolean gameOver is set as true when the counter hits zero. This makes Run1, Run2 or Run3 switch to GameOver.
     *
     * @param batch      from Run1, Run2 and Run3. Its draw function is called in draw
     * @param playerYPos is used when determining the y coordinate for the lives display
     */
    public void draw(SpriteBatch batch, float playerYPos) {
        if (counter == 3) {
            batch.draw(redHeart, LEFT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, leftHeartRectangle.width, leftHeartRectangle.height);
            batch.draw(redHeart, CENTER_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, centerHeartRectangle.width, centerHeartRectangle.height);
            batch.draw(redHeart, RIGHT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 35, rightHeartRectangle.width, rightHeartRectangle.height);
        }
        if (counter == 2) {
            batch.draw(redHeart, LEFT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, leftHeartRectangle.width, leftHeartRectangle.height);
            batch.draw(redHeart, CENTER_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, centerHeartRectangle.width, centerHeartRectangle.height);
            batch.draw(blackHeart, RIGHT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 35, rightHeartRectangle.width, rightHeartRectangle.height);
        }
        if (counter == 1) {
            batch.draw(redHeart, LEFT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, leftHeartRectangle.width, leftHeartRectangle.height);
            batch.draw(blackHeart, CENTER_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, centerHeartRectangle.width, centerHeartRectangle.height);
            batch.draw(blackHeart, RIGHT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 35, rightHeartRectangle.width, rightHeartRectangle.height);
        }
        if (counter == 0) {
            batch.draw(blackHeart, LEFT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, leftHeartRectangle.width, leftHeartRectangle.height);
            batch.draw(blackHeart, CENTER_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 25, centerHeartRectangle.width, centerHeartRectangle.height);
            batch.draw(blackHeart, RIGHT_HEART_POSITION_X, playerYPos + WINDOW_HEIGHT - 10 - HEART_HEIGHT - 35, rightHeartRectangle.width, rightHeartRectangle.height);
            gameOver = true;
        }
    }
}