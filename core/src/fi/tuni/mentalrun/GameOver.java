package fi.tuni.mentalrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * GameOver is for implementing the game over view.
 * <p>
 * GameOver draws the view that was seen when the run ended, a turquoise board, a game over announcement with points
 * the player got together, a question about what the player wants to do next and the back and retry buttons or
 * the skip & retry, skip & back and enter buttons depending on the situation.
 * The player is prompted for a name if they want to submit their high score to the high scores list.
 * GameOver also checks for user input for all of these buttons.
 * GameProject host is forwarded to HighScores, Run1, Run2, Run3 and LevelSelectionMenu.
 * Extends GameProject.
 *
 * @author Joni Mäkinen
 */
public class GameOver extends fi.tuni.mentalrun.GameProject {
    Player player = new Player();
    Button button = new Button();
    fi.tuni.mentalrun.HighScores highScores = new fi.tuni.mentalrun.HighScores();

    Texture crownTexture;

    Rectangle crownRectangle;

    String gameOverIntro;
    String gameOverMessage1;
    String gameOverMessage2;

    String name;
    String noConnectionAnnouncement;

    boolean result;
    boolean resultFetched;
    boolean cameToSubmit;
    boolean noConnection;

    /**
     * Constructs the game over view.
     */
    public GameOver() {
        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        turquoiseBoardTexture = new Texture("backgrounds/turquoise_board.png");
        crownTexture = new Texture("crown.png");

        crownRectangle = new Rectangle();

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));
        soundEffectMadeIt = Gdx.audio.newSound(Gdx.files.internal("sound_effects/made_it.mp3"));
        soundEffectDidNotMakeIt = Gdx.audio.newSound(Gdx.files.internal("sound_effects/did_not_make_it.mp3"));

        gameOverIntro = getLevelText("gameOverIntro");
        gameOverMessage1 = getLevelText("gameOver1");
        gameOverMessage2 = getLevelText("gameOver2");
    }

    /**
     * Draws a turquoise board and on the board a congratulations text and a crown or a text indicating the game is over.
     * <p>
     * Points the player got are also drawn and the buttons back and retry or skip & retry, skip & back and enter
     * depending on if the player made it to the high scores list or not.
     *
     * @param playerPosY          is used in determining the y coordinate for the game over display
     * @param levelVariable       is forwarded further to didIMakeIt and checkInput.
     *                            is the integer value (1, 2 or 3) that has the information on which level's high scores
     *                            are about to be inspected.
     * @param roboto              font used in drawing of congratulations text
     *                            and text indicating the game is over
     * @param largerRoboto        the larger font used in drawing of the points
     * @param host                GameProject host
     * @param batch               batch from Run1, Run2 and Run3. its draw function is called here
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param myPoints            is forwarded further to didIMakeIt and checkInput.
     *                            contains the amount of points the player got. in didIMakeIt, it is compared to the last amount of points
     *                            on the high scores list. In checkInput it is forwarded further.
     */
    public void draw(float playerPosY, int levelVariable, BitmapFont roboto, BitmapFont largerRoboto, GameProject host, SpriteBatch batch, Music menuBackgroundMusic, Music runBackgroundMusic, boolean musicOn, boolean soundOn, String myPoints) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        batch = host.batch;

        int myPointsPosition = 0;

        if (musicOn) {
            runBackgroundMusic.stop();
            menuBackgroundMusic.play();
            menuBackgroundMusic.setLooping(true);
        }

        if (!resultFetched) {
            result = didIMakeIt(levelVariable, Integer.parseInt(myPoints));
            resultFetched = true;
        }

        // Draws the turquoise board.
        batch.draw(turquoiseBoardTexture, 20, playerPosY - player.STARTING_POSITION_Y + 20);

        // These if statements make sure that the points display is horizontally centered regardless of how many digits it has.
        if (Integer.parseInt(myPoints) < 10000000) {
            myPointsPosition = 53;
        }
        if (Integer.parseInt(myPoints) < 1000000) {
            myPointsPosition = 63;
        }
        if (Integer.parseInt(myPoints) < 100000) {
            myPointsPosition = 73;
        }
        if (Integer.parseInt(myPoints) < 10000) {
            myPointsPosition = 83;
        }
        if (Integer.parseInt(myPoints) < 1000) {
            myPointsPosition = 93;
        }
        if (Integer.parseInt(myPoints) < 100) {
            myPointsPosition = 103;
        }
        if (Integer.parseInt(myPoints) < 10) {
            myPointsPosition = 113;
        }

        // This is true if you made it to the high scores list.
        if (result) {
            // Sets position for the enter button after the run has finished.
            // This is because of the altering excessDistance variable.
            button.enterButtonRectangle.setPosition(WINDOW_WIDTH - 40 - button.enterButtonTexture.getWidth(), playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + 5);
            // Sets position for the skip & retry button after the run has finished.
            // This is because of the altering excessDistance variable.
            button.skipAndRetryButtonRectangle.setPosition(40, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + 5);
            // Sets position for the skip & back button after the run has finished.
            // This is because of the altering excessDistance variable.
            button.skipAndBackButtonRectangle.setPosition(WINDOW_WIDTH / 2 - button.skipAndBackButtonTexture.getWidth() / 2, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + 5);

            checkInput(levelVariable, playerPosY, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, result, myPoints);

            // Draws the crown.
            batch.draw(crownTexture, WINDOW_WIDTH / 2 - crownTexture.getWidth() / 2, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 200);
            roboto.draw(batch, gameOverIntro, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + 20, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 70);
            largerRoboto.draw(batch, myPoints, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + myPointsPosition, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 115);
            roboto.draw(batch, gameOverMessage1, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + 20, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 170);

            // Draws the skip & retry, skip & back and enter buttons.
            batch.draw(button.enterButtonTexture, button.enterButtonRectangle.x, button.enterButtonRectangle.y);
            batch.draw(button.skipAndRetryButtonTexture, button.skipAndRetryButtonRectangle.x, button.skipAndRetryButtonRectangle.y);
            batch.draw(button.skipAndBackButtonTexture, button.skipAndBackButtonRectangle.x, button.skipAndBackButtonRectangle.y);
        }

        // This is true if you didn't make it to the high scores list.
        if (!result) {
            // Sets position for the retry button after the run has finished.
            // This is because of the altering excessDistance variable.
            button.retryButtonRectangle.setPosition(WINDOW_WIDTH - 40 - button.retryButtonTexture.getWidth(), playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + 5);
            // Sets position for the small back button after the run has finished.
            // This is because of the altering excessDistance variable.
            button.smallBackButtonRectangle.setPosition(40, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + 5);

            checkInput(levelVariable, playerPosY, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, result, myPoints);

            roboto.draw(batch, gameOverIntro, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + 20, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 70);
            largerRoboto.draw(batch, myPoints, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + myPointsPosition, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 115);
            roboto.draw(batch, gameOverMessage2, WINDOW_WIDTH / 2 - turquoiseBoardTexture.getWidth() / 2 + 20, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 170);

            // Draws the no internet connection announcement.
            if (noConnection) {
                smallerRoboto = host.generateSmallerFont();
                noConnectionAnnouncement = getLevelText("noConnection");

                smallerRoboto.draw(batch, noConnectionAnnouncement, 40, playerPosY - player.STARTING_POSITION_Y + WINDOW_HEIGHT / 2 - turquoiseBoardTexture.getHeight() / 2 + turquoiseBoardTexture.getHeight() - 300);
            }

            // Draws the retry and back buttons.
            batch.draw(button.retryButtonTexture, button.retryButtonRectangle.x, button.retryButtonRectangle.y);
            batch.draw(button.smallBackButtonTexture, button.smallBackButtonRectangle.x, button.smallBackButtonRectangle.y);
        }
    }

    /**
     * Checks for user input for buttons back, retry, skip & retry, skip & back and enter.
     * <p>
     * Also prompts the player for a name in case they want to submit their high score.
     *
     * @param levelVariable       is forwarded further to HighScores where it is used to determine
     *                            which level's high scores to display
     * @param playerPosY          is used when determining the y coordinate for the buttons to be drawn
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     * @param result              tells the function which buttons' input to check
     * @param myPointsString      the amount of the player's points
     */
    public void checkInput(int levelVariable, float playerPosY, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic, boolean result, String myPointsString) {
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;

        // Variable name is given to highScores.createNewScore() only if the input passes the validation.
        // The screen is also set as HighScores.
        // Otherwise the player stays on the screen GameOver.
        if (name != null) {
            highScores.createNewScore(levelVariable, name, myPointsString);
            // Takes you to HighScores.
            host.setScreen(new fi.tuni.mentalrun.HighScores(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic, levelVariable, cameToSubmit, myPointsString));
        }

        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // This is true if you made it to the high scores list.
            if (result) {
                // This is true if the enter button is pressed.
                if (button.enterButtonRectangle.contains(touchPos.x, touchPos.y + playerPosY)) {
                    // This variable is used for the ok button mechanics in HighScores.
                    // Makes the screen set as MainMenu rather than LevelSelectionMenu when the ok button is pressed in HighScores.
                    cameToSubmit = true;
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }

                    // This anonymous inner class implementation prompts the player for their name
                    // when they want to submit their score to the high scores list.
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            // These statements validate the user's input.
                            // Only letters and numbers are accepted and the maximum length for a name is ten characters.
                            if (text.length() <= 10) {
                                int i;
                                for (i = 0; i < text.length(); i++) {
                                    if (!Character.isLetterOrDigit(text.charAt(i))) {
                                        break;
                                    }
                                }
                                // Variable name is set as the user's input if the input successfully passes the validation.
                                if (i == text.length()) {
                                    name = text;
                                }
                            }
                        }

                        @Override
                        public void canceled() {

                        }
                    }, "What shall we call you?", "", "Letters and numbers only, max. 10");

                }
                // This is true if the skip & retry button is pressed.
                if (button.skipAndRetryButtonRectangle.contains(touchPos.x, touchPos.y + playerPosY)) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    if (levelVariable == 1) {
                        // Takes you to Run1.
                        host.setScreen(new fi.tuni.mentalrun.Run1(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                    if (levelVariable == 2) {
                        // Takes you to Run2.
                        host.setScreen(new Run2(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                    if (levelVariable == 3) {
                        // Takes you to Run3.
                        host.setScreen(new fi.tuni.mentalrun.Run3(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                }
                // This is true if the skip & back button is pressed.
                if (button.skipAndBackButtonRectangle.contains(touchPos.x, touchPos.y + playerPosY)) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    // Takes you to LevelSelectionMenu.
                    host.setScreen(new LevelSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                }
            }

            // This is true if you didn't make it to the high scores list.
            if (!result) {
                // This is true if the retry button is pressed.
                if (button.retryButtonRectangle.contains(touchPos.x, touchPos.y + playerPosY)) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    if (levelVariable == 1) {
                        // Takes you to Run1.
                        host.setScreen(new Run1(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                    if (levelVariable == 2) {
                        // Takes you to Run2.
                        host.setScreen(new Run2(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                    if (levelVariable == 3) {
                        // Takes you to Run3.
                        host.setScreen(new Run3(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                    }
                }
                // This is true if the small back button is pressed.
                if (button.smallBackButtonRectangle.contains(touchPos.x, touchPos.y + playerPosY)) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    // Takes you to LevelSelectionMenu.
                    host.setScreen(new LevelSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
                }
            }
        }
    }

    /**
     * Finds out if the player made it to the high scores list or not.
     *
     * @param levelVariable tells the function which level's high scores to compare to the player's score
     * @param myPoints      the amount of the player's points
     * @return Returns result which tells draw if the player made it to the high scores list or not
     */
    public boolean didIMakeIt(int levelVariable, int myPoints) {
        fi.tuni.mentalrun.HighScores checkHighScores = new HighScores(levelVariable);

        // Makes sure that there is an internet connection.
        if (checkHighScores.highScorePointsList.size() != 0) {
            // Sets boolean result as true or false, depending on if you made it to the high scores list or not.
            if (myPoints > checkHighScores.highScorePointsList.get(9)) {
                if (soundOn) {
                    soundEffectMadeIt.play();
                    soundEffectMadeIt.dispose();
                }
                result = true;
            } else {
                if (soundOn) {
                    soundEffectDidNotMakeIt.play();
                    soundEffectDidNotMakeIt.dispose();
                }
                result = false;
            }
        } else {
            noConnection = true;
        }
        return result;
    }
}