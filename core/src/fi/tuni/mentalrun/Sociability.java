package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sociability creates the level 2 sociability collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Sociability extends Collectible {
    /**
     * Constructs the sociability collectible.
     *
     * @param collectibleYPos new sociability collectible y coordinate
     */
    public Sociability(float collectibleYPos) {
        texture = new Texture("collectibles/sociability.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "sociability";
        reward = 200;
    }

    /**
     * Constructs the sociability collectible.
     */
    public Sociability() {
        texture = new Texture("collectibles/sociability.png");
        reward = 200;
    }

    /**
     * Generates the filler for all sociability collectibles.
     */
    public void generateFiller() {
        // Randomly generates a new sociability filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all sociability collectibles.
     */
    public void reduceFiller() {
        // Reduces the value of sociability filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}