package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * MainMenu is for implementing the main menu.
 * <p>
 * MainMenu draws the background animation and the settings, start, high scores and exit buttons.
 * MainMenu also checks for user input for those buttons.
 * From here you can continue to Settings by tapping settings button,
 * to LevelSelectionMenu by tapping start button,
 * to HighScoresSelectionMenu by tapping high scores button
 * or you can exit the game.
 * GameProject host is forwarded to all of these classes.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class MainMenu extends fi.tuni.mentalrun.GameProject implements Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    /**
     * Constructs the main menu.
     * <p>
     * Used when the player comes to MainMenu when opening the game or from Run1, Run2, Run3, GameOver or HighScores.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     */
    public MainMenu(fi.tuni.mentalrun.GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic) {
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
     * Constructs the main menu.
     * <p>
     * Used when the player comes to MainMenu from Settings, LevelSelectionMenu or HighScoresSelectionMenu.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public MainMenu(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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
        batch.draw(button.settingsButtonTexture, button.settingsButtonRectangle.x, button.settingsButtonRectangle.y, button.settingsButtonRectangle.width, button.settingsButtonRectangle.height);

        batch.draw(button.startButtonTexture, button.startButtonRectangle.x, button.startButtonRectangle.y, button.startButtonRectangle.width, button.startButtonRectangle.height);
        batch.draw(button.highScoresButtonTexture, button.highScoresButtonRectangle.x, button.highScoresButtonRectangle.y, button.highScoresButtonRectangle.width, button.highScoresButtonRectangle.height);
        batch.draw(button.exitButtonTexture, button.exitButtonRectangle.x, button.exitButtonRectangle.y, button.exitButtonRectangle.width, button.exitButtonRectangle.height);

        batch.end();
    }

    /**
     * Checks for user input for the settings, start, high scores and the exit buttons.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.settingsButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Settings.
                host.setScreen(new Settings(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            if (button.startButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to LevelSelectionMenu.
                host.setScreen(new LevelSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            if (button.highScoresButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to HighScoresSelectionMenu.
                host.setScreen(new HighScoresSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            // Exits the game.
            if (button.exitButtonRectangle.contains(touchPos.x, touchPos.y)) {
                throw new IllegalArgumentException();
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
        soundEffectPressButton.dispose();

        menuBackground.menuBackgroundSheet.dispose();
        button.startButtonTexture.dispose();
        button.settingsButtonTexture.dispose();
        button.highScoresButtonTexture.dispose();
        button.exitButtonTexture.dispose();
    }
}