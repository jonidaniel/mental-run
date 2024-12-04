package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Gaming creates the level 3 gaming controller collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Gaming extends Collectible {
    /**
     * Constructs the gaming controllers.
     *
     * @param collectibleYPos new gaming controller y coordinate
     */
    public Gaming(float collectibleYPos) {
        texture = new Texture("collectibles/gaming.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "gaming";
    }

    /**
     * Constructs the gaming controllers.
     */
    public Gaming() {
        texture = new Texture("collectibles/gaming.png");
    }

    /**
     * Generates the filler for all gaming controllers.
     */
    public void generateFiller() {
        // Randomly generates a new gaming controller filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all gaming controllers.
     */
    public void reduceFiller() {
        // Reduces the value of gaming controller filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}