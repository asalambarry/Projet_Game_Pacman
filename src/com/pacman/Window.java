package com.pacman;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * La fenetre du jeu 
 */
public class Window extends JFrame {

    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 500;

    public Window() {
        this.config();
    }

    private void config() {

        this.setTitle("Pacman");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.DARK_GRAY);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

    }

    public void launch() throws Exception {
        this.setVisible(true);
        this.add(new GameController(WINDOW_WIDTH, WINDOW_HEIGHT));
    }   

}
