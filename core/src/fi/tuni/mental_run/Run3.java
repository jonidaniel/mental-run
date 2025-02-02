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
 * Run3 is for implementing the level 3 run.
 * <p>
 * Run3 draws the level 3 map, its collectibles, the character animation, the points counter and
 * the pause and back to menu buttons.
 * Run3 also checks for user input for these in-game buttons and for character controls.
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
public class Run3 extends GameProject implements Screen {
    Player player = new Player();
    Button button = new Button();
    Lives lives = new Lives();
    Points points = new Points();
    Countdown countdown = new Countdown();
    GameOver gameOver = new GameOver();

    // Defines the y coordinate for new collectibles.
    int collectibleYPos = (int) player.playerSprite.getY() - player.STARTING_POSITION_Y + WINDOW_HEIGHT;

    fi.tuni.mental_run.Collectible collectible = new fi.tuni.mental_run.Collectible();
    SpecialCollectible specialCollectible = new SpecialCollectible(collectibleYPos);
    EnergyDrink energyDrink = new EnergyDrink(collectibleYPos);
    Gaming gaming = new Gaming(collectibleYPos);
    Sandwich sandwich = new Sandwich(collectibleYPos);
    Shower shower = new Shower(collectibleYPos);
    Sleep sleep = new Sleep(collectibleYPos);
    Toothbrush3 toothbrush3 = new Toothbrush3(collectibleYPos);

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

    // Increases character speed every loop.
    // Used in mapLoop().
    int loopSpeedYIncrease = 0;

    int levelVariable = 3;

    // Keeps track of how much the player.playerSprite.getY() had exceeded LOOP_START_RUN3 + LOOP_AMOUNT_RUN3
    // when the map loop was executed.
    // This value is used to compensate when it is defined where to set and draw
    // the character, collectibles, points, lives and in-game buttons after the map loop.
    float excessDistance = 0;

    float xPos;

