package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Candy creates the level 2 candy collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Candy extends Collectible {
    /**
     * Constructs the candy.
     *
     * @param collectibleYPos new candy y coordinate
     */
    public Candy(float collectibleYPos) {
        texture = new Texture("collectibles/candy.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "candy";
    }

    /**
     * Constructs the candy.
     */
    public Candy() {
        texture = new Texture("collectibles/candy.png");
    }

    /**
     * Generates the filler for all candy.
     */
    public void generateFiller() {
        // Randomly generates a new candy filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all candy.
     */
    public void reduceFiller() {
        // Reduces the value of candy filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}