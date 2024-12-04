package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * SpecialCollectible creates the special collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class SpecialCollectible extends Collectible {
    // The duration of the special collectible effect.
    int effectDuration;

    /**
     * Constructs the special collectible.
     *
     * @param collectibleYPos new special collectible y coordinate
     */
    public SpecialCollectible(float collectibleYPos) {
        texture = new Texture("collectibles/special_collectible.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "specialCollectible";
    }

    /**
     * Constructs the special collectible.
     */
    public SpecialCollectible() {
        texture = new Texture("collectibles/special_collectible.png");
    }

    /**
     * Generates the filler for all special collectibles.
     */
    public void generateFiller() {
        // Randomly generates a new special collectible filler when the previous one runs out.
        filler = MathUtils.random(500, 750);
    }

    /**
     * Reduces the fillers of all special collectibles.
     */
    public void reduceFiller() {
        // Reduces the value of special collectible filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler -= 1;
        // Reduces the value of special collectible effect duration to zero.
        // When effectDuration hits zero,
        // the character will continue its run with the vertical speed it had before the special collectible was collected.
        if (effectDuration > 0) {
            effectDuration -= 1;
        }
    }
}