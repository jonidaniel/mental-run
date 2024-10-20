package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Settings is for implementing the settings menu.
 * <p>
 * Settings draws the background animation, the sound toggle and music toggle buttons,
 * the info button and the ok button.
 * Settings also checks for user input for those buttons.
 * By tapping sound toggle or music toggle buttons you can determine if the sounds or music are played or not.
 * Info button takes you to Credits and ok button takes you back to MainMenu.
 * GameProject host is forwarded to both of these classes.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Settings extends fi.tuni.mentalrun.GameProject implements Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    /**
     * A constructor for creating the settings menu.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public Settings(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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

        statetime += Gdx.graphics.getDeltaTime();
        menuBackground.setCurrentFrame(statetime);

        batch.begin();
        batch.draw(menuBackground.currentFrame, menuBackground.menuBackgroundRectangle.x, menuBackground.menuBackgroundRectangle.y, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (soundOn) {
            batch.draw(button.soundOnButtonTexture, button.soundButtonRectangle.x, button.soundButtonRectangle.y, button.soundButtonRectangle.width, button.soundButtonRectangle.height);
        }
        if (musicOn) {
            batch.draw(button.musicOnButtonTexture, button.musicButtonRectangle.x, button.musicButtonRectangle.y, button.musicButtonRectangle.width, button.musicButtonRectangle.height);
        }
        if (!soundOn) {
            batch.draw(button.soundOffButtonTexture, button.soundButtonRectangle.x, button.soundButtonRectangle.y, button.soundButtonRectangle.width, button.soundButtonRectangle.height);
        }
        if (!musicOn) {
            batch.draw(button.musicOffButtonTexture, button.musicButtonRectangle.x, button.musicButtonRectangle.y, button.musicButtonRectangle.width, button.musicButtonRectangle.height);
        }
        batch.draw(button.infoButtonTexture, button.infoButtonRectangle.x, button.infoButtonRectangle.y, button.infoButtonRectangle.width, button.infoButtonRectangle.height);
        batch.draw(button.okButtonTexture, button.okButtonRectangle.x, button.okButtonRectangle.y, button.okButtonRectangle.width, button.okButtonRectangle.height);
        batch.end();
    }

    /**
     * Checks for user input for the sound toggle, music toggle, info and ok buttons.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Checks input for the sound toggle button.
            if (button.soundButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundOn = false;
                } else {
                    soundEffectPressButton.play();
                    soundOn = true;
                }
            }

            // Checks input for the music toggle button.
            if (button.musicButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                    if (!musicOn) {
                        menuBackgroundMusic.play();
                        menuBackgroundMusic.setLooping(true);
                        musicOn = true;
                    } else {
                        menuBackgroundMusic.stop();
                        musicOn = false;
                    }
                }
                if (!soundOn) {
                    if (!musicOn) {
                        menuBackgroundMusic.play();
                        menuBackgroundMusic.setLooping(true);
                        musicOn = true;
                    } else {
                        menuBackgroundMusic.stop();
                        musicOn = false;
                    }
                }
            }

            // Checks input for the info button.
            if (button.infoButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Credits.
                host.setScreen(new Credits(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            // Checks input for the ok button.
            if (button.okButtonRectangle.contains(touchPos.x, touchPos.y)) {
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
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();

        menuBackgroundMusic.dispose();
        soundEffectPressButton.dispose();

        menuBackground.menuBackgroundSheet.dispose();
        button.soundOnButtonTexture.dispose();
        button.soundOffButtonTexture.dispose();
        button.musicOnButtonTexture.dispose();
        button.musicOnButtonTexture.dispose();
        button.infoButtonTexture.dispose();
        button.okButtonTexture.dispose();
    }
}