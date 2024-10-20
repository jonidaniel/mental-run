package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Toothbrush creates the level 1 toothbrush collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Toothbrush extends Collectible {
    /**
     * Constructs the level 1 toothbrushes.
     *
     * @param collectibleYPos new toothbrush y coordinate
     */
    public Toothbrush(float collectibleYPos) {
        texture = new Texture("collectibles/Toothbrush.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "toothbrush";
        reward = 125;
    }

    /**
     * Constructs the level 1 toothbrushes.
     */
    public Toothbrush() {
        texture = new Texture("collectibles/Toothbrush.png");
        reward = 125;
    }

    /**
     * Generates the filler for all level 1 toothbrushes.
     */
    public void generateFiller() {
        // Randomly generates a new toothbrush filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all level 1 toothbrushes.
     */
    public void reduceFiller() {
        // Reduces the value of toothbrush filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}