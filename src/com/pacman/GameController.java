package com.pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

/**
 * Le contrôleur de jeu
 */
public class GameController extends JPanel implements ActionListener {

    private int score;
    private int nb_life;
    private Timer timer;
    private final int OPAQUE = 1;
    private final int PACMAN = 2;
    private final int PACGOMME_BLEU = 0;
    private final int PACGOMME_VOILET = 4;
    private final int PACGOMME_ORANGE = 5;
    private final int PACGOMME_VERT = 6;
    private final int FANTOME_1 = 7;
    private final int FANTOME_2 = 8;
    private final int FANTOME_3 = 9;
    private final int FANTOME_4 = 10;
    private final Color ORANGE_COLOR = new Color(255, 165, 0); 
    private final Color YELLOW_DARK_COLOR = new Color(100, 100, 0);

    private int[][] data;
    private int[][] fantomePos;

    private int W_BLOCKS = 15;
    private int H_BLOCKS = 30;
    private Pacman pacman;
    private Fantome[] fantomes;
    private boolean lifeFirstRoundAdded = false;
    private Direction keyDirection;
    private boolean directionPressed = false;
    private boolean isRunning = false;
    private boolean isDied = false;
    private boolean isWon = false;
    private boolean isPacmanWasHurt = false;
    private int countHurt = 10;
    private final int MAX_LEVEL = 2;
    private int current_level = 1;
    private Board board;

    public GameController(int w, int h) {
        board = new Board(w, h);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        timer = new Timer(100, this);
        this.initGame();
    }

    public void initGame() {

        // C'est un niveau supérieur
        if(isWon) {
            current_level++;
            current_level = current_level > MAX_LEVEL ? 1 : current_level;
        }

        isRunning = false;
        isDied = false;
        isWon = false;
        isPacmanWasHurt = false;
        nb_life = 3;
        score = 0;

        // Obtenir les données 
        data = Arrays
            .stream(current_level == 1 ? LevelData.level1Data : LevelData.level2Data)
            .map(int[]::clone)
            .toArray(int[][]::new);
        board.setData(data);

        // La position des fantomes
        fantomePos = Arrays
            .stream(current_level == 1 ? LevelData.level1FantomePos : LevelData.level2FantomePos)
            .map(int[]::clone)
            .toArray(int[][]::new);

        // Créer Pacman et Fantômes
        pacman = new Pacman(PACMAN, 1);

        int speedFantome = 2;
        fantomes = new Fantome[] {
            new Fantome(FANTOME_1, speedFantome, Direction.DOWN),
            new Fantome(FANTOME_2, speedFantome, Direction.UP),
            new Fantome(FANTOME_3, speedFantome, Direction.LEFT),
            new Fantome(FANTOME_4, speedFantome, Direction.RIGHT),
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAllContent(g);
    }

    private void drawAllContent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        board.drawMaze(g2d);
        board.drawMazeContent(g2d, pacman);
        board.drawFantomes(g2d, fantomes, fantomePos);

        if(isWon) {
            board.drawWonMsg(g2d);
        }
        else if(isDied) {
            board.drawDiedMsg(g2d);
        }
        else if( ! isRunning) {
            board.drawWelcomeMsg(g2d);
        }
        board.drawScore(g2d, score);
        board.drawLife(g2d, nb_life);
        board.drawLevel(g2d, current_level);

        if(pacman.power.isInvisible()) {
            board.drawInvisibleMsg(g2d);
        }

        if(pacman.power.isSuperPacman()) {
            board.drawSuperPacmanMsg(g2d);
        }

    }

    /**
     * Gérer la logique du jeu
     * Déplacez les personnages, gérez la vie, gagnez, marquez
     */
    private void handleGameLogic() {

        // Dors un peu quand le pacman a été blessé
        if(isPacmanWasHurt) {
            this.pacmanWasHurt();
            return;
        }

        // Déplacez le pacman
        this.movePacman();

        // Mettre à jour le nb de vie
        if(score > 5000 && !lifeFirstRoundAdded) {
            nb_life++;
            lifeFirstRoundAdded = true;
        }

        // Vérifier la victoire
        if( ! isMorePacgommes()) {
            isWon = true;
            isDied = false;
            this.stopRunning();
        }

        // Permet de rechercher s'il a perdu
        if(nb_life <= 0) {
            isDied = true;
            isWon = false;
            this.stopRunning();
        }

        // Déplacer les méchants
        for (int i = 0; i < fantomePos.length; i++) {
            this.animVillains(i);
        }

        // Recherche la collisions
        this.checkCollision();

    }