    /**
     * Constructs Run3.
     *
     * @param host                GameProject host
     * @param musicOn             has info if music is toggled
     * @param soundOn             has info if sounds are toggled
     * @param menuBackgroundMusic the menu background music
     * @param runBackgroundMusic  the run background music
     */
    public Run3(GameProject host, boolean musicOn, boolean soundOn, Music menuBackgroundMusic, Music runBackgroundMusic) {
        this.host = host;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.menuBackgroundMusic = menuBackgroundMusic;
        this.runBackgroundMusic = runBackgroundMusic;
        batch = host.batch;

        gameProjectMap = new TmxMapLoader().load("run3_map/level3map.tmx");
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
        countdown.draw(batch, soundOn);
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
     * Also the character acceleration at the start of the run and when entering the kitchen and hallway is implemented here.
     */
    private void movePlayerY() {
        // Corrects the character y coordinate .
        player.playerSprite.setY(player.playerSprite.getY() + player.PLAYER_SPEED_Y);

        // Corrects the y coordinate for new collectibles.
        collectibleYPos += player.PLAYER_SPEED_Y;

        // These if statements are checked if the special collectible effect is not in force.
        if (specialCollectible.effectDuration <= 0) {
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

            // Increases the character y speed in the kitchen and in the bedroom.
            if (player.playerSprite.getY() > TILE_HEIGHT * 200 + player.STARTING_POSITION_Y) {
                player.PLAYER_SPEED_Y += 2;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
            if (player.playerSprite.getY() > TILE_HEIGHT * 300 + player.STARTING_POSITION_Y) {
                player.PLAYER_SPEED_Y += 2;
                player.PLAYER_SPEED_Y = player.PLAYER_SPEED_Y + loopSpeedYIncrease;
                player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
            }
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
     * which is a variable that compensates differences between player.STARTING_POSITION_Y + LOOP_START_RUN3 + LOOP_AMOUNT_RUN3
     * and player.playerSprite.getY() at the moment in which the loop is initiated.
     * The coordinate player.playerSprite.getY() varies because player.STARTING_POSITION_Y + LOOP_START_RUN3 + LOOP_AMOUNT_RUN3
     * is not always divisible with PLAYER_SPEED_Y.
     */
    private void mapLoop() {
        if (player.playerSprite.getY() >= player.STARTING_POSITION_Y + LOOP_START_RUN3 + LOOP_AMOUNT_RUN3) {
            // Saves the excess distance traveled in a variable
            // in case LOOP_START_RUN3 + LOOP_AMOUNT_RUN3 is not divisible by PLAYER_SPEED_Y.
            excessDistance = player.playerSprite.getY() - (player.STARTING_POSITION_Y + LOOP_START_RUN3 + LOOP_AMOUNT_RUN3);

            // Increases character speed every map loop by 1.
            if (specialCollectible.effectDuration <= 0) {
                loopSpeedYIncrease += 1;
            }

            // Loops the character.
            player.playerSprite.setY(player.STARTING_POSITION_Y + LOOP_START_RUN3 + excessDistance);

            // Loops the contents of the collectible list.
            for (int i = 0; i < list.size(); i++) {
                list.get(i).rectangle.setY(list.get(i).rectangle.getY() - LOOP_AMOUNT_RUN3 + excessDistance);
            }

            // Loops the vertical position of new collectibles.
            collectibleYPos -= LOOP_AMOUNT_RUN3 - excessDistance;

            // Sets the camera on every loop.
            camera.position.y = LOOP_START_RUN3 + WINDOW_HEIGHT / 2;
            camera.update();
        }
    }

    /**
     * Calls each level 3 collectibles' and the special collectible's generateInitialFiller function.
     * <p>
     * Each collectible generates its own initial filler.
     */
    private void generateInitialFillers() {
        specialCollectible.generateFiller();
        energyDrink.generateFiller();
        gaming.generateFiller();
        sandwich.generateFiller();
        shower.generateFiller();
        sleep.generateFiller();
        toothbrush3.generateFiller();
    }

    /**
     * Generates the x coordinate for every level 3 collectible and
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
            energyDrink.rectangle.x = xPos;
            energyDrink.generateFiller();
            list.add(energyDrink);
        }
        if (randomCollectible == 2) {
            generateXPos();
            gaming.rectangle.x = xPos;
            gaming.generateFiller();
            list.add(gaming);
        }
        if (randomCollectible == 3) {
            generateXPos();
            sandwich.rectangle.x = xPos;
            sandwich.generateFiller();
            list.add(sandwich);
        }
        if (randomCollectible == 4) {
            generateXPos();
            shower.rectangle.x = xPos;
            shower.generateFiller();
            list.add(shower);
        }
        if (randomCollectible == 5) {
            generateXPos();
            sleep.rectangle.x = xPos;
            sleep.generateFiller();
            list.add(sleep);
        }
        if (randomCollectible == 6) {
            generateXPos();
            toothbrush3.rectangle.x = xPos;
            toothbrush3.generateFiller();
            list.add(toothbrush3);
        }
    }

    /**
     * Adds a collectible to list every time the collectible's filler has dropped to zero.
     * <p>
     * At this point the collectible's x coordinate has been generated. The x coordinate and
     * a y coordinate (collectibleYPos) is set for the collectible's rectangle.
     */
    private void addCollectibleToList() {
        if (specialCollectible.filler <= 0 && player.playerSprite.getY() > TILE_HEIGHT * 360 + player.STARTING_POSITION_Y) {
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
        if (energyDrink.filler <= 0) {
            generateXPos();
            energyDrink.rectangle.x = xPos;
            energyDrink.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (energyDrink.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(energyDrink);
                    }
                }
            } else {
                list.add(energyDrink);
            }
            energyDrink.generateFiller();
        }
        if (gaming.filler <= 0) {
            generateXPos();
            gaming.rectangle.x = xPos;
            gaming.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (gaming.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(gaming);
                    }
                }
            } else {
                list.add(gaming);
            }
            gaming.generateFiller();
        }
        if (sandwich.filler <= 0) {
            generateXPos();
            sandwich.rectangle.x = xPos;
            sandwich.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (sandwich.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(sandwich);
                    }
                }
            } else {
                list.add(sandwich);
            }
            sandwich.generateFiller();
        }
        if (shower.filler <= 0) {
            generateXPos();
            shower.rectangle.x = xPos;
            shower.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (shower.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(shower);
                    }
                }
            } else {
                list.add(shower);
            }
            shower.generateFiller();
        }
        if (sleep.filler <= 0) {
            generateXPos();
            sleep.rectangle.x = xPos;
            sleep.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (sleep.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(sleep);
                    }
                }
            } else {
                list.add(sleep);
            }
            sleep.generateFiller();
        }
        if (toothbrush3.filler <= 0) {
            generateXPos();
            toothbrush3.rectangle.x = xPos;
            toothbrush3.rectangle.y = collectibleYPos;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (toothbrush3.rectangle.overlaps(list.get(i).rectangle)) {
                        break;
                    }
                    if (i == list.size() - 1) {
                        list.add(toothbrush3);
                    }
                }
            } else {
                list.add(toothbrush3);
            }
            toothbrush3.generateFiller();
        }
    }

    /**
     * Calls each level 3 collectibles' and the special collectible's reduceFiller function.
     * <p>
     * Each collectible reduces its own filler.
     */
    private void reduceFillers() {
        specialCollectible.reduceFiller();
        energyDrink.reduceFiller();
        gaming.reduceFiller();
        sandwich.reduceFiller();
        shower.reduceFiller();
        sleep.reduceFiller();
        toothbrush3.reduceFiller();
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
                    player.PLAYER_SPEED_Y -= 2;
                    player.CAMERA_SPEED_Y = player.PLAYER_SPEED_Y;
                }
                if (list.get(i).id.equals("sandwich")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += sandwich.reward;
                }
                if (list.get(i).id.equals("shower")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += shower.reward;
                }
                if (list.get(i).id.equals("energyDrink")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectibleNegative.play();
                    }
                    lives.counter -= 1;
                }
                if (list.get(i).id.equals("gaming")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectibleNegative.play();
                    }
                    lives.counter -= 1;
                }
                if (list.get(i).id.equals("sleep")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += sleep.reward;
                }
                if (list.get(i).id.equals("toothbrush3")) {
                    if (soundOn) {
                        soundEffectCollectiblePositive.stop();
                        soundEffectCollectibleNegative.stop();
                        soundEffectCollectiblePositive.play();
                    }
                    points.counter += toothbrush3.reward;
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
                    System.out.println("KHLGLHGHLG");
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
        energyDrink.texture.dispose();
        gaming.texture.dispose();
        sandwich.texture.dispose();
        shower.texture.dispose();
        sleep.texture.dispose();
        toothbrush3.texture.dispose();
    }
}