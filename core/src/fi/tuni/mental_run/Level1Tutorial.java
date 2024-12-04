package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Level1Tutorial is for implementing the level 1 tutorial view.
 * <p>
 * Level1Tutorial draws the background animation, a turquoise board with a header,
 * a tutorial for level 1, the level's collectibles, the rewards for collecting them, instructions on what collectibles to avoid
 * and an ok button.
 * Level1Tutorial also checks for user input for the ok button.
 * Ok button takes the player to Run1.
 * GameProject host is forwarded to Run1.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Level1Tutorial extends fi.tuni.mental_run.GameProject implements Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    AlarmClock alarmClock = new AlarmClock();
    Backpack backpack = new Backpack();
    fi.tuni.mental_run.Bed bed = new Bed();
    NoBreakfast noBreakfast = new NoBreakfast();
    fi.tuni.mental_run.Porridge porridge = new Porridge();
    fi.tuni.mental_run.Toothbrush toothbrush = new Toothbrush();

    String level1Tut;
    String avoid;

    /**
     * Constructs the level 1 tutorial view.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public Level1Tutorial(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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

        turquoiseBoardTexture = new Texture("backgrounds/turquoise_board.png");

        roboto = host.generateFont();

        level1Tut = getLevelText("level1Tutorial");
        avoid = getLevelText("avoid");
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
        batch.draw(turquoiseBoardTexture, 20, 40);
        roboto.draw(batch, level1Tut, 40, 420);

        // Draws the collectibles and their values.
        batch.draw(porridge.texture, 50, 205);
        roboto.draw(batch, String.valueOf(porridge.reward), 50, 195);
        batch.draw(alarmClock.texture, WINDOW_WIDTH / 2 - alarmClock.texture.getWidth() / 2, 205);
        roboto.draw(batch, String.valueOf(alarmClock.reward), WINDOW_WIDTH / 2 - alarmClock.texture.getWidth() / 2, 195);
        batch.draw(backpack.texture, WINDOW_WIDTH - 50 - backpack.texture.getWidth(), 205);
        roboto.draw(batch, String.valueOf(backpack.reward), WINDOW_WIDTH - 50 - backpack.texture.getWidth(), 195);
        batch.draw(toothbrush.texture, 50, 130);
        roboto.draw(batch, String.valueOf(toothbrush.reward), 50, 120);
        batch.draw(noBreakfast.texture, WINDOW_WIDTH / 2 - noBreakfast.texture.getWidth() / 2, 130);
        roboto.draw(batch, avoid, WINDOW_WIDTH / 2 - noBreakfast.texture.getWidth() / 2 - 20, 120);
        batch.draw(bed.texture, WINDOW_WIDTH - 50 - bed.texture.getWidth(), 130);
        roboto.draw(batch, avoid, WINDOW_WIDTH - 50 - bed.texture.getWidth() - 20, 120);

        batch.draw(button.smallOkButtonTexture, button.smallOkButtonRectangle.x, button.smallOkButtonRectangle.y, button.smallOkButtonRectangle.width, button.smallOkButtonRectangle.height);
        batch.end();
    }

    /**
     * Checks for user input for the ok button.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.smallOkButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Run1.
                host.setScreen(new Run1(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
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
        runBackgroundMusic.dispose();

        soundEffectPressButton.dispose();

        menuBackground.menuBackgroundSheet.dispose();
        turquoiseBoardTexture.dispose();
        alarmClock.texture.dispose();
        backpack.texture.dispose();
        bed.texture.dispose();
        noBreakfast.texture.dispose();
        porridge.texture.dispose();
        toothbrush.texture.dispose();
        button.smallOkButtonTexture.dispose();

        menuBackgroundMusic.dispose();
        soundEffectPressButton.dispose();
        roboto.dispose();
    }
}