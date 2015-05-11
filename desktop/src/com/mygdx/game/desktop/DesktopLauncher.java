package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GameState;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Irish Frenzy";
        // set screen size to size of monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        config.width = (int)screenSize.getWidth();
        config.height = (int)screenSize.getHeight();
        config.fullscreen = true;
        config.resizable = false;
		new LwjglApplication(new GameState(), config);
	}
}
