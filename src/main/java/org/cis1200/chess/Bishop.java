package org.cis1200.chess;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bishop extends NormalPiece {

    boolean hasMoved;
    boolean isWhite;
    boolean hasKingInSight;
    State state;
    BufferedImage img;

    public Bishop(int y, int x, boolean white, Board b, State s) {
        super(white ? "files/whitebishop.png" : "files/blackbishop.png", x, y, white, b, s);
        file = x;
        rank = y;
        hasMoved = false;
        isWhite = white;
        board = b;
        state = s;
        linesOfSight = new ArrayList<>();
    }

    public String getSymbol() {
        String color = isWhite ? "W" : "B";
        return color + "B";
    }

    public void update() {
        linesOfSight.clear();
        ArrayList<int[]> checkLineOfSight;
        checkLineOfSight = new ArrayList<>();
        int x = file;
        int y = rank;
        while (x < 7 && y < 7) {
            x++;
            y++;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (x < 7 && y > 0) {
            x++;
            y--;
            checkLineOfSight.add(new int[] { y, x });

        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (x > 0 && y < 7) {
            x--;
            y++;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (x > 0 && y > 0) {
            x--;
            y--;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
    }

    public int getPoints() {
        return 3;
    }
}
