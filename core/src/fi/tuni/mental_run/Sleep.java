package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sleep creates the level 3 sleep collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Sleep extends Collectible {
    /**
     * Constructs the sleep collectibles.
     *
     * @param collectibleYPos new sleep collectible y coordinate
     */
    public Sleep(float collectibleYPos) {
        texture = new Texture("collectibles/sleep.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "sleep";
        reward = 200;
    }

    /**
     * Constructs the sleep collectibles.
     */
    public Sleep() {
        texture = new Texture("collectibles/sleep.png");
        reward = 200;
    }

    /**
     * Generates the filler for all sleep collectibles.
     */
    public void generateFiller() {
        // Randomly generates a new sleep filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all sleep collectibles.
     */
    public void reduceFiller() {
        // Reduces the value of sleep filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}