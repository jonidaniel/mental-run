package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * HighScores is for implementing the high scores view.
 * <p>
 * HighScores draws the background animation, a turquoise high scores board with a header
 * which tells which level's high scores are currently viewed, a list of the best ten scores for the level
 * and an ok button.
 * HighScores also checks for user input for the ok button.
 * Ok button takes you back HighScoresSelectionMenu.
 * GameProject host is forwarded to HighScoresSelectionMenu.
 * Extends GameProject.
 * Implements interfaces HighScoreListener and Screen.
 *
 * @author Joni Mäkinen
 */
public class HighScores extends fi.tuni.mental_run.GameProject implements HighScoreListener, Screen {
    MenuBackground menuBackground = new MenuBackground();
    Button button = new Button();

    Texture level1HighScoresHeaderTexture;
    Texture level2HighScoresHeaderTexture;
    Texture level3HighScoresHeaderTexture;

    String myPointsString;
    String connectionError;

    boolean myHighScoreFound;
    boolean cameToSubmit;

    int levelVariable;
    int myStanding = 10;

    /**
     * Constructs the high scores views.
     * <p>
     * Is called in HighScoresSelectionMenu.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param levelVariable       tells which levels' high scores to fetch
     * @param statetime           makes the screen draw the correct current frame of the animation
     */
    public HighScores(fi.tuni.mental_run.GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, int levelVariable, float statetime) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        this.statetime = statetime;
        this.levelVariable = levelVariable;
        batch = host.batch;

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.fetchHighScores(this, levelVariable);

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));

        widerTurquoiseBoardTexture = new Texture("backgrounds/wider_turquoise_board.png");
        level1HighScoresHeaderTexture = new Texture("headers/level_1_high_score_header.png");
        level2HighScoresHeaderTexture = new Texture("headers/level_2_high_score_header.png");
        level3HighScoresHeaderTexture = new Texture("headers/level_3_high_score_header.png");

        roboto = host.generateFont();
        yellowRoboto = host.generateYellowFont();

        connectionError = getLevelText("connectionError");
    }

    /**
     * Constructs the high scores views.
     * <p>
     * Is called in GameOver.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param levelVariable       tells which levels' high scores to fetch
     * @param cameToSubmit        if true, player came from GameOver
     * @param myPointsString      has players points in string format
     */
    public HighScores(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, int levelVariable, boolean cameToSubmit, String myPointsString) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        this.levelVariable = levelVariable;
        this.cameToSubmit = cameToSubmit;
        this.myPointsString = myPointsString;
        batch = host.batch;

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.fetchHighScores(this, levelVariable);

        widerTurquoiseBoardTexture = new Texture("backgrounds/wider_turquoise_board.png");
        level1HighScoresHeaderTexture = new Texture("headers/level_1_high_score_header.png");
        level2HighScoresHeaderTexture = new Texture("headers/level_2_high_score_header.png");
        level3HighScoresHeaderTexture = new Texture("headers/level_3_high_score_header.png");

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));

        roboto = host.generateFont();
        yellowRoboto = host.generateYellowFont();

        connectionError = getLevelText("connectionError");
    }

    /**
     * Constructor for HighScores.
     *
     * @param levelVariable tells which levels' high scores to fetch
     */
    public HighScores(int levelVariable) {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.fetchHighScores(this, levelVariable);
    }

    /**
     * Constructs the high scores views.
     */
    public HighScores() {

    }

    @Override
    public void receiveHighScore(List<fi.tuni.mental_run.HighScoreEntry> highScores) {
        Gdx.app.log("GameProject", "Received new high scores successfully");
        updateScores(highScores);
    }

    @Override
    public void receiveSendReply(int httpResponse) {
        Gdx.app.log("GameProject", "Received response from server: " + httpResponse);
        HighScoreServer.fetchHighScores(this, levelVariable);
    }

    @Override
    public void failedToRetrieveHighScores(String s) {
        Gdx.app.error("GameProject", "Something went wrong while getting high scores");
    }

    @Override
    public void failedToSendHighScore(String s) {
        Gdx.app.error("GameProject", "Something went wrong while sending a high scoreField entry");
    }

    public ArrayList<String> highScoreNameList = new ArrayList<>();
    public ArrayList<Integer> highScorePointsList = new ArrayList<>();

    /**
     * Two lists, highScoreNameList and highScorePointsList are updated here every time
     * the high scores board is viewed or a new high score is added to it.
     *
     * @param scores a List from receiveHighScore containing the ten best high score entries
     */
    private void updateScores(List<fi.tuni.mental_run.HighScoreEntry> scores) {
        for (fi.tuni.mental_run.HighScoreEntry e : scores) {
            String entry = e.getName();
            highScoreNameList.add(entry);
        }
        for (fi.tuni.mental_run.HighScoreEntry e : scores) {
            int entry = e.getScore();
            highScorePointsList.add(entry);
        }
        findMyScore();
    }

    /**
     * Finds the newly submitted high score standing from highScorePointsList.
     * <p>
     * That information is used in render when the high scores board is viewed right after submitting a score.
     * In render, the submitted high score is highlighted in a yellow color so that the player knows
     * which high score was the one they just submitted.
     */
    private void findMyScore() {
        if (!myHighScoreFound) {
            for (int i = highScorePointsList.size() - 1; i >= 0; i--) {
                if (highScorePointsList.get(i) == Integer.parseInt(myPointsString) && !myHighScoreFound) {
                    myStanding = i;
                    myHighScoreFound = true;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkInput(cameToSubmit);

        statetime += Gdx.graphics.getDeltaTime();
        menuBackground.setCurrentFrame(statetime);

        batch.begin();
        batch.draw(menuBackground.currentFrame, menuBackground.menuBackgroundRectangle.x, menuBackground.menuBackgroundRectangle.y, WINDOW_WIDTH, WINDOW_HEIGHT);
        batch.draw(widerTurquoiseBoardTexture, 4, 40);
        if (levelVariable == 1) {
            batch.draw(level1HighScoresHeaderTexture, WINDOW_WIDTH / 2 - level1HighScoresHeaderTexture.getWidth(), 375, level1HighScoresHeaderTexture.getWidth() * 2, level1HighScoresHeaderTexture.getHeight() * 2);
        }
        if (levelVariable == 2) {
            batch.draw(level2HighScoresHeaderTexture, WINDOW_WIDTH / 2 - level2HighScoresHeaderTexture.getWidth(), 375, level1HighScoresHeaderTexture.getWidth() * 2, level1HighScoresHeaderTexture.getHeight() * 2);
        }
        if (levelVariable == 3) {
            batch.draw(level3HighScoresHeaderTexture, WINDOW_WIDTH / 2 - level3HighScoresHeaderTexture.getWidth(), 375, level1HighScoresHeaderTexture.getWidth() * 2, level1HighScoresHeaderTexture.getHeight() * 2);
        }

        // Makes sure that there is an internet connection.
        if (highScoreNameList.size() != 0) {
            drawNamesAndScores();
        } else {
            roboto.draw(batch, connectionError, 90, 350);
        }

        batch.draw(button.smallOkButtonTexture, button.smallOkButtonRectangle.x, button.smallOkButtonRectangle.y, button.smallOkButtonRectangle.width, button.smallOkButtonRectangle.height);
        batch.end();
    }

    /**
     * Draws names and scores on high scores board.
     * <p>
     * Newly added standing is highlighted in yellow.
     */
    private void drawNamesAndScores() {
        // Draws the names on the high score list.
        // The standing added just now is drawn in yellow.
        if (myStanding == 0) {
            yellowRoboto.draw(batch, "1.   " + highScoreNameList.get(0), 15, 390);
        } else {
            roboto.draw(batch, "1.   " + highScoreNameList.get(0), 15, 390);
        }
        if (myStanding == 1) {
            yellowRoboto.draw(batch, "2.   " + highScoreNameList.get(1), 15, 360);
        } else {
            roboto.draw(batch, "2.   " + highScoreNameList.get(1), 15, 360);
        }
        if (myStanding == 2) {
            yellowRoboto.draw(batch, "3.   " + highScoreNameList.get(2), 15, 330);
        } else {
            roboto.draw(batch, "3.   " + highScoreNameList.get(2), 15, 330);
        }
        if (myStanding == 3) {
            yellowRoboto.draw(batch, "4.   " + highScoreNameList.get(3), 15, 300);
        } else {
            roboto.draw(batch, "4.   " + highScoreNameList.get(3), 15, 300);
        }
        if (myStanding == 4) {
            yellowRoboto.draw(batch, "5.   " + highScoreNameList.get(4), 15, 270);
        } else {
            roboto.draw(batch, "5.   " + highScoreNameList.get(4), 15, 270);
        }
        if (myStanding == 5) {
            yellowRoboto.draw(batch, "6.   " + highScoreNameList.get(5), 15, 240);
        } else {
            roboto.draw(batch, "6.   " + highScoreNameList.get(5), 15, 240);
        }
        if (myStanding == 6) {
            yellowRoboto.draw(batch, "7.   " + highScoreNameList.get(6), 15, 210);
        } else {
            roboto.draw(batch, "7.   " + highScoreNameList.get(6), 15, 210);
        }
        if (myStanding == 7) {
            yellowRoboto.draw(batch, "8.   " + highScoreNameList.get(7), 15, 180);
        } else {
            roboto.draw(batch, "8.   " + highScoreNameList.get(7), 15, 180);
        }
        if (myStanding == 8) {
            yellowRoboto.draw(batch, "9.   " + highScoreNameList.get(8), 15, 150);
        } else {
            roboto.draw(batch, "9.   " + highScoreNameList.get(8), 15, 150);
        }
        if (myStanding == 9) {
            yellowRoboto.draw(batch, "10. " + highScoreNameList.get(9), 15, 120);
        } else {
            roboto.draw(batch, "10. " + highScoreNameList.get(9), 15, 120);
        }

        // Draws the points on the high scores list.
        // The standing added just now is drawn in yellow.
        if (myStanding == 0) {
            yellowRoboto.draw(batch, highScorePointsList.get(0).toString(), 200, 390);
        } else {
            roboto.draw(batch, highScorePointsList.get(0).toString(), 200, 390);
        }
        if (myStanding == 1) {
            yellowRoboto.draw(batch, highScorePointsList.get(1).toString(), 200, 360);
        } else {
            roboto.draw(batch, highScorePointsList.get(1).toString(), 200, 360);
        }
        if (myStanding == 2) {
            yellowRoboto.draw(batch, highScorePointsList.get(2).toString(), 200, 330);
        } else {
            roboto.draw(batch, highScorePointsList.get(2).toString(), 200, 330);
        }
        if (myStanding == 3) {
            yellowRoboto.draw(batch, highScorePointsList.get(3).toString(), 200, 300);
        } else {
            roboto.draw(batch, highScorePointsList.get(3).toString(), 200, 300);
        }
        if (myStanding == 4) {
            yellowRoboto.draw(batch, highScorePointsList.get(4).toString(), 200, 270);
        } else {
            roboto.draw(batch, highScorePointsList.get(4).toString(), 200, 270);
        }
        if (myStanding == 5) {
            yellowRoboto.draw(batch, highScorePointsList.get(5).toString(), 200, 240);
        } else {
            roboto.draw(batch, highScorePointsList.get(5).toString(), 200, 240);
        }
        if (myStanding == 6) {
            yellowRoboto.draw(batch, highScorePointsList.get(6).toString(), 200, 210);
        } else {
            roboto.draw(batch, highScorePointsList.get(6).toString(), 200, 210);
        }
        if (myStanding == 7) {
            yellowRoboto.draw(batch, highScorePointsList.get(7).toString(), 200, 180);
        } else {
            roboto.draw(batch, highScorePointsList.get(7).toString(), 200, 180);
        }
        if (myStanding == 8) {
            yellowRoboto.draw(batch, highScorePointsList.get(8).toString(), 200, 150);
        } else {
            roboto.draw(batch, highScorePointsList.get(8).toString(), 200, 150);
        }
        if (myStanding == 9) {
            yellowRoboto.draw(batch, highScorePointsList.get(9).toString(), 200, 120);
        } else {
            roboto.draw(batch, highScorePointsList.get(9).toString(), 200, 120);
        }
    }

    /**
     * Checks for user input for the ok button.
     * <p>
     * Ok button takes you to MainMenu or HighScoresSelectionMenu depending on cameToSubmit's value.
     * If the player has come to HighScores for submitting a high score, they are taken to MainMenu,
     * if they came from HighScoresSelectionMenu, they are taken to HighScoresMenu.
     *
     * @param cameToSubmit if true, player came from GameOver. determines which screen is used when the ok button is pressed:
     *                     main menu or high scores selection menu
     */
    public void checkInput(boolean cameToSubmit) {
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Is true if the player has just submitted their high score and pressed the ok button.
            if (button.okButtonRectangle.contains(touchPos.x, touchPos.y) && cameToSubmit) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to MainMenu.
                host.setScreen(new MainMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
            }

            if (!cameToSubmit) {
                // If statement is true if the player is only viewing the high scores and not submitting a score.
                if (button.okButtonRectangle.contains(touchPos.x, touchPos.y)) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    // Takes you to HighScoresSelectionMenu.
                    host.setScreen(new HighScoresSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, statetime));
                }
            }
        }
    }

    /**
     * Fetches the high scores from HighScoreServer,
     * transforms scoreString, which is the newly submitted score, into an integer form,
     * creates scoreEntry, which contains the name which the player has given and the score the player has acquired
     * and sends levelVariable and scoreEntry to sendNewHighScore at HighScoreServer.
     *
     * @param levelVariable an integer value (1, 2 or 3) that has the information on
     *                      what level's high scores createNewScore is supposed to fetch and send
     * @param name          the name that the player has given during submitting a high score
     * @param scoreString   the string value of the new about to be submitted high score.
     *                      Is transformed into integer score
     */
    public void createNewScore(int levelVariable, String name, String scoreString) {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.fetchHighScores(this, levelVariable);

        int score = Integer.parseInt(scoreString);
        fi.tuni.mental_run.HighScoreEntry scoreEntry = new HighScoreEntry(name, score);
        HighScoreServer.sendNewHighScore(levelVariable, scoreEntry, this);
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
        widerTurquoiseBoardTexture.dispose();
        level1HighScoresHeaderTexture.dispose();
        level2HighScoresHeaderTexture.dispose();
        level3HighScoresHeaderTexture.dispose();
        button.smallOkButtonTexture.dispose();

        roboto.dispose();
        yellowRoboto.dispose();
    }
}