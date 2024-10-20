package fi.tuni.mentalrun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Button is for implementing the buttons.
 * <p>
 * Button contains all the variables that affect the buttons.
 * Extends GameProject.
 *
 * @author Joni Mäkinen
 */
public class Button extends GameProject {
    // Initializes the main menu buttons.
    Texture settingsButtonTexture;
    Rectangle settingsButtonRectangle;
    Texture startButtonTexture;
    Rectangle startButtonRectangle;
    Texture highScoresButtonTexture;
    Rectangle highScoresButtonRectangle;
    Texture exitButtonTexture;
    Rectangle exitButtonRectangle;

    // Initializes the level selection menu buttons.
    Texture level1ButtonTexture;
    Rectangle level1ButtonRectangle;
    Texture level2ButtonTexture;
    Rectangle level2ButtonRectangle;
    Texture level3ButtonTexture;
    Rectangle level3ButtonRectangle;

    // Initializes the settings menu buttons.
    Texture soundOnButtonTexture;
    Texture soundOffButtonTexture;
    Rectangle soundButtonRectangle;
    Texture musicOnButtonTexture;
    Texture musicOffButtonTexture;
    Rectangle musicButtonRectangle;
    Texture infoButtonTexture;
    Rectangle infoButtonRectangle;

    // Initializes the OK and back buttons.
    Texture okButtonTexture;
    Rectangle okButtonRectangle;
    Texture smallOkButtonTexture;
    Rectangle smallOkButtonRectangle;
    Rectangle lowerSmallOkButtonRectangle;
    Texture backButtonTexture;
    Rectangle backButtonRectangle;
    Texture smallBackButtonTexture;
    Rectangle smallBackButtonRectangle;
    Rectangle leftSmallBackButtonRectangle;

    // Initializes the show collab, enter, skip & retry, skip & back and retry buttons.
    Texture showCollabButtonTexture;
    Rectangle showCollabButtonRectangle;
    Texture enterButtonTexture;
    Rectangle enterButtonRectangle;
    Texture skipAndRetryButtonTexture;
    Rectangle skipAndRetryButtonRectangle;
    Texture skipAndBackButtonTexture;
    Rectangle skipAndBackButtonRectangle;
    Texture retryButtonTexture;
    Rectangle retryButtonRectangle;

    // Initializes the in-game buttons.
    Texture pauseButtonTexture;
    Rectangle pauseButtonRectangle;
    Texture backToMenuButtonTexture;
    Rectangle backToMenuButtonRectangle;

    final int MENU_BUTTON_POSITION_X = WINDOW_WIDTH / 2;
    final int MENU_BUTTON_POSITION_Y = 40;

    final int IN_GAME_BUTTON_WIDTH = 32;
    final int IN_GAME_BUTTON_HEIGHT = 32;

    final int IN_GAME_BUTTON_POSITION_X = WINDOW_WIDTH - IN_GAME_BUTTON_WIDTH - 20;

