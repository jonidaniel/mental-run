package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * AlarmClock creates the level 1 alarm clock collectible.
 * <p>
 * Extends Collectible.
 *
 * @author Joni Mäkinen
 */
public class AlarmClock extends Collectible {
    /**
     * Constructs the alarm clocks.
     *
     * @param collectibleYPos new alarm clock y coordinate
     */
    public AlarmClock(float collectibleYPos) {
        texture = new Texture("collectibles/alarmclock.png");
        rectangle = new Rectangle(xPos, collectibleYPos, COLLECTIBLE_WIDTH, COLLECTIBLE_HEIGHT);
        id = "alarmClock";
        reward = 175;
    }

    /**
     * Constructs the alarm clocks.
     */
    public AlarmClock() {
        texture = new Texture("collectibles/alarmclock.png");
        reward = 175;
    }

    /**
     * Generates the filler for all alarm clock.
     */
    public void generateFiller() {
        // Randomly generates a new alarm clock filler when the previous one runs out.
        filler = MathUtils.random(180, 360);
    }

    /**
     * Reduces the fillers of all alarm clocks.
     */
    public void reduceFiller() {
        // Reduces the value of alarm clock filler to zero.
        // In generateFiller(), a new value is generated for the filler when it hits zero.
        filler = filler - 1;
    }
}