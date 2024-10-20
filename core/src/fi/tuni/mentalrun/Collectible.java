package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Collectible contains common variables of the collectibles.
 * <p>
 * Extends GameProject.
 * Every collectible class extend Collectible.
 *
 * @author Joni Mäkinen
 */
public class Collectible extends GameProject {
    Texture texture;

    Rectangle rectangle;

    int COLLECTIBLE_WIDTH = 32;
    int COLLECTIBLE_HEIGHT = 32;

    int filler;
    int reward;

    float xPos;

    String id;
}