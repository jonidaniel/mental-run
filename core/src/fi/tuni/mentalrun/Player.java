package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Player is the class where playerSprite and character animation are created.
 * <p>
 * Player stores all of the variables which affect the character and its position.
 * Also each frame to be drawn at each moment is defined here.
 * Extends GameProject.
 *
 * @author Joni Mäkinen
 */
public class Player extends GameProject {
    Sprite playerSprite;

    Texture playerWalkForwardSheet;
    Texture playerWalkLeftSheet;
    Texture playerWalkRightSheet;

    final int PLAYER_WIDTH = 36;
    final int PLAYER_HEIGHT = 64;

    int PLAYER_SPEED_X = 4;
    int PLAYER_SPEED_Y = 1;
    int CAMERA_SPEED_Y = PLAYER_SPEED_Y;

    final int STARTING_POSITION_X = WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2;
    final int STARTING_POSITION_Y = 5;

    // Step control.
    // The + 4 and - 4 corrects the character to the center of the side lanes.
    final int STEP_LEFT_POSITION = FAR_LEFT + 4;
    final int STEP_CENTER_POSITION = STARTING_POSITION_X;
    final int STEP_RIGHT_POSITION = FAR_RIGHT - 4;

    final int FRAME_COLS = 4;
    final int FRAME_ROWS = 1;
    Animation<TextureRegion> walkForwardAnimation;
    Animation<TextureRegion> walkLeftAnimation;
    Animation<TextureRegion> walkRightAnimation;
    float statetime = 0.0f;
    TextureRegion currentFrame;

    /**
     * Constructs Player: the character and its animation.
     */
    public Player() {
        playerSprite = new Sprite();
        playerSprite.setBounds(STARTING_POSITION_X, STARTING_POSITION_Y, PLAYER_WIDTH, PLAYER_HEIGHT / 2);

        playerWalkForwardSheet = new Texture("player/CharacterWalk.png");
        playerWalkLeftSheet = new Texture("player/player_walk_left_sheet.png");
        playerWalkRightSheet = new Texture("player/player_walk_right_sheet.png");

        TextureRegion[][] temp = TextureRegion.split(playerWalkForwardSheet, playerWalkForwardSheet.getWidth() / FRAME_COLS, playerWalkForwardSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions = transform2DTo1D(temp);
        walkForwardAnimation = new Animation<>(10 / 60f, textureRegions);

        TextureRegion[][] temp2 = TextureRegion.split(playerWalkLeftSheet, playerWalkLeftSheet.getWidth() / FRAME_COLS, playerWalkLeftSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions2 = transform2DTo1D(temp2);
        walkLeftAnimation = new Animation<>(10 / 60f, textureRegions2);

        TextureRegion[][] temp3 = TextureRegion.split(playerWalkRightSheet, playerWalkRightSheet.getWidth() / FRAME_COLS, playerWalkRightSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions3 = transform2DTo1D(temp3);
        walkRightAnimation = new Animation<>(10 / 60f, textureRegions3);
    }

    /**
     * Transforms the 2D arrays created in the constructor into 1D arrays.
     *
     * @param temp the 2D array
     * @return returns walkFrames which is a 1D array containing the animation frames
     */
    private TextureRegion[] transform2DTo1D(TextureRegion[][] temp) {
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = temp[i][j];
            }
        }
        return walkFrames;
    }

    /**
     * Sets the right frame to be drawn at each moment.
     */
    public void setCurrentFrame() {
        statetime += Gdx.graphics.getDeltaTime();
        if (useWalkForwardSheet) {
            currentFrame = walkForwardAnimation.getKeyFrame(statetime, true);
        }
        if (useWalkLeftSheet) {
            currentFrame = walkLeftAnimation.getKeyFrame(statetime, true);
        }
        if (useWalkRightSheet) {
            currentFrame = walkRightAnimation.getKeyFrame(statetime, true);
        }
    }
}