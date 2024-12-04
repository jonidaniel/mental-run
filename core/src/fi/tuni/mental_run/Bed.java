package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Bed creates the level 1 bed collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Bed extends Collectible {
    /**
     * Constructs the beds.
     *
     * @param collectibleYPos new bed y coordinate
     */
    public Bed(float collectibleYPos) {
        texture = new Texture("collectibles/bed1.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "bed";
    }

    /**
     * Constructs the beds.
     */
    public Bed() {
        texture = new Texture("collectibles/bed1.png");
    }

    /**
     * Generates the filler for all beds.
     */
    public void generateFiller() {
        // Randomly generates a new bed filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all beds.
     */
    public void reduceFiller() {
        // Reduces the value of bed filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}