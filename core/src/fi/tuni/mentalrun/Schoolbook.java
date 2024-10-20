package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Schoolbook creates the level 2 schoolbook collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Schoolbook extends Collectible {
    /**
     * Constructs the schoolbooks.
     *
     * @param collectibleYPos new schoolbook y coordinate
     */
    public Schoolbook(float collectibleYPos) {
        texture = new Texture("collectibles/schoolbook.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "schoolbook";
        reward = 175;
    }

    /**
     * Constructs the schoolbooks.
     */
    public Schoolbook() {
        texture = new Texture("collectibles/schoolbook.png");
        reward = 175;
    }

    /**
     * Generates the filler for all schoolbooks.
     */
    public void generateFiller() {
        // Randomly generates a new schoolbook filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all schoolbooks.
     */
    public void reduceFiller() {
        // Reduces the value of schoolbook filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}