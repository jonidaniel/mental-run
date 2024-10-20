package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * MenuBackground is for implementing the background animation.
 * <p>
 * MenuBackground stores all variables for the background animation.
 * Also each frame to be drawn at each moment is defined here.
 *
 * @author Joni Mäkinen
 */
public class MenuBackground extends GameProject {
    Texture menuBackgroundSheet;

    Rectangle menuBackgroundRectangle;

    final int FRAME_COLS = 3;
    final int FRAME_ROWS = 9;
    Animation<TextureRegion> menuBackgroundAnimation;
    TextureRegion currentFrame;

    /**
     * Constructs the menu background animation.
     */
    public MenuBackground() {
        menuBackgroundSheet = new Texture("backgrounds/MenuArt798x3996.png");

        menuBackgroundRectangle = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        TextureRegion[][] temp = TextureRegion.split(menuBackgroundSheet, menuBackgroundSheet.getWidth() / FRAME_COLS, menuBackgroundSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions = transform2DTo1D(temp);
        menuBackgroundAnimation = new Animation<>(10 / 60f, textureRegions);
    }

    /**
     * Transforms the 2D arrays created in MenuBackground's constructor to 1D arrays.
     *
     * @param temp the 2D array created in the constructor
     * @return returns menuBackgroundFrames which is the 1D array containing the animation frames
     */
    private TextureRegion[] transform2DTo1D(TextureRegion[][] temp) {
        TextureRegion[] menuBackgroundFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                menuBackgroundFrames[index++] = temp[i][j];
            }
        }
        return menuBackgroundFrames;
    }

    /**
     * Sets the frame to be drawn at each moment.
     *
     * @param statetime used in determining the correct current frame for the animation
     */
    public void setCurrentFrame(float statetime) {
        currentFrame = menuBackgroundAnimation.getKeyFrame(statetime, true);
    }
}