package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Run2 is for implementing the level 2 run.
 * <p>
 * Run2 draws the level 2 map, its collectibles, the character animation, the points counter and
 * the pause and back to menu buttons.
 * Run2 also checks for user input for these in-game buttons and for character controls.
 * Tapping pause button pauses the game and tapping back to menu button takes you back to LevelSelectionMenu.
 * Tapping the left side of the screen makes the character move leftwards and tapping the right side
 * makes the character move rightwards.
 * The character's and camera's y coordinate is increased during the run.
 * This makes the character run upwards along the map.
 * The player has three lives at the start of the run. The goal is collect as much positive collectibles
 * as possible and avoid the negative collectibles.
 * If the player mistakenly collects a negative collectible, they will lose a life.
 * When all three lives are lost they are taken to GameOver.
 * GameProject host is forwarded to classes Lives, GameOver, LevelSelectionMenu.
 * Extends GameProject.
 * Implements interface Screen.
 *
 * @author Joni Mäkinen
 */
public class Run2 extends fi.tuni.mental_run.GameProject implements Screen {
    Player player = new Player();
    fi.tuni.mental_run.Button button = new Button();
    Lives lives = new Lives();
    fi.tuni.mental_run.Points points = new Points();
    fi.tuni.mental_run.Countdown countdown = new Countdown();
    fi.tuni.mental_run.GameOver gameOver = new GameOver();

    // Defines the y coordinate for new collectibles.
    int collectibleYPos = (int) player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT;

    fi.tuni.mental_run.Collectible collectible = new fi.tuni.mental_run.Collectible();
    fi.tuni.mental_run.SpecialCollectible specialCollectible = new SpecialCollectible(collectibleYPos);
    fi.tuni.mental_run.Sociability sociability = new Sociability(collectibleYPos);
    fi.tuni.mental_run.Schoolbook schoolbook = new Schoolbook(collectibleYPos);
    fi.tuni.mental_run.Football football = new Football(collectibleYPos);
    Lunch lunch = new Lunch(collectibleYPos);
    Candy candy = new Candy(collectibleYPos);
    fi.tuni.mental_run.MobilePhone mobilePhone = new MobilePhone(collectibleYPos);

    ArrayList<fi.tuni.mental_run.Collectible> list;

    // These variables are used in character control.
    boolean movingLeftLeft;
    boolean movingRightLeft;
    boolean movingLeftRight;
    boolean movingRightRight;
    boolean denyCrossing;
    boolean rightSideTouchedOnce;
    boolean leftSideTouchedOnce;

    // Used in controlling the pause mechanics and controlling the countdown sequence.
    boolean paused;

    // Used in controlling the character acceleration.
    boolean initialLoopDone;

    // If true, the special collectible effect is active
    boolean specialEffectActive;

    // Increases character speed every loop.
    // Used in mapLoop().
    int loopSpeedYIncrease = 0;

    int levelVariable = 2;

    // Keeps track of how much the player.playerSprite.getY() had exceeded LOOP_START_RUN2 + LOOP_AMOUNT_RUN2
    // when the map loop was executed.
    // This value is used to compensate when it is defined where to set and draw
    // the character, collectibles, points, lives and in-game buttons after the map loop.
    float excessDistance = 0;

    float xPos;

