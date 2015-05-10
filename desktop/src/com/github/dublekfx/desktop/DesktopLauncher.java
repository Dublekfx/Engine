package com.github.dublekfx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.dublekfx.TEMPOrary.TEMPOrary;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "TEMPOrary";
		new LwjglApplication(new TEMPOrary(), config);
	}
}
