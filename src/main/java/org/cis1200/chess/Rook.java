package org.cis1200.chess;

import java.util.ArrayList;

public class Rook extends NormalPiece {
    boolean isWhite;

    public Rook(int y, int x, boolean white, Board b, State s) {
        super(white ? "files/whiterook.png" : "files/blackrook.png", x, y, white, b, s);
        file = x;
        rank = y;
        hasMoved = false;
        isWhite = white;
        board = b;
        linesOfSight = new ArrayList<>();
    }

    public String getSymbol() {
        String color = isWhite ? "W" : "B";
        return color + "R";
    }

    public void update() {
        linesOfSight.clear();
        ArrayList<int[]> checkLineOfSight;
        int x = file;
        int y = rank;
        checkLineOfSight = new ArrayList<>();
        while (x < 7) {
            x++;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (x > 0) {
            x--;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (y < 7) {
            y++;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
        checkLineOfSight = new ArrayList<>();
        x = file;
        y = rank;
        while (y > 0) {
            y--;
            checkLineOfSight.add(new int[] { y, x });
        }
        linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));

    }

    public int getPoints() {
        return 5;
    }
}
