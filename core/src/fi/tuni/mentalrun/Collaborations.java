package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Collaborations is for implementing the collaborations view.
 * <p>
 * Collaborations draws the background animation, a turquoise board, a collaborations header at the top of the board,
 * information about the collaborators in producing of this game, collaborators' logos and a back button.
 * Collaborations also checks for user input for the back button.
 * Back button takes you back to Settings.
 * GameProject host is forwarded to Settings.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Collaborations extends fi.tuni.mentalrun.GameProject implements Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    Texture collaborationsHeaderTexture;
    Texture tuniTexture;
    Texture elaytyenOsalliseksiTexture;
    Texture europeanUnionTexture;
    Texture vipuvoimaaTexture;

    String collab;

    /**
     * A constructor for creating the collaborations view.
     * <p>
     * Called in Credits.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public Collaborations(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, float statetime) {
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
        collaborationsHeaderTexture = new Texture("headers/collaborations_header.png");
        tuniTexture = new Texture("collaborations_images/tuni_tamk_logo_eng.png");
        elaytyenOsalliseksiTexture = new Texture("collaborations_images/elaytyen_osalliseksi.png");
        europeanUnionTexture = new Texture("collaborations_images/euroopan_unioni.png");
        vipuvoimaaTexture = new Texture("collaborations_images/vipuvoimaa.png");

        roboto = host.generateSmallerFont();

        collab = getLevelText("collaboration");
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
        batch.draw(collaborationsHeaderTexture, WINDOW_WIDTH / 2 - collaborationsHeaderTexture.getWidth(), 410, collaborationsHeaderTexture.getWidth() * 2, collaborationsHeaderTexture.getHeight() * 2);
        roboto.draw(batch, collab, 12, 405);
        batch.draw(tuniTexture, WINDOW_WIDTH / 2 - tuniTexture.getWidth() / 2, 235, tuniTexture.getWidth(), tuniTexture.getHeight());
        batch.draw(elaytyenOsalliseksiTexture, WINDOW_WIDTH / 2 - elaytyenOsalliseksiTexture.getWidth() / 2, 140, elaytyenOsalliseksiTexture.getWidth(), elaytyenOsalliseksiTexture.getHeight());
        batch.draw(europeanUnionTexture, 55, 55, europeanUnionTexture.getWidth(), europeanUnionTexture.getHeight());
        batch.draw(vipuvoimaaTexture, 145, 75, vipuvoimaaTexture.getWidth(), vipuvoimaaTexture.getHeight());
        batch.draw(button.smallBackButtonTexture, button.smallBackButtonRectangle.x, button.smallBackButtonRectangle.y, button.smallBackButtonRectangle.width, button.smallBackButtonRectangle.height);
        batch.end();
    }

    /**
     * Checks for user input for the back button.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (button.lowerSmallOkButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to Settings.
                host.setScreen(new Settings(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
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
        collaborationsHeaderTexture.dispose();
        tuniTexture.dispose();
        elaytyenOsalliseksiTexture.dispose();
        europeanUnionTexture.dispose();
        vipuvoimaaTexture.dispose();
        button.smallBackButtonTexture.dispose();

        roboto.dispose();
    }
}