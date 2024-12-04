package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Credits is for implementing the credits view.
 * <p>
 * Credits draws the background animation, a turquoise board, a credits header at the top of the board,
 * information about the developers of this game, back and show collab buttons and a picture of a colorful hand.
 * Credits also checks for user input for those two buttons.
 * Back button takes you back to Settings and show collab button takes you to Collaborations.
 * GameProject host is forwarded to both of these classes.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Credits extends fi.tuni.mental_run.GameProject implements Screen {
    fi.tuni.mental_run.MenuBackground menuBackground = new MenuBackground();
    fi.tuni.mental_run.Button button = new Button();

    fi.tuni.mental_run.SpecialCollectible specialCollectible = new SpecialCollectible();

    Texture creditsHeaderTexture;

    String creditsList;

    /**
     * Constructs the credits view.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public Credits(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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

        largerTurquoiseBoardTexture = new Texture("backgrounds/larger_turquoise_board.png");
        creditsHeaderTexture = new Texture("headers/credits_header.png");

        roboto = host.generateSmallerFont();

        creditsList = getLevelText("credits");
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
        batch.draw(largerTurquoiseBoardTexture, 4, 3);
        batch.draw(creditsHeaderTexture, WINDOW_WIDTH / 2 - creditsHeaderTexture.getWidth(), 420, creditsHeaderTexture.getWidth() * 2, creditsHeaderTexture.getHeight() * 2);
        roboto.draw(batch, creditsList, 20, 430);
        batch.draw(specialCollectible.texture, WINDOW_WIDTH / 2 - specialCollectible.texture.getWidth() * 2, 65, specialCollectible.texture.getWidth() * 4, specialCollectible.texture.getHeight() * 4);
        batch.draw(button.smallBackButtonTexture, button.leftSmallBackButtonRectangle.x, button.leftSmallBackButtonRectangle.y, button.leftSmallBackButtonRectangle.width, button.leftSmallBackButtonRectangle.height);
        batch.draw(button.showCollabButtonTexture, button.showCollabButtonRectangle.x, button.showCollabButtonRectangle.y, button.showCollabButtonRectangle.width, button.showCollabButtonRectangle.height);
        batch.end();
    }

    /**
     * Checks for user input for the back button and the show collab button.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.leftSmallBackButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Settings.
                host.setScreen(new Settings(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            if (button.showCollabButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Collaborations.
                host.setScreen(new Collaborations(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
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
        largerTurquoiseBoardTexture.dispose();
        creditsHeaderTexture.dispose();
        specialCollectible.texture.dispose();
        button.smallBackButtonTexture.dispose();
        button.showCollabButtonTexture.dispose();

        roboto.dispose();
    }
}