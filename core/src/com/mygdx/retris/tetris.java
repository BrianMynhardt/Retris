package com.mygdx.retris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class tetris {

    private final Point[][][] Tetraminos = {
            // I-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
            },

            // J-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
            },

            // L-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
            },

            // O-Piece
            {
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
            },

            // S-Piece
            {
                    { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
                    { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
            },

            // T-Piece
            {
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
            },

            // Z-Piece
            {
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
            }
    };


    private Point pieceOrigin;
    private int currentPiece;
    private int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

    private long score;
    private Texture[][] well;
    Texture border,middle,locked,active;


    // Creates a border around the well and initializes the dropping piece
    public void init(Texture nborder,Texture nmiddle,Texture nlocked,Texture nactive) {
        border = nborder;
        middle = nmiddle;
        locked = nlocked;
        active = nactive;
        well = new Texture[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (i == 0 || i == 11 || j == 22) {
                    well[i][j] =border;
                } else {
                    well[i][j] = middle;
                }
            }
        }
        newPiece();
    }

    // Put a new, random piece into the dropping position
    public void newPiece() {
        pieceOrigin = new Point(5, 0);
        rotation = 0;
        if (nextPieces.isEmpty()) {
            Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nextPieces);
        }
        currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
        drawPiece();
    }

    // Collision test for the dropping piece
    private boolean collidesAt(int x, int y, int rotation) {
        for (Point p : Tetraminos[currentPiece][rotation]) {
            if (well[p.x + x][p.y + y] != middle && (well[p.x + x][p.y + y] != active) ){
                return true;
            }
        }
        return false;
    }

    // Rotate the piece clockwise or counterclockwise
    public void rotate(int i) {
        int newRotation = (rotation + i) % 4;
        if (newRotation < 0) {
            newRotation = 3;
        }
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
            rotation = newRotation;
        }
        paintBoard();
    }

    // Move the piece left or right
    public void move(int i) {
        if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
            pieceOrigin.x += i;
        }

    }

    // Drops the piece one line or fixes it to the well if it can't drop
    public void dropDown() {
        if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
            pieceOrigin.y += 1;
            paintBoard();
        } else {
            fixToWell();
        }


    }

    // Make the dropping piece part of the well, so it is available for
    // collision detection.
    public void fixToWell() {
        for (Point p : Tetraminos[currentPiece][rotation]) {
            well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = locked;
        }
        clearRows();
        newPiece();
    }

    public void deleteRow(int row) {
        for (int j = row-1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                well[i][j+1] = well[i][j];
            }
        }
    }

    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.
    public void clearRows() {
        boolean gap;
        int numClears = 0;

        for (int j = 21; j > 0; j--) {
            gap = false;
            for (int i = 1; i < 11; i++) {
                if (well[i][j] == middle) {
                    gap = true;
                    break;
                }
            }
            if (!gap) {
                deleteRow(j);
                j += 1;
                numClears += 1;
            }
        }

        switch (numClears) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 500;
                break;
            case 4:
                score += 800;
                break;
        }
    }

    // Draw the falling piece
    private void drawPiece() {

        for (Point p : Tetraminos[currentPiece][rotation]) {
            well[p.x+pieceOrigin.x][p.y+pieceOrigin.y] = active;
        }
    }

    //@Override
    public void paintBoard()
    {
        // Paint the well

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (well[i][j] == active){
                    well[i][j] = middle;
                }
            }
        }
        // Draw the currently falling piece
        drawPiece();

        // Display the score
        //g.setColor(Color.WHITE);
        //g.drawString("" + score, 19*12, 25);


    }

    public Texture[][] getWell() {
        return well;
    }

    public void setScore(long score) {
        this.score += score;
    }

    /* public static void main(String[] args) {
        JFrame f = new JFrame("Tetris");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(12*26+10, 26*23+25);
        f.setVisible(true);

        final Tetris game = new Tetris();
        game.init();
        f.add(game);

        // Keyboard controls
        f.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.rotate(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.rotate(+1);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.move(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.move(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.dropDown();
                        game.score += 1;
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        // Make the falling piece drop every second
        new Thread() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        game.dropDown();
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();
    }*/
}

