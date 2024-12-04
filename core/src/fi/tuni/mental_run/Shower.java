package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Shower creates the level 3 shower collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Shower extends Collectible {
    /**
     * Constructs the shower collectibles.
     *
     * @param collectibleYPos new shower collectible y coordinate
     */
    public Shower(float collectibleYPos) {
        texture = new Texture("collectibles/shower.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "shower";
        reward = 125;
    }

    /**
     * Constructs the shower collectibles.
     */
    public Shower() {
        texture = new Texture("collectibles/shower.png");
        reward = 125;
    }

    /**
     * Generates the filler for all shower collectibles.
     */
    public void generateFiller() {
        // Randomly generates a new alarm clock filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all shower collectibles.
     */
    public void reduceFiller() {
        // Reduces the value of alarm clock filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}