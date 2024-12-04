package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Level3Tutorial is for implementing the level 3 tutorial view.
 * <p>
 * Level3Tutorial draws the background animation, a turquoise board with a header,
 * a tutorial for level 3, the level's collectibles, the rewards for collecting them, instructions on what collectibles to avoid
 * and an ok button.
 * Level3Tutorial also checks for user input for the ok button.
 * Ok button takes the player to Run3.
 * GameProject host is forwarded to Run3.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Level3Tutorial extends fi.tuni.mental_run.GameProject implements Screen {
    fi.tuni.mental_run.MenuBackground menuBackground = new MenuBackground();
    fi.tuni.mental_run.Button button = new Button();

    fi.tuni.mental_run.EnergyDrink energyDrink = new EnergyDrink();
    fi.tuni.mental_run.Gaming gaming = new Gaming();
    fi.tuni.mental_run.Sandwich sandwich = new Sandwich();
    fi.tuni.mental_run.Shower shower = new Shower();
    fi.tuni.mental_run.Sleep sleep = new Sleep();
    fi.tuni.mental_run.Toothbrush3 toothbrush3 = new Toothbrush3();

    String level3Tut;
    String avoid;

    /**
     * Constructs the level 3 tutorial view.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public Level3Tutorial(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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

        level3Tut = getLevelText("level3Tutorial");
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
        roboto.draw(batch, level3Tut, 35, 420);

        // Draws the collectibles and their values.
        batch.draw(sleep.texture, 50, 205);
        roboto.draw(batch, String.valueOf(sleep.reward), 50, 195);
        batch.draw(toothbrush3.texture, WINDOW_WIDTH / 2 - toothbrush3.texture.getWidth() / 2, 205);
        roboto.draw(batch, String.valueOf(toothbrush3.reward), WINDOW_WIDTH / 2 - toothbrush3.texture.getWidth() / 2, 195);
        batch.draw(sandwich.texture, WINDOW_WIDTH - 50 - sandwich.texture.getWidth(), 205);
        roboto.draw(batch, String.valueOf(sandwich.reward), WINDOW_WIDTH - 50 - sandwich.texture.getWidth(), 195);
        batch.draw(shower.texture, 50, 130);
        roboto.draw(batch, String.valueOf(shower.reward), 50, 120);
        batch.draw(energyDrink.texture, WINDOW_WIDTH / 2 - energyDrink.texture.getWidth() / 2, 130);
        roboto.draw(batch, avoid, WINDOW_WIDTH / 2 - energyDrink.texture.getWidth() / 2 - 20, 120);
        batch.draw(gaming.texture, WINDOW_WIDTH - 50 - gaming.texture.getWidth(), 130);
        roboto.draw(batch, avoid, WINDOW_WIDTH - 50 - gaming.texture.getWidth() - 20, 120);

        batch.draw(button.smallOkButtonTexture, button.smallOkButtonRectangle.x, button.smallOkButtonRectangle.y, button.smallOkButtonRectangle.width, button.smallOkButtonRectangle.height);
        batch.end();
    }

    /**
     * Checking for user input for the ok button.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.smallOkButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Run3.
                host.setScreen(new Run3(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
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
        turquoiseBoardTexture.dispose();
        sleep.texture.dispose();
        toothbrush3.texture.dispose();
        sandwich.texture.dispose();
        shower.texture.dispose();
        energyDrink.texture.dispose();
        gaming.texture.dispose();
        button.smallOkButtonTexture.dispose();

        menuBackgroundMusic.dispose();
        soundEffectPressButton.dispose();
        roboto.dispose();
    }
}