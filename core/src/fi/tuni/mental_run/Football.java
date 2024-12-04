package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Football creates the level 2 football collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Football extends Collectible {
    /**
     * Constructs the footballs.
     *
     * @param collectibleYPos new football y coordinate
     */
    public Football(float collectibleYPos) {
        texture = new Texture("collectibles/football.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "football";
        reward = 150;
    }

    /**
     * Constructs the footballs.
     */
    public Football() {
        texture = new Texture("collectibles/football.png");
        reward = 150;
    }

    /**
     * Generates the filler for all footballs.
     */
    public void generateFiller() {
        // Randomly generates a new football filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all footballs.
     */
    public void reduceFiller() {
        // Reduces the value of football filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}