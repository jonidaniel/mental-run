package fi.tuni.mental_run.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fi.tuni.mental_run.GameProject;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 288;
        config.height = 480;
        new LwjglApplication(new GameProject(), config);
    }
}
