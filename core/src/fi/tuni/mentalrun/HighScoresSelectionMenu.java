package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * HighScoresSelectionMenu is for implementing the high scores selection view.
 * <p>
 * HighScoresSelectionMenu draws the background animation and the level 1,
 * level 2 and the level 3 high scores buttons and the back button.
 * HighScoresSelectionMenu also checks for user input for those buttons.
 * Tapping level 1, level 2, or level 3 buttons take you to HighScores.
 * Whichever button you tap determines which level's high scores board is shown in HighScores.
 * Back button takes you back to MainMenu.
 * GameProject host is forwarded to both of those classes.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class HighScoresSelectionMenu extends fi.tuni.mentalrun.GameProject implements Screen {
    fi.tuni.mentalrun.MenuBackground menuBackground = new MenuBackground();
    fi.tuni.mentalrun.Button button = new Button();

    /**
     * Constructs HighScoresSelectionMenu.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public HighScoresSelectionMenu(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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
     * Checks for user input for level 1, level 2 and level 3 high scores buttons and for back button.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.level1ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                levelVariable = 1;
                // Takes you to HighScores.
                host.setScreen(new fi.tuni.mentalrun.HighScores(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, levelVariable, statetime));
            }
            if (button.level2ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                levelVariable = 2;
                // Takes you to HighScores.
                host.setScreen(new fi.tuni.mentalrun.HighScores(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, levelVariable, statetime));
            }
            if (button.level3ButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                levelVariable = 3;
                // Takes you to HighScores.
                host.setScreen(new HighScores(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, levelVariable, statetime));
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
        menuBackgroundMusic.dispose();
        soundEffectPressButton.dispose();

        menuBackground.menuBackgroundSheet.dispose();
        button.level1ButtonTexture.dispose();
        button.level2ButtonTexture.dispose();
        button.level3ButtonTexture.dispose();
        button.backButtonTexture.dispose();
    }
}