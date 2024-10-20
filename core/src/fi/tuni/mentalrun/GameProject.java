package fi.tuni.mentalrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * This is the main class of the game Mental Run.
 * <p>
 * The generating of fonts used in the project is implemented here.
 * GameProject's object is forwarded to class MainMenu and later to other classes.
 * Every class in this project extend GameProject.
 *
 * @author Joni Mäkinen
 * @version 1.0.1
 */
public class GameProject extends Game {
    GameProject host;

    SpriteBatch batch;

    OrthographicCamera camera;

    Music menuBackgroundMusic;
    Music runBackgroundMusic;

    Sound soundEffectCountdown321;
    Sound soundEffectCountdownGo;
    Sound soundEffectPressButton;
    Sound soundEffectSpecialCollectible;
    Sound soundEffectCollectiblePositive;
    Sound soundEffectCollectibleNegative;
    Sound soundEffectMadeIt;
    Sound soundEffectDidNotMakeIt;

    Texture turquoiseBoardTexture;
    Texture widerTurquoiseBoardTexture;
    Texture largerTurquoiseBoardTexture;

    TiledMap gameProjectMap;
    TiledMapRenderer tiledMapRenderer;

    BitmapFont roboto;
    BitmapFont smallerRoboto;
    BitmapFont largerRoboto;
    BitmapFont yellowRoboto;

    int WINDOW_WIDTH = 288;
    int WINDOW_HEIGHT = 480;

    final int TILE_WIDTH = 32;
    final int TILE_HEIGHT = 32;

    // Stepless character control. The - 6 corrects the difference between TILE_WIDTH (32) and PLAYER_WIDTH (36).
    // The + 4 and - 4 defines a little margin, an empty space, on the sides of the character and the collectibles (stepless control).
    final int FAR_LEFT = WINDOW_WIDTH / 2 - TILE_WIDTH * 2 - TILE_WIDTH / 2 + 4;
    final int FAR_RIGHT = WINDOW_WIDTH / 2 + TILE_WIDTH + TILE_WIDTH / 2 - 6 - 4;

    final int LEFT_SIDE_BORDER = TILE_WIDTH * 2;
    final int RIGHT_SIDE_BORDER = TILE_WIDTH * 7;

    final int LOOP_START_RUN1 = TILE_HEIGHT * 400;
    final int LOOP_AMOUNT_RUN1 = TILE_HEIGHT * 119;
    final int LOOP_START_RUN2 = 0;
    final int LOOP_AMOUNT_RUN2 = TILE_HEIGHT * 170;
    final int LOOP_START_RUN3 = TILE_HEIGHT * 360;
    final int LOOP_AMOUNT_RUN3 = TILE_HEIGHT * 159;

    Vector3 touchPos = new Vector3();

    static int levelVariable;

    boolean useWalkForwardSheet = true;
    boolean useWalkLeftSheet;
    boolean useWalkRightSheet;
    boolean musicOn;
    boolean soundOn;

    Locale locale = Locale.getDefault();

    float statetime;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        menuBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu_background_music.mp3"));
        menuBackgroundMusic.play();
        menuBackgroundMusic.setLooping(true);
        runBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/run_background_music.mp3"));

        musicOn = true;
        soundOn = true;

        // Sets MainMenu as the screen. MainMenu gets GameProject object as an attribute.
        setScreen(new MainMenu(this, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        // Uses renders of the following views.
        super.render();
    }

    /**
     * Creates the most widely used black font.
     *
     * @return returns generator.generateFont(parameter), which is the black, size 18 font
     */
    public BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 1;
        return generator.generateFont(parameter);
    }

    /**
     * Creates the smaller black font.
     *
     * @return returns generator.generateFont(parameter), which is the smaller black, size 12 font
     */
    public BitmapFont generateSmallerFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 1;
        return generator.generateFont(parameter);
    }

    /**
     * Creates the larger black font.
     *
     * @return returns generator.generateFont(parameter), which is the larger black, size 36 font
     */
    public BitmapFont generateLargerFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 1;
        return generator.generateFont(parameter);
    }

    /**
     * Creates the yellow font.
     * <p>
     * The font is used to highlight the newly submitted high score on high scores board.
     *
     * @return returns generator.generateFont(parameter), which is yellow, size 18 font
     */
    public BitmapFont generateYellowFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.YELLOW;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        return generator.generateFont(parameter);
    }

    /**
     * All translatable texts are retrieved through this method
     *
     * @param key String that corresponds with a translation
     * @return returns the string of text that corresponds with the given key
     */
    public String getLevelText(String key) {

        Locale locale = Locale.getDefault();

        I18NBundle langBundle =
                I18NBundle.createBundle(Gdx.files.internal("LangBundle"), locale);

        return langBundle.get(key);
    }

    public boolean isFinnish() {
        return locale.toString().equals("fi_FI");
    }

    @Override
    public void dispose() {
        batch.dispose();

        menuBackgroundMusic.dispose();
        runBackgroundMusic.dispose();

        soundEffectMadeIt.dispose();
        soundEffectDidNotMakeIt.dispose();

        turquoiseBoardTexture.dispose();
        widerTurquoiseBoardTexture.dispose();
        largerTurquoiseBoardTexture.dispose();

        roboto.dispose();
        yellowRoboto.dispose();
        largerRoboto.dispose();
        smallerRoboto.dispose();
    }
}
