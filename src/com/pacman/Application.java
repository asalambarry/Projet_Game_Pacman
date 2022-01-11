package com.pacman;

import javax.swing.SwingUtilities;

/**
 * Main Application
 */
public class Application {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new Window().launch();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
	}
}
