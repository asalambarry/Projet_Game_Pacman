package com.pacman;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

/**
 * Le plateau du labyrinthe
 */
public class Board {

    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private int BLOCK_SIZE = 25;
    private int W_BLOCKS = 15;
    private int H_BLOCKS = 30;
    private int OFFSET = BLOCK_SIZE;
    private final int PACMAN = 2;
    private final int PACGOMME_BLEU = 0;
    private final int PACGOMME_VOILET = 4;
    private final int PACGOMME_ORANGE = 5;
    private final int PACGOMME_VERT = 6;
    private final Color ORANGE_COLOR = new Color(255, 165, 0); 
    private final Color PURPLE_COLOR = new Color(128,0,128);
    private int[][] data;
    
    public Board(int w, int h) {
        this.WINDOW_WIDTH = w;
        this.WINDOW_HEIGHT = h;
    }

    public void setData(int[][] data) {
        this.data = data;
    }
    
    public void drawMaze(Graphics2D g2d) {

        g2d.clearRect(OFFSET, OFFSET, BLOCK_SIZE * H_BLOCKS, BLOCK_SIZE * W_BLOCKS);
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRect(OFFSET, OFFSET, BLOCK_SIZE * H_BLOCKS, BLOCK_SIZE * W_BLOCKS);
        g2d.setColor(new Color(255, 255, 10));
        g2d.drawRect(OFFSET, OFFSET, BLOCK_SIZE * H_BLOCKS, BLOCK_SIZE * W_BLOCKS);
        g2d.setColor(Color.YELLOW);
    }

    public void drawMazeContent(Graphics2D g2d, Pacman pacman) {

        for(int i = 0; i < W_BLOCKS; ++i) {
            for(int j = 0; j < H_BLOCKS; ++j) {
                int y = OFFSET + i * BLOCK_SIZE,
                    x = OFFSET + j * BLOCK_SIZE;
                if(data[i][j] == 1) {
                    g2d.setColor(new Color(200, 190, 89));
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
                else {
                    switch(data[i][j]) {
                    case PACMAN:
                        g2d.setColor(pacman.getColor());

                        int startAngle = pacman.getDirection() == Direction.UP ? 110 :
                            (pacman.getDirection() == Direction.DOWN ? 290 :
                            pacman.getDirection() == Direction.LEFT ? 200 : 20);
                        int arcAngle = pacman.isMouthOpen() ? 340 : 360;
                        pacman.setMouthOpen(!pacman.isMouthOpen());

                        g2d.fillArc(x, y, 20, 20, startAngle, arcAngle);
                        pacman.setX(i);
                        pacman.setY(j);
                        break;
                    case PACGOMME_VOILET:
                        drawPacgomme(g2d, PURPLE_COLOR, x, y);
                        break;
                    case PACGOMME_ORANGE:
                        drawPacgomme(g2d, ORANGE_COLOR, x, y);
                        break;
                    case PACGOMME_VERT:
                        drawPacgomme(g2d, Color.GREEN, x, y);
                        break;
                    case PACGOMME_BLEU:
                        g2d.setColor(Color.BLUE);
                        g2d.fillArc(x + BLOCK_SIZE/2, y + BLOCK_SIZE/2, 2, 2, 0, 360);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Mettez les Fantômes sur labyrinthe
     * 
     * @param g2d
     * @param fantomes
     * @param fantomePos
     */
    public void drawFantomes(Graphics2D g2d, Fantome[] fantomes, int[][] fantomePos) {
        for (int i = 0; i < fantomePos.length; i++) {

            int y = OFFSET + fantomePos[i][0] * BLOCK_SIZE,
                x = OFFSET + fantomePos[i][1] * BLOCK_SIZE;

            int xPoints[] = {x + 0, x + 10, x + 20, x + 16, x + 12, x + 10, x + 6};
            int yPoints[] = {y + 20, y + 0, y + 20, y + 16, y + 20, y + 16, y + 20};
            g2d.setColor(fantomes[0].getColor());
            g2d.fillPolygon(xPoints, yPoints, xPoints.length);

            fantomes[i].setX(fantomePos[i][0]);
            fantomes[i].setY(fantomePos[i][1]);
        }
    }

    public void drawPacgomme(Graphics2D g2d, Color color, int x, int y) {
        g2d.setColor(color);
        g2d.fillArc(x, y, 20, 20, 0, 360);
    }

    public void drawWelcomeMsg(Graphics2D g2d) {
        String s = "Press space bar to start.";
        showPopMessage(g2d, s, new Color(32, 32, 48));
    }

    public void drawDiedMsg(Graphics2D g2d) {
        String s = "You lost !. Press space bar to restart";
        showPopMessage(g2d, s, new Color(120, 32, 32));
    }

    public void drawWonMsg(Graphics2D g2d) {
        String s = "You won !. Press space bar to continue";
        showPopMessage(g2d, s, new Color(32, 120, 32));
    }

    private void showPopMessage(Graphics2D g2d, String text, Color bgcolor) {
        g2d.setColor(bgcolor);
        g2d.fillRect(140, WINDOW_HEIGHT/2 - 60, 500, 50);
        g2d.drawRect(140, WINDOW_HEIGHT/2 - 60, 500, 50);

        Font small = new Font("Helvetica", Font.BOLD, 20);
        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(text, WINDOW_WIDTH/3 - 80, WINDOW_HEIGHT/2 - 30);
    }

    public void drawScore(Graphics2D g, int score) {

        String s;
        g.setFont(smallFont);
        g.setColor(Color.WHITE);
        s = "Score: " + score;
        g.drawString(s, WINDOW_WIDTH/3 - 50, WINDOW_HEIGHT - 60);
    }

    public void drawInvisibleMsg(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(Color.GREEN);
        g.drawString("Invisible", WINDOW_WIDTH/2 + 150, WINDOW_HEIGHT - 60);
    }

    public void drawSuperPacmanMsg(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(Color.GREEN);
        g.drawString("Super Pacman", WINDOW_WIDTH/2 + 230, WINDOW_HEIGHT - 60);
    }

    public void drawLife(Graphics2D g, int nb_life) {
        String s;
        g.setFont(smallFont);
        g.setColor(Color.WHITE);
        s = "Life: ";
        int x = WINDOW_WIDTH/2;
        int y = WINDOW_HEIGHT - 60;
        g.drawString(s, x, y);
        for(int i = 0; i < nb_life; ++i) {
            g.fillArc(x + 40 + (21 * i), y - 14, 16, 16, 0, 360);
        }
    }

    public void drawLevel(Graphics2D g, int current_level) {
        String s;
        g.setFont(smallFont);
        g.setColor(Color.WHITE);
        s = "Level: " + current_level;
        int x = BLOCK_SIZE;
        int y = WINDOW_HEIGHT - 60;
        g.drawString(s, x, y);
    }
}
