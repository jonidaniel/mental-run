package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Toothbrush3 creates the level 3 toothbrush collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Toothbrush3 extends Collectible {
    /**
     * Constructs the level 3 toothbrushes.
     *
     * @param collectibleYPos new toothbrush y coordinate
     */
    public Toothbrush3(float collectibleYPos) {
        texture = new Texture("collectibles/toothbrush3.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "toothbrush3";
        reward = 175;
    }

    /**
     * Constructs the level 3 toothbrushes.
     */
    public Toothbrush3() {
        texture = new Texture("collectibles/toothbrush3.png");
        reward = 175;
    }

    /**
     * Generates the filler for all level 3 toothbrushes.
     */
    public void generateFiller() {
        // Randomly generates a new alarm clock filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all level 3 toothbrushes.
     */
    public void reduceFiller() {
        // Reduces the value of alarm clock filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}