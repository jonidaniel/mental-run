package fi.tuni.mental_run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * MobilePhone creates the level 2 mobile phone collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class MobilePhone extends Collectible {
    /**
     * Constructs the mobile phones.
     *
     * @param collectibleYPos new mobile phone y coordinate
     */
    public MobilePhone(float collectibleYPos) {
        texture = new Texture("collectibles/mobile_phone.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "mobilePhone";
    }

    /**
     * Constructs the mobile phones.
     */
    public MobilePhone() {
        texture = new Texture("collectibles/mobile_phone.png");
    }

    /**
     * Generates the filler for all mobile phones.
     */
    public void generateFiller() {
        // Randomly generates a new mobile phone filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all mobile phones.
     */
    public void reduceFiller() {
        // Reduces the value of mobile phone filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}