package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Lunch creates the level 2 lunch collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class Lunch extends Collectible {
    /**
     * Constructs the lunch plates.
     *
     * @param collectibleYPos new lunch plate y coordinate
     */
    public Lunch(float collectibleYPos) {
        texture = new Texture("collectibles/lunch.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "lunch";
        reward = 125;
    }

    /**
     * Constructs the lunch plates.
     */
    public Lunch() {
        texture = new Texture("collectibles/lunch.png");
        reward = 125;
    }

    /**
     * Generates the filler for all lunch plates.
     */
    public void generateFiller() {
        // Randomly generates a new lunch plate filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all lunch plates.
     */
    public void reduceFiller() {
        // Reduces the value of lunch plate filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}