    /**
     * Constructs all the buttons.
     */
    public Button() {

        // Creates the main menu buttons.
        settingsButtonTexture = new Texture("buttons/settings_button.png");
        settingsButtonRectangle = new Rectangle(WINDOW_WIDTH - settingsButtonTexture.getWidth() - 5, WINDOW_HEIGHT - settingsButtonTexture.getHeight() - 5, settingsButtonTexture.getWidth(), settingsButtonTexture.getHeight());
        startButtonTexture = new Texture("buttons/start_button.png");
        startButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - startButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y + 150, startButtonTexture.getWidth() * 2, startButtonTexture.getHeight() * 2);
        highScoresButtonTexture = new Texture("buttons/highscores_button.png");
        highScoresButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - highScoresButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y + 75, highScoresButtonTexture.getWidth() * 2, highScoresButtonTexture.getHeight() * 2);
        exitButtonTexture = new Texture("buttons/exit_button.png");
        exitButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - exitButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y, exitButtonTexture.getWidth() * 2, exitButtonTexture.getHeight() * 2);

        // Creates the level selection menu buttons.
        level1ButtonTexture = new Texture("buttons/level_1_button.png");
        level1ButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - level1ButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y + 225, level1ButtonTexture.getWidth() * 2, level1ButtonTexture.getHeight() * 2);
        level2ButtonTexture = new Texture("buttons/level_2_button.png");
        level2ButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - level2ButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y + 150, level2ButtonTexture.getWidth() * 2, level2ButtonTexture.getHeight() * 2);
        level3ButtonTexture = new Texture("buttons/level_3_button.png");
        level3ButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - level3ButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y + 75, level3ButtonTexture.getWidth() * 2, level3ButtonTexture.getHeight() * 2);

        // Creates the settings menu buttons.
        soundOnButtonTexture = new Texture("buttons/sound_on_button.png");
        soundOffButtonTexture = new Texture("buttons/sound_off_button.png");
        soundButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - soundOnButtonTexture.getWidth() / 2, MENU_BUTTON_POSITION_Y + 200, soundOnButtonTexture.getWidth(), soundOnButtonTexture.getHeight());
        musicOnButtonTexture = new Texture("buttons/music_on_button.png");
        musicOffButtonTexture = new Texture("buttons/music_off_button.png");
        musicButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - musicOnButtonTexture.getWidth() / 2, MENU_BUTTON_POSITION_Y + 150, musicOnButtonTexture.getWidth(), musicOnButtonTexture.getHeight());
        infoButtonTexture = new Texture("buttons/info_button.png");
        infoButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - infoButtonTexture.getWidth() / 2, MENU_BUTTON_POSITION_Y + 100, infoButtonTexture.getWidth(), infoButtonTexture.getHeight());

        // Creates the OK and back buttons.
        okButtonTexture = new Texture("buttons/ok_button.png");
        okButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - okButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y, okButtonTexture.getWidth() * 2, okButtonTexture.getHeight() * 2);
        smallOkButtonTexture = new Texture("buttons/ok_button.png");
        smallOkButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - smallOkButtonTexture.getWidth() / 2, 60, okButtonTexture.getWidth(), okButtonTexture.getHeight());
        lowerSmallOkButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - smallOkButtonTexture.getWidth() / 2, 15, okButtonTexture.getWidth(), okButtonTexture.getHeight());
        backButtonTexture = new Texture("buttons/back_button.png");
        backButtonRectangle = new Rectangle(MENU_BUTTON_POSITION_X - backButtonTexture.getWidth(), MENU_BUTTON_POSITION_Y, backButtonTexture.getWidth() * 2, backButtonTexture.getHeight() * 2);
        smallBackButtonTexture = new Texture("buttons/back_button.png");
        smallBackButtonRectangle = new Rectangle(WINDOW_WIDTH / 2 - smallBackButtonTexture.getWidth() / 2, 15, backButtonTexture.getWidth(), backButtonTexture.getHeight());
        leftSmallBackButtonRectangle = new Rectangle(40, 15, backButtonTexture.getWidth(), backButtonTexture.getHeight());

        // Creates the show collab, enter, skip & retry, skip & back and retry buttons.
        // Enter, skip & retry, skip & back and retry buttons' positions are defined in classes Run1, Run2 and Run3.
        // This is because of the altering excessDistance variable.
        showCollabButtonTexture = new Texture("buttons/show_collab_button.png");
        showCollabButtonRectangle = new Rectangle(130, 15, showCollabButtonTexture.getWidth(), showCollabButtonTexture.getHeight());
        enterButtonTexture = new Texture("buttons/enter_button.png");
        enterButtonRectangle = new Rectangle();
        enterButtonRectangle.setSize(enterButtonTexture.getWidth(), enterButtonTexture.getHeight());
        skipAndRetryButtonTexture = new Texture("buttons/skip_and_retry_button.png");
        skipAndRetryButtonRectangle = new Rectangle();
        skipAndRetryButtonRectangle.setSize(skipAndRetryButtonTexture.getWidth(), skipAndRetryButtonTexture.getHeight());
        skipAndBackButtonTexture = new Texture("buttons/skip_and_back_button.png");
        skipAndBackButtonRectangle = new Rectangle();
        skipAndBackButtonRectangle.setSize(skipAndBackButtonTexture.getWidth(), skipAndBackButtonTexture.getHeight());
        retryButtonTexture = new Texture("buttons/retry_button.png");
        retryButtonRectangle = new Rectangle();
        retryButtonRectangle.setSize(retryButtonTexture.getWidth(), retryButtonTexture.getHeight());

        // Creates the in-game buttons.
        // The in-game buttons' positions are defined in classes Run1, Run2 and Run3.
        // This is because of the altering excessDistance variable.
        pauseButtonTexture = new Texture("buttons/pause_button.png");
        pauseButtonRectangle = new Rectangle();
        pauseButtonRectangle.setSize(pauseButtonTexture.getWidth() * 1.3f, pauseButtonTexture.getHeight() * 1.3f);
        backToMenuButtonTexture = new Texture("buttons/back_to_menu_button.png");
        backToMenuButtonRectangle = new Rectangle();
        backToMenuButtonRectangle.setSize(backToMenuButtonTexture.getWidth() * 1.3f, backToMenuButtonTexture.getHeight() * 1.3f);

        // Used for Finnish locale
        if (isFinnish()) {

            // Main menu buttons
            startButtonTexture = new Texture("buttons/buttons_FI/Pelaa.png");
            highScoresButtonTexture = new Texture("buttons/buttons_FI/Tulokset.png");
            exitButtonTexture = new Texture("buttons/buttons_FI/Poistu.png");

            // Level selection buttons
            level1ButtonTexture = new Texture("buttons/buttons_FI/Taso1.png");
            level2ButtonTexture = new Texture("buttons/buttons_FI/Taso2.png");
            level3ButtonTexture = new Texture("buttons/buttons_FI/Taso3.png");

            // Back buttons
            backButtonTexture = new Texture("buttons/buttons_FI/Takaisin.png");
            smallBackButtonTexture = new Texture("buttons/buttons_FI/Takaisin.png");

            // Game Over screen buttons
            enterButtonTexture = new Texture("buttons/buttons_FI/Lisaa.png");
            skipAndRetryButtonTexture = new Texture("buttons/buttons_FI/ohita_ja_juokse.png");
            skipAndBackButtonTexture = new Texture("buttons/buttons_FI/ohita_ja_palaa.png");
            retryButtonTexture = new Texture("buttons/buttons_FI/Pelaa.png");

            // Credits screen buttons
            showCollabButtonTexture = new Texture("buttons/buttons_FI/yhteistyossa.png");
        }
    }
}