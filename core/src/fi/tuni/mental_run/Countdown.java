package fi.tuni.mental_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Countdown is for implementing the countdown sequence.
 * <p>
 * Countdown draws the numbers 3, 2 and 1 and "Go!" before the run starts.
 * Countdown also draws the instruction about character controls.
 *
 * @author Joni Mäkinen
 */
public class Countdown extends GameProject {
    Timer timer;

    Texture number3Texture;
    Texture number2Texture;
    Texture number1Texture;
    Texture goTexture;

    Rectangle rectangle;

    String controls;

    final int COUNTDOWN_WIDTH = 200;
    final int COUNTDOWN_HEIGHT = 200;

    int secondsLeft = 4;
    boolean completed;

    boolean initialBeep3Done;
    boolean initialBeep2Done;
    boolean initialBeep1Done;
    boolean initialBeepGoDone;

    /**
     * A constructor for creating a countdown timer, rectangle, textures, sound effects, a font and an instruction text.
     */
    public Countdown() {
        timer = new Timer();

        roboto = generateFont();
        controls = getLevelText("controls");

        soundEffectCountdown321 = Gdx.audio.newSound(Gdx.files.internal("sound_effects/countdown321.mp3"));
        soundEffectCountdownGo = Gdx.audio.newSound(Gdx.files.internal("sound_effects/countdown_go.mp3"));

        number3Texture = new Texture("countdown/countdown_3.png");
        number2Texture = new Texture("countdown/countdown_2.png");
        number1Texture = new Texture("countdown/countdown_1.png");
        goTexture = new Texture("countdown/countdown_go.png");

        rectangle = new Rectangle(WINDOW_WIDTH / 2 - COUNTDOWN_WIDTH / 2, WINDOW_HEIGHT / 2 - COUNTDOWN_HEIGHT / 2, COUNTDOWN_WIDTH, COUNTDOWN_HEIGHT);
    }

    /**
     * Starts the countdown sequence.
     */
    public void start() {
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // A task for timer.
    TimerTask task = new TimerTask() {
        /**
         * This function runs the task of timer.
         */
        public void run() {
            if (secondsLeft > 0) {
                secondsLeft--;
                if (secondsLeft > 0) {
                    if (soundOn) {
                        soundEffectPressButton.play();
                    }
                }
            }
            if (secondsLeft == 0) {
                if (soundOn) {
                    soundEffectPressButton.play();
                }
                timer.cancel();
                completed = true;
            }
        }
    };

    /**
     * Draws the countdown sequence and the instruction text below it.
     *
     * @param batch   from Run1, Run2 or Run3. Its draw function is called in draw
     * @param soundOn tells the function whether to play the countdown sequence sounds or not
     */
    public void draw(SpriteBatch batch, boolean soundOn) {
        // Draws the instruction text.
        roboto.draw(batch, controls, 20, 120);

        // Draws the countdown sequence.
        if (secondsLeft == 3) {
            if (soundOn && !initialBeep3Done) {
                soundEffectCountdown321.play();
                initialBeep3Done = true;
            }
            batch.draw(number3Texture, rectangle.getX(), rectangle.getY(), rectangle.width, rectangle.height);
        }
        if (secondsLeft == 2) {
            if (soundOn && !initialBeep2Done) {
                soundEffectCountdown321.play();
                initialBeep2Done = true;
            }
            batch.draw(number2Texture, rectangle.getX(), rectangle.getY(), rectangle.width, rectangle.height);
        }
        if (secondsLeft == 1) {
            if (soundOn && !initialBeep1Done) {
                soundEffectCountdown321.play();
                initialBeep1Done = true;
            }
            batch.draw(number1Texture, rectangle.getX(), rectangle.getY(), rectangle.width, rectangle.height);
        }
        if (secondsLeft == 0) {
            if (soundOn && !initialBeepGoDone) {
                soundEffectCountdownGo.play();
                initialBeepGoDone = true;
            }
            batch.draw(goTexture, rectangle.getX(), rectangle.getY(), rectangle.width, rectangle.height);
        }
    }
}