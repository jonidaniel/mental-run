package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * LevelSelectionMenu is for implementing the level selection menu.
 * <p>
 * LevelSelectionMenu draws the background animation and the level 1, level 2, level 3 and back buttons.
 * LevelSelectionMenu also checks for user input for those buttons.
 * Tapping level 1, level 2, or level 3 buttons take you to Run1, Run2 or Run3.
 * Whichever button you tap determines which class you are taken to.
 * Back button takes you back to MainMenu.
 * GameProject host is forwarded to each of those classes.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class LevelSelectionMenu extends fi.tuni.mentalrun.GameProject implements Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    /**
     * A constructor for creating the level selection menu.
     * <p>
     * Is called in Run1, Run2, Run3 and GameOver.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     */
    public LevelSelectionMenu(fi.tuni.mentalrun.GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        batch = host.batch;

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));
    }

    /**
     * A constructor for creating the level selection menu.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public LevelSelectionMenu(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        this.statetime = statetime;
        batch = host.batch;

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkInput();

        menuBackground.setCurrentFrame(statetime);
        statetime += Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.draw(menuBackground.currentFrame, menuBackground.menuBackgroundRectangle.x, menuBackground.menuBackgroundRectangle.y, WINDOW_WIDTH, WINDOW_HEIGHT);

        batch.draw(button.level1ButtonTexture, button.level1ButtonRectangle.x, button.level1ButtonRectangle.y, button.level1ButtonRectangle.width, button.level1ButtonRectangle.height);
        batch.draw(button.level2ButtonTexture, button.level2ButtonRectangle.x, button.level2ButtonRectangle.y, button.level2ButtonRectangle.width, button.level2ButtonRectangle.height);
        batch.draw(button.level3ButtonTexture, button.level3ButtonRectangle.x, button.level3ButtonRectangle.y, button.level3ButtonRectangle.width, button.level3ButtonRectangle.height);
        batch.draw(button.backButtonTexture, button.backButtonRectangle.x, button.backButtonRectangle.y, button.backButtonRectangle.width, button.backButtonRectangle.height);

        batch.end();
    }

    /**
     * Checks for user input.
     * <p>
     * Checks for user input for level 1, level 2, level 3 and back buttons.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.level1ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Level1Tutorial.
                host.setScreen(new Level1Tutorial(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }
            if (button.level2ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Level2Tutorial.
                host.setScreen(new Level2Tutorial(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }
            if (button.level3ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Level3Tutorial.
                host.setScreen(new Level3Tutorial(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }
            if (button.backButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to MainMenu.
                host.setScreen(new MainMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();

        menuBackgroundMusic.dispose();
        runBackgroundMusic.dispose();

        soundEffectPressButton.dispose();

        menuBackground.menuBackgroundSheet.dispose();
        button.level1ButtonTexture.dispose();
        button.level2ButtonTexture.dispose();
        button.level3ButtonTexture.dispose();
        button.backButtonTexture.dispose();
    }
}