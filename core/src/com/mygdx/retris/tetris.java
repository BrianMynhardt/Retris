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

    private final Point[][][] Tetrominoes = {
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


    private Point pieceStart;
    private int currentPiece;
    private int nextPiece;
    private int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
    private boolean state;
    int temp;
    private long score;
    private Texture[][] board;
    Texture border,middle,locked,active, waterB, waterF;


    // Creates a border and middle out of textures then puts a new active piece on the board
    public void init(Texture nborder,Texture nmiddle,Texture nlocked,Texture nactive,Texture wBorder, Texture wFill) {
        border = nborder;
        middle = nmiddle;
        locked = nlocked;
        active = nactive;
        waterB = wBorder;
        waterF = wFill;
        state = true;
        score = 0;
        board = new Texture[24][24];
        Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
        Collections.shuffle(nextPieces);
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                if(i<12 && j<23) {
                    if (i == 0 || i == 11 || j == 22) {
                        board[i][j] = border;
                    } else {
                        board[i][j] = middle;
                    }
                }else {
                    if((i==12 || i==18 || j==0 || j==6 || j==7)&&j<8 ){
                        board[i][j] = border;
                    }else{
                        if (j<8) {
                            board[i][j] = middle;
                        }else{
                            board[i][j] = locked;
                        }
                    }
                }
            }
        }
        newActive();
    }

    // Create a new active piece
    public void newActive() {

        pieceStart = new Point(5, 0);
        rotation = 0;
        temp = nextPieces.get(0);
        if(nextPieces.size()<=1){
            temp = nextPiece;
            Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nextPieces);

        }
        nextPiece = nextPieces.get(1);
        currentPiece = temp;
        nextPieces.remove(0);

        /*if (nextPieces.isEmpty()) {
            Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nextPieces);
        }
        currentPiece = nextPieces.get(0);
        nextPiece = nextPieces.get(1);
        nextPieces.remove(0);*/
        if(!collidesAt(pieceStart.x, pieceStart.y , rotation)){

            drawPiece();
            drawNextPiece();
        }else{
            gameOver();
        }

    }
    public void swap(){
        int temp=0;
        temp = nextPiece;
        nextPiece = currentPiece;
        currentPiece = temp;
        paintBoard();


    }

    // Collision test
    private boolean collidesAt(int x, int y, int rotation) {
        for (Point p : Tetrominoes[currentPiece][rotation]) {
            if (board[p.x + x][p.y + y] != middle && (board[p.x + x][p.y + y] != active) ){
                return true;
            }
        }
        return false;
    }

    //Rotate the active piece
    public void rotate(int i) {
        int nRotation = (rotation + i) % 4;
        if (nRotation < 0) {
            nRotation = 3;
        }
        if (!collidesAt(pieceStart.x, pieceStart.y, nRotation)) {
            rotation = nRotation;
        }
        paintBoard();
    }

    // Move the piece left or right
    public void move(int i) {
        if (!collidesAt(pieceStart.x + i, pieceStart.y, rotation)) {
            pieceStart.x += i;
        }

    }

    // Moves piece one down or adds it to pile
    public void dropDown() {
        if (!collidesAt(pieceStart.x, pieceStart.y + 1, rotation)) {
            pieceStart.y += 1;
            paintBoard();
        } else {
            lockPiece();
        }


    }

    // Make the dropping piece part of the well, so it is available for
    // collision detection.
    public void lockPiece() {
        for (Point p : Tetrominoes[currentPiece][rotation]) {
            board[pieceStart.x + p.x][pieceStart.y + p.y] = locked;
        }
        for (int i = 0; i <24 ; i++) {
            for (int j = 0; j <24 ; j++) {
                if (board[i][j] == active && i>11 && j<8) {
                    board[i][j] = middle;
                }
            }

        }
        clearRows();
        newActive();
    }

    public void deleteRow(int row) {
        for (int j = row-1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                board[i][j+1] = board[i][j];
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
                if (board[i][j] == middle) {
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

        for (Point p : Tetrominoes[currentPiece][rotation]) {
            board[p.x+pieceStart.x][p.y+pieceStart.y] = active;
        }
    }
    private void drawNextPiece() {
        for (int i = 0; i <24 ; i++) {
            for (int j = 0; j <24 ; j++) {
                if (board[i][j] == active && i>11 && j<8) {
                    board[i][j] = middle;
                }
            }

        }
        for (Point p : Tetrominoes[nextPiece][2]) {
            board[p.x+14][p.y+2] = active;
        }
    }

    //@Override
    public void paintBoard()
    {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                if(i<12){
                if (board[i][j] == active){
                    board[i][j] = middle;
                }
            }}

        }
        // Draw the currently falling piece
        drawNextPiece();
        drawPiece();

    }

    public Texture[][] getBoard() {
        return board;
    }

    public void setScore(long score) {
        this.score += score;
    }
    public boolean getState(){
        return state;
    }
    public long getScore(){
        return score;
    }

    public void gameOver(){
        state = false;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (i == 0 || i == 11 || j == 22) {
                    board[i][j] =border;
                } else{
                    board[i][j] = middle;
                }
            }

        }

    }


}

