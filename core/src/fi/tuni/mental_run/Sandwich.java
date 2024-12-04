package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sandwich creates the level 3 sandwich collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Sandwich extends Collectible {
    /**
     * Constructs the sandwiches.
     *
     * @param collectibleYPos new sandwich y coordinate
     */
    public Sandwich(float collectibleYPos) {
        texture = new Texture("collectibles/sandwich.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "sandwich";
        reward = 150;
    }

    /**
     * Constructs the sandwiches.
     */
    public Sandwich() {
        texture = new Texture("collectibles/sandwich.png");
        reward = 150;
    }

    /**
     * Generates the filler for all sandwiches.
     */
    public void generateFiller() {
        // Randomly generates a new sandwich filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all sandwiches.
     */
    public void reduceFiller() {
        // Reduces the value of sandwich filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}