    /**
     * Constructs Run2.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     */
    public Run2(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        batch = host.batch;

        gameProjectMap = new TmxMapLoader().load("run2_map/Map2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(gameProjectMap);

        list = new ArrayList<>();

        camera = new OrthographicCamera();
        // yDown: false means that positive Y-axis points up on the screen.
        // Camera width is WINDOW_WIDTH, camera height is WINDOW_HEIGHT.
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        menuBackgroundMusic.stop();
        if (musicOn) {
            runBackgroundMusic.play();
            runBackgroundMusic.setLooping(true);
        }

        soundEffectPressButton = Gdx.audio.newSound(Gdx.files.internal("sound_effects/press_button.mp3"));
        soundEffectSpecialCollectible = Gdx.audio.newSound(Gdx.files.internal("sound_effects/special_collectible.mp3"));
        soundEffectCollectiblePositive = Gdx.audio.newSound(Gdx.files.internal("sound_effects/collectible_positive.mp3"));
        soundEffectCollectibleNegative = Gdx.audio.newSound(Gdx.files.internal("sound_effects/collectible_negative.mp3"));
        soundEffectMadeIt = Gdx.audio.newSound(Gdx.files.internal("sound_effects/made_it.mp3"));
        soundEffectDidNotMakeIt = Gdx.audio.newSound(Gdx.files.internal("sound_effects/did_not_make_it.mp3"));

        turquoiseBoardTexture = new Texture("backgrounds/turquoise_board.png");

        roboto = host.generateFont();
        largerRoboto = host.generateLargerFont();

        addInitialCollectibleToList();
        generateInitialFillers();

        countdown.start();

        // Sets the initial frame to be drawn of the character animation.
        // This enables drawing of the character before the countdown sequence has been completed.
        // In other words, currentFrame is no longer null after this method is executed.
        player.setCurrentFrame();
    }

    public void render(float delta) {
        clearScreen();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // The in-game buttons work even if the game is paused
        // although the countdown sequence has to be completed for them to work.
        if (countdown.completed && !lives.gameOver) {
            // Checks input for the in-game buttons.
            checkInGameButtonsInput();
        }

        // Stops execution of parts of render when the game is paused.
        // Also lets the run begin only after the countdown sequence has been completed.
        if (!paused && countdown.completed && !lives.gameOver) {
            moveCamera();
            movePlayerY();
            mapLoop();

            addCollectibleToList();
            removeCollectibleFromList();

            // Checks input for character controls.
            checkInput();

            checkCollisions();

            reduceFillers();

            // Sets the current frame to be drawn of the character animation.
            player.setCurrentFrame();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Draws all content of the collectible list.
        for (Collectible collectible : list) {
            batch.draw(collectible.texture, collectible.rectangle.x, collectible.rectangle.y - excessDistance, collectible.rectangle.width, collectible.rectangle.height);
        }
        // Draws the character animation.
        batch.draw(player.currentFrame, player.playerSprite.getX(), player.playerSprite.getY() - excessDistance);
        // Draws the point counter.
        roboto.draw(batch, points.stringCounter, points.POINT_COUNTER_POSITION_X, player.playerSprite.getY() + WINDOW_HEIGHT - 10 - excessDistance);
        // Calls lives.draw which draws the life counter.
        lives.draw(batch, player.playerSprite.getY() - excessDistance);
        // Draws the pause button.
        batch.draw(button.pauseButtonTexture, button.IN_GAME_BUTTON_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.IN_GAME_BUTTON_HEIGHT * 2 - excessDistance - 40, button.pauseButtonTexture.getWidth() * 1.3f, button.pauseButtonTexture.getHeight() * 1.3f);
        // Draws the back to menu button.
        batch.draw(button.backToMenuButtonTexture, button.IN_GAME_BUTTON_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.IN_GAME_BUTTON_HEIGHT - excessDistance - 20, button.backToMenuButtonTexture.getWidth() * 1.3f, button.backToMenuButtonTexture.getHeight() * 1.3f);
        // Calls countdown.draw which draws the countdown sequence.
        if (!initialLoopDone) {
            countdown.draw(batch, soundOn);
        }
        // Calls gameOver.draw which draws the game over announcement.
        if (lives.gameOver) {
            gameOver.draw(player.playerSprite.getY() - excessDistance, levelVariable, roboto, largerRoboto, host, batch, menuBackgroundMusic, runBackgroundMusic, musicOn, soundOn, points.stringCounter);
        }
        batch.end();
    }

    /**
     * Clears the screen.
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Moves the character upwards.
     * <p>
     * Also the character acceleration at the start of the run and two times along the way is implemented here.
     */
    private void movePlayerY() {
        // Corrects the character y coordinate .
        player.playerSprite.setY(player.playerSprite.getY() + player.PLAYER_SPEED_Y);

        // Corrects the y coordinate for new collectibles.
        collectibleYPos += player.PLAYER_SPEED_Y;

        // These if statements are checked if the special collectible effect is not in force.
        if (specialCollectible.effectDuration <= 0 && !initialLoopDone) {
            // Accelerates the character to constant speed in the beginning of the run.
            if (player.playerSprite.getY() > 10) {
                player.PLAYER_SPEED_Y = 2;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
            if (player.playerSprite.getY() > 30) {
                player.PLAYER_SPEED_Y = 3;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
            if (player.playerSprite.getY() > 60) {
                player.PLAYER_SPEED_Y = 4;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }

            // Increases the character y speed two times along the way.
            if (player.playerSprite.getY() > TILE_HEIGHT * 100 + player.STARTING_POSITION_Y) {
                player.PLAYER_SPEED_Y += 1;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
            if (player.playerSprite.getY() > TILE_HEIGHT * 140 + player.STARTING_POSITION_Y) {
                player.PLAYER_SPEED_Y += 1;
                player.PLAYER_SPEED_Y = player.PLAYER_SPEED_Y + loopSpeedYIncrease;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
        }

        if (specialCollectible.effectDuration > 0 && !specialEffectActive) {
            player.PLAYER_SPEED_Y -= 2;
            player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            specialEffectActive = true;
        }
        if (specialCollectible.effectDuration <= 0 && specialEffectActive) {
            player.PLAYER_SPEED_Y += 2;
            player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            specialEffectActive = false;
        }
    }

    /**
     * Moves the camera upwards.
     */
    private void moveCamera() {
        camera.translate(0, player.CAMERA_SPEED_Y);
        camera.update();
    }

    /**
     * Loops the map, character, collectibles and the camera. Looping is done so that the run can continue infinitely.
     * <p>
     * Also increases the character's upward speed every loop and keeps record of excessDistance,
     * which is a variable that compensates differences between player.STARTING_POSITION_Y + LOOP_START_RUN2 + LOOP_AMOUNT_RUN2
     * and player.playerSprite.getY() at the moment in which the loop is initiated.
     * The coordinate player.playerSprite.getY() varies because player.STARTING_POSITION_Y + LOOP_START_RUN2 + LOOP_AMOUNT_RUN2
     * is not always divisible with PLAYER_SPEED_Y.
     */
    private void mapLoop() {
        if (player.playerSprite.getY() >= player.STARTING_POSITION_Y + LOOP_START_RUN2 + LOOP_AMOUNT_RUN2) {
            // Saves the excess distance traveled in a variable
            // in case LOOP_START_RUN2 + LOOP_AMOUNT_RUN2 is not divisible by PLAYER_SPEED_Y.
            excessDistance = player.playerSprite.getY() - (player.STARTING_POSITION_Y + LOOP_START_RUN2 + LOOP_AMOUNT_RUN2);

            // Increases character speed every map loop by 1.
            if (specialCollectible.effectDuration <= 0) {
                player.PLAYER_SPEED_Y += 1;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }

            // Loops the character.
            player.playerSprite.setY(player.STARTING_POSITION_Y + LOOP_START_RUN2 + excessDistance);

            // Loops the contents of the collectible list.
            for (int i = 0; i < list.size(); i++) {
                list.get(i).rectangle.setY(list.get(i).rectangle.getY() - LOOP_AMOUNT_RUN2 + excessDistance);
            }

            // Loops the vertical position of new collectibles.
            collectibleYPos -= LOOP_AMOUNT_RUN2 - excessDistance;

            // Sets the camera on every loop.
            camera.position.y = LOOP_START_RUN2 + WINDOW_HEIGHT / 2;
            camera.update();

            initialLoopDone = true;
        }
    }

    /**
     * Calls each level 2 collectibles' and the special collectible's generateInitialFiller function.
     * <p>
     * Each collectible generates its own initial filler.
     */
    private void generateInitialFillers() {
        specialCollectible.generateFiller();
        sociability.generateFiller();
        schoolbook.generateFiller();
        football.generateFiller();
        lunch.generateFiller();
        candy.generateFiller();
        mobilePhone.generateFiller();
    }

    /**
     * Generates the x coordinate for every level 2 collectible and
     * the special collectible every time a collectible's filler drops to zero.
     */
    public void generateXPos() {
        // Randomly generates a horizontal position for new collectibles.
        xPos = MathUtils.random(LEFT_SIDE_BORDER, RIGHT_SIDE_BORDER);

        // These if statements make the new collectibles to be drawn on three different lanes.
        // The + 2 corrects the difference between COLLECTIBLE_WIDTH (32) and PLAYER_WIDTH (36).
        // It centralizes the collectibles on the character's line of run.
        if (xPos <= 117) {
            xPos = player.STEP_LEFT_POSITION + 2;
        }
        if (xPos > 117 && xPos < 171) {
            xPos = player.STEP_CENTER_POSITION + 2;
        }
        if (xPos >= 171) {
            xPos = player.STEP_RIGHT_POSITION + 2;
        }
    }

    /**
     * Adds a randomly chosen collectible to list to be the first collectible drawn on the screen.
     * <p>
     * The special collectible can not be the first collectible drawn.
     */
    private void addInitialCollectibleToList() {
        // Generates a random integer between 1 and 6.
        int randomCollectible = MathUtils.random(5) + 1;

        if (randomCollectible == 1) {
            generateXPos();
            sociability.rectangle.x = xPos;
            sociability.generateFiller();
            list.add(sociability);
        }
        if (randomCollectible == 2) {
            generateXPos();
            schoolbook.rectangle.x = xPos;
            schoolbook.generateFiller();
            list.add(schoolbook);
        }
        if (randomCollectible == 3) {
            generateXPos();
            football.rectangle.x = xPos;
            football.generateFiller();
            list.add(football);
        }
        if (randomCollectible == 4) {
            generateXPos();
            lunch.rectangle.x = xPos;
            lunch.generateFiller();
            list.add(lunch);
        }
        if (randomCollectible == 5) {
            generateXPos();
            candy.rectangle.x = xPos;
            candy.generateFiller();
            list.add(candy);
        }
        if (randomCollectible == 6) {
            generateXPos();
            mobilePhone.rectangle.x = xPos;
            mobilePhone.generateFiller();
            list.add(mobilePhone);
        }
    }

    /**
     * Adds a collectible to list every time the collectible's filler has dropped to zero.
     * <p>
     * At this point the collectible's x coordinate has been generated. The x coordinate and
     * a y coordinate (collectibleYPos) is set for the collectible's rectangle.
     */
    private void addCollectibleToList() {
        if (specialCollectible.filler <= 0 && initialLoopDone) {
            generateXPos();
            specialCollectible.rectangle.x = xPos;
            specialCollectible.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (specialCollectible.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(specialCollectible);
                    }
                }
            } else {
                list.add(specialCollectible);
            }
            specialCollectible.generateFiller();
        }
        if (sociability.filler <= 0) {
            generateXPos();
            sociability.rectangle.x = xPos;
            sociability.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (sociability.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(sociability);
                    }
                }
            } else {
                list.add(sociability);
            }
            sociability.generateFiller();
        }
        if (schoolbook.filler <= 0) {
            generateXPos();
            schoolbook.rectangle.x = xPos;
            schoolbook.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (schoolbook.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(schoolbook);
                    }
                }
            } else {
                list.add(schoolbook);
            }
            schoolbook.generateFiller();
        }
        if (football.filler <= 0) {
            generateXPos();
            football.rectangle.x = xPos;
            football.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (football.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(football);
                    }
                }
            } else {
                list.add(football);
            }
            football.generateFiller();
        }
        if (lunch.filler <= 0) {
            generateXPos();
            lunch.rectangle.x = xPos;
            lunch.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (lunch.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(lunch);
                    }
                }
            } else {
                list.add(lunch);
            }
            lunch.generateFiller();
        }
        if (candy.filler <= 0) {
            generateXPos();
            candy.rectangle.x = xPos;
            candy.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (candy.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(candy);
                    }
                }
            } else {
                list.add(candy);
            }
            candy.generateFiller();
        }
        if (mobilePhone.filler <= 0) {
            generateXPos();
            mobilePhone.rectangle.x = xPos;
            mobilePhone.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (mobilePhone.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(mobilePhone);
                    }
                }
            } else {
                list.add(mobilePhone);
            }
            mobilePhone.generateFiller();
        }
    }

    /**
     * Calls each level 2 collectibles' and the special collectible's reduceFiller function.
     * <p>
     * Each collectible reduces its own filler.
     */
    private void reduceFillers() {
        specialCollectible.reduceFiller();
        sociability.reduceFiller();
        schoolbook.reduceFiller();
        football.reduceFiller();
        lunch.reduceFiller();
        candy.reduceFiller();
        mobilePhone.reduceFiller();
    }

    /**
     * Removes a collectible from list every time a collectible leaves the screen.
     */
    private void removeCollectibleFromList() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).rectangle.y <= player.playerSprite.getY() - player.STARTING_POSITION_Y - collectible.COLLECTIBLE_HEIGHT) {
                list.remove(list.get(i));
            }
        }
    }

    /**
     * Checks for collisions between the character and the collectibles.
     * <p>
     * If the character and a positive collectible collide, a sound is played and points are rewarded.
     * If the character and a negative collectible collide, a different sound is played and the player
     * loses a life.
     * In case of a collision with a special collectible, a third kind of sound is played and the player
     * is rewarded with an approximately three second decrease in character upwards speed.
     */
    private void checkCollisions() {
        // Checks for collisions between the character and the collectibles.
        for (int i = 0; i < list.size(); i++) {
            if ((player.playerSprite.getBoundingRectangle().overlaps(list.get(i).rectangle))) {
                if (list.get(i).id.equals("specialCollectible")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectSpecialCollectible.play();
                    }
                    // The character vertical speed is reduced by 2 for a 300 frame period.
                    specialCollectible.effectDuration = 300;
                }
                if (list.get(i).id.equals("sociability")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += sociability.reward;
                }
                if (list.get(i).id.equals("schoolbook")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += schoolbook.reward;
                }
                if (list.get(i).id.equals("mobilePhone")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectibleNegative.play();
                    }
                    lives.counter -= 1;
                }
                if (list.get(i).id.equals("candy")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectibleNegative.play();
                    }
                    lives.counter -= 1;
                }
                if (list.get(i).id.equals("lunch")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += lunch.reward;
                }
                if (list.get(i).id.equals("football")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += football.reward;
                }
                list.remove(list.get(i));
                points.stringCounter = String.valueOf(points.counter);
            }
        }
    }

    /**
     * Checks for user input for character controls.
     * <p>
     * Also determines what character animation sheet should be used at any given time.
     */
    public void checkInput() {
        if (Gdx.input.justTouched()
                || movingLeftLeft
                || movingRightLeft
                || movingLeftRight
                || movingRightRight) {
            // This if statement makes sure that new coordinates for touchPos are generated only if the screen is touched just now,
            // not every time this function is called from Run1, Run2 or Run3 render.
            if (Gdx.input.justTouched()) {
                touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
            }

            // These if statements define which character animation is used at a specific moment.
            if (!movingLeftLeft && !movingRightLeft && !movingLeftRight && !movingRightRight) {
                player.useWalkForwardSheet = true;
                player.useWalkLeftSheet = false;
                player.useWalkRightSheet = false;
            }
            if (movingLeftLeft) {
                player.useWalkForwardSheet = false;
                player.useWalkLeftSheet = true;
                player.useWalkRightSheet = false;
            }
            if (movingRightLeft) {
                player.useWalkForwardSheet = false;
                player.useWalkLeftSheet = false;
                player.useWalkRightSheet = true;
            }
            if (movingLeftRight) {
                player.useWalkForwardSheet = false;
                player.useWalkLeftSheet = true;
                player.useWalkRightSheet = false;
            }
            if (movingRightRight) {
                player.useWalkForwardSheet = false;
                player.useWalkLeftSheet = false;
                player.useWalkRightSheet = true;
            }

            // Variable denyCrossing is set as true when the character arrives to center lane from far left lane.
            // This is done so that the character does not cross over the center lane and continue traveling to the far right lane.
            // Every time this function is called,
            // this boolean value is set as false so that the character can be ordered from the center lane to the far right lane.
            denyCrossing = false;

            // This if statement checks if there are two quick consecutive right side touches.
            // From far left lane to far right lane.
            // LEFT  ->  CENTER  ->  RIGHT
            if (Gdx.input.justTouched()
                    && touchPos.x > RIGHT_SIDE_BORDER
                    && rightSideTouchedOnce) {
                movingRightRight = true;
            }
            // This if statement checks if there are two quick consecutive left side touches.
            // From far right lane to far left lane.
            // LEFT  <-  CENTER  <-  RIGHT
            if (Gdx.input.justTouched()
                    && touchPos.x < LEFT_SIDE_BORDER
                    && leftSideTouchedOnce) {
                movingLeftLeft = true;
            }

            // This if statement is true when the screen is touched and is executed until the end of character movement.
            // From center lane to far left lane.
            // LEFT  <-  CENTER      RIGHT
            if (movingLeftLeft
                    || !movingRightLeft
                    && touchPos.x <= LEFT_SIDE_BORDER
                    && player.playerSprite.getX() <= player.STEP_CENTER_POSITION
                    && player.playerSprite.getX() > player.STEP_LEFT_POSITION) {
                player.playerSprite.setX(player.playerSprite.getX() - player.PLAYER_SPEED_X);
                movingLeftLeft = true;
                if (player.playerSprite.getX() <= player.STEP_LEFT_POSITION) {
                    player.playerSprite.setX(player.STEP_LEFT_POSITION);
                    movingLeftLeft = false;
                    player.useWalkForwardSheet = true;
                    player.useWalkLeftSheet = false;
                }
            }

            // This if statement is true when the screen is touched and is executed until the end of character movement.
            // From far left lane to center lane.
            // LEFT  ->  CENTER      RIGHT
            if (movingRightLeft
                    || !movingLeftLeft
                    && touchPos.x >= RIGHT_SIDE_BORDER
                    // This row counts out the area at the top of the screen.
                    // Makes the pause button work properly.
                    && touchPos.y < player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.pauseButtonRectangle.height * 2 - 40
                    && player.playerSprite.getX() < player.STEP_CENTER_POSITION
                    && player.playerSprite.getX() >= player.STEP_LEFT_POSITION) {
                player.playerSprite.setX(player.playerSprite.getX() + player.PLAYER_SPEED_X);
                rightSideTouchedOnce = true;
                movingRightLeft = true;
                if (player.playerSprite.getX() >= player.STEP_CENTER_POSITION) {
                    player.playerSprite.setX(player.STEP_CENTER_POSITION);
                    rightSideTouchedOnce = false;
                    movingRightLeft = false;
                    if (!movingRightRight) {
                        player.useWalkForwardSheet = true;
                    }
                    player.useWalkRightSheet = false;
                    denyCrossing = true;
                }
            }

            // This if statement is true if the character is moving rightwards and the left side of the screen is touched.
            // From between center lane and far left lane to far left lane.
            // LEFT <<-> CENTER      RIGHT
            if (movingRightLeft
                    && touchPos.x <= LEFT_SIDE_BORDER
                    && player.playerSprite.getX() < player.STEP_CENTER_POSITION
                    && player.playerSprite.getX() > player.STEP_LEFT_POSITION) {
                rightSideTouchedOnce = false;
                movingRightLeft = false;
                movingLeftLeft = true;
            }

            // This if statement is true if the character is moving leftwards and the right side of the screen is touched.
            // From between center lane and far left lane to center lane.
            // LEFT <->> CENTER      RIGHT
            if (movingLeftLeft
                    && touchPos.x >= RIGHT_SIDE_BORDER
                    // This row counts out the area at the top of the screen.
                    // Makes the pause button work properly.
                    && touchPos.y < player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.pauseButtonRectangle.height * 2 - 40
                    && player.playerSprite.getX() < player.STEP_CENTER_POSITION
                    && player.playerSprite.getX() > player.STEP_LEFT_POSITION) {
                movingLeftLeft = false;
                movingRightLeft = true;
            }

            // This if statement is executed when the screen is touched and is executed until the end of character movement.
            // From far right lane to center lane.
            // LEFT      CENTER  <-  RIGHT
            if (movingLeftRight
                    || !movingRightRight
                    && touchPos.x <= LEFT_SIDE_BORDER
                    && player.playerSprite.getX() <= player.STEP_RIGHT_POSITION
                    && player.playerSprite.getX() > player.STEP_CENTER_POSITION) {
                player.playerSprite.setX(player.playerSprite.getX() - player.PLAYER_SPEED_X);
                leftSideTouchedOnce = true;
                movingLeftRight = true;
                if (player.playerSprite.getX() <= player.STEP_CENTER_POSITION) {
                    player.playerSprite.setX(player.STEP_CENTER_POSITION);
                    leftSideTouchedOnce = false;
                    movingLeftRight = false;
                    if (!movingLeftLeft) {
                        player.useWalkForwardSheet = true;
                    }
                    player.useWalkLeftSheet = false;
                }
            }

            // This if statement is true when the screen is touched and is executed until the end of character movement.
            // From center lane to far right lane.
            // LEFT      CENTER  ->  RIGHT
            if (movingRightRight
                    || !movingLeftRight && !denyCrossing
                    && touchPos.x >= RIGHT_SIDE_BORDER
                    // This row counts out the area at the top of the screen.
                    // Makes the pause button work properly.
                    && touchPos.y < player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.pauseButtonRectangle.height * 2 - 40
                    && player.playerSprite.getX() < player.STEP_RIGHT_POSITION
                    && player.playerSprite.getX() >= player.STEP_CENTER_POSITION) {
                player.playerSprite.setX(player.playerSprite.getX() + player.PLAYER_SPEED_X);
                movingRightRight = true;
                if (player.playerSprite.getX() >= player.STEP_RIGHT_POSITION) {
                    player.playerSprite.setX(player.STEP_RIGHT_POSITION);
                    movingRightRight = false;
                    player.useWalkForwardSheet = true;
                    player.useWalkRightSheet = false;
                }
            }

            // This if statement is true if the character is moving rightwards and the left side of the screen is touched.
            // From between center lane and far right lane to center lane.
            // LEFT      CENTER <<-> RIGHT
            if (movingRightRight
                    && touchPos.x <= LEFT_SIDE_BORDER
                    && player.playerSprite.getX() < player.STEP_RIGHT_POSITION
                    && player.playerSprite.getX() > player.STEP_CENTER_POSITION) {
                movingRightRight = false;
                movingLeftRight = true;
            }

            // This if statement is true if the character is moving leftwards and the right side of the screen is touched.
            // From between center lane and far right lane to far right lane.
            // LEFT      CENTER <->> RIGHT
            if (movingLeftRight
                    && touchPos.x >= RIGHT_SIDE_BORDER
                    // This row counts out the area at the top of the screen.
                    // Makes the pause button work properly.
                    && touchPos.y < player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT - button.pauseButtonRectangle.height * 2 - 40
                    && player.playerSprite.getX() < player.STEP_RIGHT_POSITION
                    && player.playerSprite.getX() > player.STEP_CENTER_POSITION) {
                leftSideTouchedOnce = false;
                movingLeftRight = false;
                movingRightRight = true;
            }
        }
    }

    /**
     * Checks for user input for the pause button and the back to menu button.
     * <p>
     * Pause button pauses the run. Back to menu button takes you back to LevelSelectionMenu.
     */
    public void checkInGameButtonsInput() {
        // Sets position for the pause button on every render call.
        // This is because of the altering excessDistance variable.
        button.pauseButtonRectangle.setPosition(button.IN_GAME_BUTTON_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y - excessDistance + WINDOW_HEIGHT - button.IN_GAME_BUTTON_HEIGHT * 2 - 40);
        // Sets position for back to menu button on every render call.
        // This is because of the altering excessDistance variable.
        button.backToMenuButtonRectangle.setPosition(button.IN_GAME_BUTTON_POSITION_X, player.playerSprite.getY() - player.STARTING_POSITION_Y - excessDistance + WINDOW_HEIGHT - button.IN_GAME_BUTTON_HEIGHT - 20);

        // Checks input for the pause button and for the back to menu button.
        if (Gdx.input.justTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Is true if the pause button is pressed.
            if (button.pauseButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (!paused) {
                    if (musicOn) {
                        runBackgroundMusic.pause();
                    }
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    paused = true;
                    useWalkForwardSheet = true;
                    useWalkLeftSheet = false;
                    useWalkRightSheet = false;
                } else if (paused) {
                    if (musicOn) {
                        runBackgroundMusic.play();
                        menuBackgroundMusic.setLooping(true);
                    }
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                    paused = false;
                    useWalkForwardSheet = true;
                    useWalkLeftSheet = false;
                    useWalkRightSheet = false;
                }
            }

            // Is true if the back to menu button is pressed.
            if (button.backToMenuButtonRectangle.contains(touchPos.x, touchPos.y)) {
                if (musicOn) {
                    runBackgroundMusic.stop();
                    menuBackgroundMusic.play();
                    menuBackgroundMusic.setLooping(true);
                }
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                // Takes you to LevelSelectionMenu.
                host.setScreen(new LevelSelectionMenu(host, musicOn, soundOn, menuBackgroundMusic, runBackgroundMusic));
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
        runBackgroundMusic.dispose();

        soundEffectCountdown321.dispose();
        soundEffectCountdownGo.dispose();
        soundEffectSpecialCollectible.dispose();
        soundEffectCollectiblePositive.dispose();
        soundEffectCollectibleNegative.dispose();
        soundEffectMadeIt.dispose();
        soundEffectDidNotMakeIt.dispose();
        roboto.dispose();
        largerRoboto.dispose();

        player.playerWalkForwardSheet.dispose();
        player.playerWalkLeftSheet.dispose();
        player.playerWalkRightSheet.dispose();
        turquoiseBoardTexture.dispose();
        countdown.number3Texture.dispose();
        countdown.number2Texture.dispose();
        countdown.number1Texture.dispose();
        countdown.goTexture.dispose();
        lives.redHeart.dispose();
        lives.blackHeart.dispose();
        sociability.texture.dispose();
        schoolbook.texture.dispose();
        football.texture.dispose();
        lunch.texture.dispose();
        candy.texture.dispose();
        mobilePhone.texture.dispose();
    }
}