    private void stopRunning() {
        isRunning = false;
        timer.stop();
        repaint();
    }

    /**
     * Vérifiez s'il y a plus de pacgommes
     * @return
     */
    private boolean isMorePacgommes() {

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j] == PACGOMME_BLEU || data[i][j] == PACGOMME_VOILET || 
                    data[i][j] == PACGOMME_ORANGE ||  data[i][j] == PACGOMME_VERT) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Déplacer le pacman lorsqu'une touche de direction est enfoncée
     */
    private void movePacman() {

        if(directionPressed) {

            int x = pacman.getX(),
                y = pacman.getY();
            pacman.setDirection(keyDirection);

            switch (keyDirection) {

                case UP:
                    if(x > 0) {
                        this.manageNextCase(x, y, x - 1, y);
                    }
                    else {
                        this.manageNextCase(x, y, W_BLOCKS - 1, y);
                    }
                    break;

                case DOWN:
                    if(x < W_BLOCKS - 1) {
                        this.manageNextCase(x, y, x + 1, y);
                    }
                    else {
                        this.manageNextCase(x, y, 0, y);
                    }
                    break;

                case LEFT:
                    if(y > 0) {
                        this.manageNextCase(x, y, x, y - 1);
                    }
                    else {
                        this.manageNextCase(x, y, x, H_BLOCKS - 1);
                    }
                    break;

                case RIGHT:
                    if(y < H_BLOCKS - 1) {
                        this.manageNextCase(x, y, x, y + 1);
                    }
                    else {
                        this.manageNextCase(x, y, x, 0);
                    }
                    break;

            }
        }
    }

    /**
     * Anime Fantomes
     * 
     * @param index
     */
    private void animVillains(int index) {
        
        Direction d = fantomes[index].getDirection();
        int x = fantomePos[index][0],
            y = fantomePos[index][1],
            speed = fantomes[index].getSpeed(),
            smallStepX = fantomes[index].getSmallStepX(),
            smallStepY = fantomes[index].getSmallStepY();

        switch (d) {
            case UP:
                if(smallStepX == 1 || (smallStepX == 2 && speed == 1)) {
                    fantomes[index].setSmallStepX(speed + smallStepX);
                }
                else {

                    if(x > 0 && data[x-1][y] != OPAQUE) {
                        fantomes[index].setSmallStepX(smallStepX == 2 ? 1 : speed);
                        fantomePos[index][0] = x - 1;
                    }
                    else {
                        pickOtherDirection(index, d);
                    }
                }
                break;

            case DOWN:

                if(smallStepX == 3 || (smallStepX == 2 && speed == 1)) {
                    fantomes[index].setSmallStepX(smallStepX - speed);
                }
                else {

                    if(x < W_BLOCKS - 1 && data[x+1][y] != OPAQUE) {
                        fantomes[index].setSmallStepX(smallStepX == 2 ? 3 : 4 - speed);
                        fantomePos[index][0] = x + 1;
                    }
                    else {
                        pickOtherDirection(index, d);
                    }
                }
                break;

            case LEFT:

                if(smallStepY == 1 || (smallStepY == 2 && speed == 1)) {
                    fantomes[index].setSmallStepY(speed + smallStepY);
                }
                else {

                    if(y > 0 && data[x][y] != OPAQUE && data[x][y-1] != OPAQUE) {
                        fantomes[index].setSmallStepY(smallStepY == 2 ? 1 : speed);
                        fantomePos[index][1] = y - 1;
                    }
                    else {
                        pickOtherDirection(index, d);
                    }
                }
                break;

            case RIGHT:

                if(smallStepY == 3 || (smallStepY == 2 && speed == 1)) {
                    fantomes[index].setSmallStepY(smallStepY - speed);
                }
                else {

                    if(y < H_BLOCKS - 1 && data[x][y+1] != OPAQUE) {
                        fantomes[index].setSmallStepY(smallStepY == 2 ? 3 : 4 - speed);
                        fantomePos[index][1] = y + 1;
                    }
                    else {
                        pickOtherDirection(index, d);
                    }
                }
                break;
        }

    }

    /**
     * Choisissez une direction aléatoire pour un Fantome
     *  
     * @param index
     * @param actual
     */
    private void pickOtherDirection(int index, Direction actual) {
        Direction picked;
        Random random = new Random();
        Direction tabDir[] = {
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT,
        };
        do {
            picked = tabDir[random.nextInt(tabDir.length)];
        }
        while(picked == actual);

        fantomes[index].setDirection(picked);
    }

    /**
     * Gérer la collision entre pacman et fantome
     */
    private void checkCollision() {
        int x = pacman.getX(),
            y = pacman.getY();
        for (int k = 0; k < fantomePos.length; k++) {
            if(fantomePos[k][0] == x && fantomePos[k][1] == y) {
                // Fantôme Retour au centre
                if(pacman.power.isSuperPacman()) {
                    fantomePos[k][0] = W_BLOCKS / 2;
                    fantomePos[k][1] = H_BLOCKS / 2 + 1;
                    fantomes[k].setSpeed(1);
                    fantomes[k].setSmallStepX(1);
                    fantomes[k].setSmallStepY(1);
                }
                else if(pacman.power.isInvisible()) {
                    // Rien à faire
                }
                else { // Pacman est touché
                    nb_life = nb_life - 1;
                    pacman.setColor(Color.DARK_GRAY);
                    isPacmanWasHurt = true;
                }
                break;
            }
        }
    }

    /**
     * Évaluer l'état d'une case  avant de déplacer le Pacman
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void manageNextCase(int x1, int y1, int x2, int y2) {

        int state = 0;

        switch(data[x2][y2]) {
            
            case OPAQUE: // Mur
                state = 0;
                break;

            case PACGOMME_BLEU:
                score += 100;
                state = 1;
                break;
            
            case PACGOMME_VOILET:
                score += 300;
                pacman.power.setInvisible(true);
                pacman.setColor(YELLOW_DARK_COLOR);
                state = 1;
                break;
            
            case PACGOMME_ORANGE:
                score += 500;
                pacman.power.setSuperPacman(true);
                pacman.setColor(ORANGE_COLOR);
                for (Fantome f : fantomes) {
                    f.setColor(Color.BLUE);
                }
                state = 1;
                break;
            
            case PACGOMME_VERT:
                score += 1000;
            
                // Changer la structure du labyrinthe
                int xLenght = data.length,
                yLenght = data[0].length,
                i = 0, j = 0;
                while(i < xLenght) {
                    j = 0;
                    while(j < yLenght) {
                        if(data[i][j] == OPAQUE && (j + 1 < yLenght)
                        && (data[i][j+1] == PACGOMME_BLEU || data[i][j+1] == -1)) {
                            data[i][j] = PACGOMME_BLEU;
                            data[i][j+1] = OPAQUE;
                            j = j + 1;
                        }
                        j = j + 1;
                    }
                    i = i+1;
                }
            
                state = 1;
                break;
            
            default:
                state = 1;
                break;
        }

        if(state == 0) {
            // Ne rien faire, c'est une zone opaque
        }
        else {
            // C'est un pacgomme, continuez et effacez la position réelle
            data[x1][y1] = -1;
            data[x2][y2] = 2;
        }

    }

    /**
     * Lorsque pacman frappe un fantome, réinitialisez la position du fantome
     */
    private void pacmanWasHurt() {
        if(countHurt <= 0) {
            isPacmanWasHurt = false;
            countHurt = 1000/40;
            data[pacman.getX()][pacman.getY()] = -1;
            data[14][10] = 2;
            pacman.setColor(Color.WHITE);
            pacman.setDirection(Direction.UP);
            return;
        }

        countHurt--;
    }

    /**
     * Gérer les événements clés (HAUT, BAS, GAUCHE, DROITE)
     */
    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_RIGHT:
                    keyDirection = Direction.RIGHT;
                    directionPressed = true;
                    break;

                case KeyEvent.VK_LEFT:
                    keyDirection = Direction.LEFT;
                    directionPressed = true;
                    break;

                case KeyEvent.VK_UP:
                    keyDirection = Direction.UP;
                    directionPressed = true;
                    break;

                case KeyEvent.VK_DOWN:
                    keyDirection = Direction.DOWN;
                    directionPressed = true;
                    break;

                case KeyEvent.VK_SPACE:
                    
                    if( ! isRunning) {

                        // Réinitialiser les données si le pacman est mort ou a gagné
                        if(isDied || isWon) {
                            initGame();
                        }

                        isRunning = true;
                        timer.start();
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if(isRunning) {
                        isRunning = false;
                        timer.stop();
                        repaint();
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    directionPressed = false;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Appel en interne entre chaque unité d'animation
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        handleGameLogic();
        this.repaint();
    }

    /**
     * JPanel
     */
    @Override
    public void addNotify() {
        super.addNotify();
    }

}
