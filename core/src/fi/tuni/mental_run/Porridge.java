package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Porridge creates the level 1 porridge bowl collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Porridge extends Collectible {
    /**
     * Constructs the porridge bowls.
     *
     * @param collectibleYPos new porridge bowl y coordinate
     */
    public Porridge(float collectibleYPos) {
        texture = new Texture("collectibles/porridge.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "porridge";
        reward = 200;
    }

    /**
     * Constructs the porridge bowls.
     */
    public Porridge() {
        texture = new Texture("collectibles/porridge.png");
        reward = 200;
    }

    /**
     * Generates the filler for all porridge bowls.
     */
    public void generateFiller() {
        // Randomly generates a new porridge bowl filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all porridge bowls.
     */
    public void reduceFiller() {
        // Reduces the value of porridge bowl filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}