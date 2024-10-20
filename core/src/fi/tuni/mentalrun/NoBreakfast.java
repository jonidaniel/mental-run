package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * NoBreakfast creates the level 1 empty breakfast bowl collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class NoBreakfast extends Collectible {
    /**
     * Constructs the empty breakfast bowls.
     *
     * @param collectibleYPos new empty breakfast bowl y coordinate
     */
    public NoBreakfast(float collectibleYPos) {
        texture = new Texture("collectibles/NoBreakfast.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "noBreakfast";
    }

    /**
     * Constructs the empty breakfast bowls.
     */
    public NoBreakfast() {
        texture = new Texture("collectibles/NoBreakfast.png");
    }

    /**
     * Generates the filler for all empty breakfast bowls.
     */
    public void generateFiller() {
        // Randomly generates a new empty breakfast bowl filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all empty breakfast bowls.
     */
    public void reduceFiller() {
        // Reduces the value of empty breakfast bowl filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}