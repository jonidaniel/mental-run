package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * EnergyDrink creates the level 3 energy drink collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class EnergyDrink extends Collectible {
    /**
     * Constructs the energy drinks.
     *
     * @param collectibleYPos new energy drink y coordinate
     */
    public EnergyDrink(float collectibleYPos) {
        texture = new Texture("collectibles/energy_drink.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "energyDrink";
    }

    /**
     * Constructs the energy drinks.
     */
    public EnergyDrink() {
        texture = new Texture("collectibles/energy_drink.png");
    }

    /**
     * Generates the filler for all energy drinks.
     */
    public void generateFiller() {
        // Randomly generates a new energy drink filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all energy drinks.
     */
    public void reduceFiller() {
        // Reduces the value of energy drink filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}