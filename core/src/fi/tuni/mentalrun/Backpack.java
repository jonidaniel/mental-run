package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Backpack creates the level 1 backpack collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Backpack extends Collectible {
    /**
     * Constructs the backpacks.
     *
     * @param collectibleYPos new backpack y coordinate
     */
    public Backpack(float collectibleYPos) {
        texture = new Texture("collectibles/Backpack.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "backpack";
        reward = 150;
    }

    /**
     * Constructs the backpacks.
     */
    public Backpack() {
        texture = new Texture("collectibles/Backpack.png");
        reward = 150;
    }

    /**
     * Generates the filler for all backpacks.
     */
    public void generateFiller() {
        // Randomly generates a new backpack filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all backpacks.
     */
    public void reduceFiller() {
        // Reduces the value of backpack filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}